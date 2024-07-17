package repositories.interfaces;

import model.client.Client;
import model.order.Order;

import java.util.LinkedHashSet;

public interface OrderRepository {

    LinkedHashSet<Order> findAll(final Client client);

    void save(Order order);

}
