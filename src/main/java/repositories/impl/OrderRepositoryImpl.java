package repositories.impl;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import model.order.Order;
import repositories.interfaces.OrderRepository;
import utils.TransactionManagerUtils;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderRepositoryImpl implements OrderRepository {

    EntityManager em;

    @Override
    public LinkedHashSet<Order> findAll(final Client client) {
        return em.createQuery("SELECT o FROM Order o WHERE o.client = :client ORDER BY o.createdAt DESC", Order.class)
                .setParameter("client", client)
                .getResultStream()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void save(final Order order) {
        TransactionManagerUtils.executePersistence(em, (aux) -> aux.persist(order));
    }
}
