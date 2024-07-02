package repositories.interfaces;

import domain.order.Order;

import java.util.Set;
import java.util.UUID;

public interface OrderRepository {

    Set<Order> findAll(UUID id);

    void save(Order order);

}
