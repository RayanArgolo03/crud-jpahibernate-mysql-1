package repositories.impl;

import criteria.OrderFilterParam;
import enums.Category;
import jakarta.persistence.criteria.*;
import jpa.JpaManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import model.order.Order;
import model.order.OrderItem;
import model.order.Product;
import repositories.interfaces.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderRepositoryImpl implements OrderRepository {

    JpaManager jpaManager;
    CriteriaBuilder builder;
    CriteriaQuery<Order> query;
    Root<Order> root;

    public OrderRepositoryImpl(JpaManager jpaManager) {

        this.jpaManager = jpaManager;

        //Init criteria api boilerplate
        builder = jpaManager.getEntityManager().getCriteriaBuilder();
        query = builder.createQuery(Order.class);
        root = query.from(Order.class);
    }

    @Override
    public Set<Order> findAll(final Client client) {
        final Set<Order> orders = new HashSet<>(jpaManager.getEntityManager()
                .createQuery(query.where(builder.equal(root.get("client"), client)))
                .getResultList());

        jpaManager.clearContextPersistence();
        return orders;
    }

    @Override
    public Set<Order> findAllByParams(final Client client, final OrderFilterParam orderFilterParam) {

        final Predicate[] predicates = this.createPredicates(query, root, client, orderFilterParam);

        query.where(predicates);

        final Set<Order> orders = new HashSet<>(jpaManager.getEntityManager()
                .createQuery(query)
                .getResultList());

        jpaManager.clearContextPersistence();
        return orders;
    }

    private Join<Order, OrderItem> joinOrderItemsTable(final Root<Order> root) {
        return root.join("orderItems", JoinType.LEFT);
    }

    private Join<OrderItem, Product> joinProductTable(final Join<Order, OrderItem> orderItemsTable) {
        return orderItemsTable.join("product", JoinType.LEFT);
    }

    @Override
    public Set<Order> findByOrderDate(final Client client, final LocalDate orderDate) {
        return new HashSet<>(jpaManager.getEntityManager()
                .createNamedQuery("Order.findByOrderDate", Order.class)
                .setParameter("client", client)
                .setParameter("orderDate", orderDate)
                .getResultList());
    }

    @Override
    public Set<Order> findByTotalPrice(final Client client, final BigDecimal total) {
        return new HashSet<>(jpaManager.getEntityManager()
                .createNamedQuery("Order.findByTotalPrice", Order.class)
                .setParameter("client", client)
                .setParameter("total", total + "%")
                .getResultList());
    }

    @Override
    public Set<Order> findByCategory(final Client client, final Category category) {
        return new HashSet<>(jpaManager.getEntityManager()
                .createNamedQuery("Order.findByCategory", Order.class)
                .setParameter("client", client)
                .setParameter("category", category)
                .getResultList());
    }

    @Override
    public Set<Order> findByProductName(final Client client, final String productName) {

        final Join<OrderItem, Product> productTable = joinProductTable(root.join("orderItems", JoinType.LEFT));

        final Predicate[] predicates = {
                builder.equal(root.get("client"), client),
                builder.equal(builder.lower(productTable.get("name")), productName)
        };

        return new HashSet<>(jpaManager.getEntityManager()
                .createQuery(query.where(predicates))
                .getResultList());

    }

    @Override
    public void save(final Order order) {
        jpaManager.executeAction((aux) -> aux.persist(order));
    }

    @Override
    public int deleteAllByParams(final Client client, final OrderFilterParam orderFilterParam) {

        final CriteriaDelete<Order> queryDelete = builder.createCriteriaDelete(Order.class);
        final Root<Order> rootDelete = queryDelete.from(Order.class);

        final Subquery<Order> deleteSubquery = queryDelete.subquery(Order.class);
        final Root<Order> rootSubquery = deleteSubquery.from(Order.class);

        final Predicate[] predicates = this.createPredicates(deleteSubquery, rootSubquery, client, orderFilterParam);

        //Set predicates to subquery
        deleteSubquery.select(rootSubquery).where(predicates);

        queryDelete.where(rootDelete.in(deleteSubquery));

        final AtomicInteger rowsDeleted = new AtomicInteger();

        jpaManager.executeAction((aux) -> rowsDeleted.set(aux.createQuery(queryDelete).executeUpdate()));

        return rowsDeleted.get();
    }

    private Predicate[] createPredicates(final AbstractQuery<Order> query, final Root<Order> root, final Client client, final OrderFilterParam orderFilterParam) {

        final List<Predicate> predicates = new ArrayList<>(
                List.of(builder.equal(root.get("client"), client))
        );

        if (orderFilterParam.getOrderDate() != null) {
            predicates.add(builder.equal(builder.function("date", LocalDate.class, root.get("createdAt")), orderFilterParam.getOrderDate()));
        }

        Join<Order, OrderItem> orderItemTable = null;
        Join<OrderItem, Product> productTable = null;

        if (orderFilterParam.getTotalPrice() != null) {

            orderItemTable = this.joinOrderItemsTable(root);
            productTable = this.joinProductTable(orderItemTable);

            final Expression<Number> totalPrice = builder.sum(builder.prod(productTable.get("unitPrice"), orderItemTable.get("quantity")));
            final Expression<String> substringTotalPrice = builder.substring(totalPrice.as(String.class), 1, 1);
            final Predicate likeTotalPrice = builder.like(substringTotalPrice, orderFilterParam.getTotalPrice() + "%");

            query.groupBy(root).having(likeTotalPrice);
        }

        if (orderFilterParam.getProductName() != null) {

            if (orderItemTable == null) {
                orderItemTable = this.joinOrderItemsTable(root);
                productTable = this.joinProductTable(orderItemTable);
            }

            predicates.add(builder.equal(builder.lower(productTable.get("name")), orderFilterParam.getProductName()));
        }

        if (orderFilterParam.getCategory() != null) {

            if (orderItemTable == null) {
                orderItemTable = this.joinOrderItemsTable(root);
                productTable = this.joinProductTable(orderItemTable);
            }

            final Join<Product, Category> categoriesTable = productTable.join("categories", JoinType.LEFT);

            predicates.add(builder.equal(categoriesTable, orderFilterParam.getCategory()));
        }

        return predicates.toArray(Predicate[]::new);
    }
}
