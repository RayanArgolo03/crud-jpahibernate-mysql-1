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
    public Set<Order> findAllByParams(final Client client, final OrderFilterParam params) {

        final List<Predicate> predicates = new ArrayList<>(List.of(
                builder.equal(root.get("client"), client)
        ));

        if (params.getOrderDate() != null) {
            final Expression<LocalDate> createdAtToLocalDate = builder.function("date", LocalDate.class, root.get("createdAt"));
            predicates.add(builder.equal(createdAtToLocalDate, params.getOrderDate()));
        }

        //To avoid duplicate joins in other conditions
        Join<Order, OrderItem> orderItemsTable = null;
        Join<OrderItem, Product> productTable = null;

        if (params.getTotalPrice() != null) {

            orderItemsTable = this.joinOrderItemsTable();
            productTable = this.joinProductTable(orderItemsTable);

            final Expression<BigDecimal> totalPrice = builder.sum(
                    builder.prod(productTable.get("unitPrice"), orderItemsTable.get("quantity"))
            );

            final Expression<String> substringTotalPrice = builder.substring(
                    totalPrice.as(String.class), 1, 1
            );

            final Predicate predicate = builder.like(substringTotalPrice, params.getTotalPrice() + "%");

            query.groupBy(root.get("id"))
                    .having(predicate);
        }

        if (params.getProductName() != null) {

            if (orderItemsTable == null) {
                orderItemsTable = this.joinOrderItemsTable();
                productTable = this.joinProductTable(orderItemsTable);
            }

            predicates.add(builder.equal(builder.lower(productTable.get("name")), params.getProductName()));
        }

        if (params.getCategory() != null) {

            if (orderItemsTable == null) {
                orderItemsTable = this.joinOrderItemsTable();
                productTable = this.joinProductTable(orderItemsTable);
            }

            final Join<Product, Category> categoryTable = productTable.join("categories", JoinType.LEFT);

            predicates.add(builder.equal(categoryTable, params.getCategory()));
        }

        query.where(predicates.toArray(Predicate[]::new));

        final Set<Order> orders = new HashSet<>(jpaManager.getEntityManager()
                .createQuery(query)
                .getResultList());

        jpaManager.clearContextPersistence();
        return orders;
    }

    private Join<Order, OrderItem> joinOrderItemsTable() {
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
    public void delete(final Set<Order> orders) {
        jpaManager.executeAction((aux) -> orders.forEach(aux::remove));
    }
}
