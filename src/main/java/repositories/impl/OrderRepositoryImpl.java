package repositories.impl;

import enums.Category;
import jakarta.persistence.criteria.*;
import jpa.JpaManager;
import lombok.AllArgsConstructor;
import model.client.Client;
import model.order.Order;
import repositories.interfaces.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
public final class OrderRepositoryImpl implements OrderRepository {

    JpaManager jpaManager;

    @Override
    public Set<Order> findAll(final Client client) {

        final LinkedHashSet<Order> orders = new LinkedHashSet<>(
                jpaManager.getEntityManager()
                        .createQuery("""
                                SELECT o
                                FROM Order o
                                WHERE o.client = :client
                                ORDER BY o.createdAt DESC
                                """, Order.class)
                        .setParameter("client", client)
                        .getResultList()
        );

        jpaManager.clearContextPersistence();
        return orders;
    }

    @Override
    public Set<Order> findAllByParamsUsingCriteria(Client client) {
        return null;
    }


    //Todo continue aqui
    @Override
    public Set<Order> findByOrderDate(final Client client, final LocalDate orderDate) {

        final CriteriaBuilder builder = jpaManager.getBuilder();
        final CriteriaQuery<Order> query = builder.createQuery(Order.class);
        final Root<Order> root = query.from(Order.class);

        final Expression<LocalDate> createdDateInLocalDateFormat = builder.function("date", LocalDate.class, root.get("createdAt"));

        final Predicate[] predicates = new Predicate[]{
                builder.equal(root.get("client"), client),
                builder.equal(createdDateInLocalDateFormat, orderDate)
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
                        JOIN o.orderItems oi
                        WHERE o.client = :client
                        GROUP BY o
                        HAVING SUBSTRING( CAST(SUM(oi.product.unitPrice * oi.quantity) AS STRING), 1, 1) LIKE :total
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("total", total + "%")
                .getResultList());
    }

    @Override
    public Set<Order> findByCategory(final Client client, final Category category) {

        return new HashSet<>(jpaManager.getEntityManager()
                .createQuery("""
                            SELECT o FROM Order o
                            JOIN o.orderItems oi
                            JOIN oi.product.categories c
                            WHERE o.client = :client AND c = :category
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("category", category)
                .getResultList());
    }

    @Override
    public Set<Order> findByProductName(final Client client, final String productName) {

        return new HashSet<>(jpaManager.getEntityManager()
                .createQuery("""
                        SELECT o FROM Order o
                        JOIN o.orderItems oi
                        WHERE o.client = :client AND oi.product.name = :productName
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("productName", productName)
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
