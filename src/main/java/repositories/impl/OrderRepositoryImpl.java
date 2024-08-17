package repositories.impl;

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
import java.util.HashSet;
import java.util.Set;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderRepositoryImpl implements OrderRepository {

    JpaManager jpaManager;
    CriteriaBuilder builder;
    CriteriaQuery<Order> query;
    Root<Order> root;

    public OrderRepositoryImpl(JpaManager jpaManager) {

        this.jpaManager = jpaManager;

        //Start Criteria API boilerplate
        builder = jpaManager.getBuilder();
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

    //Todo
    @Override
    public Set<Order> findAllByParamsUsingCriteria(Client client) {
        return null;
    }


    @Override
    public Set<Order> findByOrderDate(final Client client, final LocalDate orderDate) {

        final Expression<LocalDate> mappedCreatedAtToDate = builder.function("date", LocalDate.class, root.get("createdAt"));

        final Predicate[] predicates = new Predicate[]{
                builder.equal(root.get("client"), client),
                builder.equal(mappedCreatedAtToDate, orderDate)
        };

        return new HashSet<>(jpaManager.getEntityManager()
                .createQuery(query.where(predicates))
                .getResultList());
    }

    @Override
    public Set<Order> findByTotalPrice(final Client client, final BigDecimal total) {

        return new HashSet<>(jpaManager.getEntityManager()
                .createQuery("""
                        SELECT o FROM Order o
                        JOIN  o.orderItems oi
                        JOIN  oi.product p
                        WHERE o.client = :client
                        GROUP BY o
                        HAVING SUBSTRING( CAST(SUM(p.unitPrice * oi.quantity) AS STRING), 1, 1) LIKE :total
                         """, Order.class)
                .setParameter("client", client)
                .setParameter("total", total + "%")
                .getResultList());

    }

    @Override
    public Set<Order> findByCategory(final Client client, final Category category) {

        return new HashSet<>(jpaManager.getEntityManager()
                .createQuery("""
                        SELECT o
                        FROM Order o
                        JOIN FETCH o.orderItems oi
                        JOIN FETCH oi.product p
                        JOIN FETCH p.categories c
                        WHERE o.client = :client
                        AND c = :category
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("category", category)
                .getResultList());

    }

    @Override
    public Set<Order> findByProductName(final Client client, final String productName) {

        final Join<OrderItem, Product> productTable = root.join("orderItems", JoinType.LEFT)
                .join("product", JoinType.LEFT);

        final Predicate[] predicates = new Predicate[]{
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
