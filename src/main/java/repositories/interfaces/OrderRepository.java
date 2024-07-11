package repositories.interfaces;

import model.order.Order;

import java.util.Set;

public interface OrderRepository {

    Set<Order> findAll();

    void save(Order order);

}
