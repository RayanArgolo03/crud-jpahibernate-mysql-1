package repositories.interfaces;

import enums.Category;
import model.client.Client;
import model.order.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public interface OrderRepository {

    Set<Order> findAll(Client client);

    Set<Order> findByOrderDate(Client client, LocalDate orderDate);

    Set<Order> findByTotalPrice(Client client, BigDecimal total);

    Set<Order> findByCategory(Client client, Category category);

    Set<Order> findByProductName(Client client, String productName);

    void save(Order order);

    void delete(Set<Order> orders);

}
