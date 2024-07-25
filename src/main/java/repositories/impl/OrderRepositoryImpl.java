package repositories.impl;

import enums.Category;
import jpa.JpaTransactionManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import model.order.Order;
import repositories.interfaces.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderRepositoryImpl implements OrderRepository {

    JpaTransactionManager transactionManager;

    @Override
    public LinkedHashSet<Order> findAll(final Client client) {
        final LinkedHashSet<Order> orders = transactionManager.getEntityManager()
                .createQuery("""
                        SELECT o
                        FROM Order o
                        WHERE o.client = :client
                        ORDER BY o.createdAt DESC
                        """, Order.class)
                .setParameter("client", client)
                .getResultStream()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        transactionManager.clearContextPersistence();
        return orders;
    }

    @Override
    public Set<Order> findByOrderDate(final Client client, final LocalDate orderDate) {

        return transactionManager.getEntityManager()
                .createQuery("""
                        SELECT o
                        FROM Order o
                        WHERE o.client = :client AND DATE(o.createdAt) = :orderDate
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("orderDate", orderDate)
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Order> findByTotalPrice(final Client client, final BigDecimal total) {

        return transactionManager.getEntityManager()
                .createQuery("""
                        SELECT o FROM Order o
                        JOIN o.orderItems oi
                        WHERE o.client = :client
                        GROUP BY o
                        HAVING CAST(SUM(oi.product.unitPrice * oi.quantity) AS STRING) LIKE :total
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("total", total + "%")
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Order> findByCategory(final Client client, final Category category) {

        return transactionManager.getEntityManager()
                .createQuery("""
                            SELECT o FROM Order o
                            JOIN o.orderItems oi
                            JOIN oi.product.categories c
                            WHERE o.client = :client AND c = :category
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("category", category)
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Order> findByProductName(final Client client, final String productName) {

        return transactionManager.getEntityManager()
                .createQuery("""
                        SELECT o FROM Order o
                        JOIN o.orderItems oi
                        WHERE o.client = :client AND oi.product.name = :productName
                        """, Order.class)
                .setParameter("client", client)
                .setParameter("productName", productName)
                .getResultStream()
                .collect(Collectors.toSet());
    }

    @Override
    public void save(final Order order) {
        transactionManager.execute((aux) -> aux.persist(order));
    }

    @Override
    public void delete(final Set<Order> orders) {
        transactionManager.execute((aux) -> orders.forEach(aux::remove));
    }
}
