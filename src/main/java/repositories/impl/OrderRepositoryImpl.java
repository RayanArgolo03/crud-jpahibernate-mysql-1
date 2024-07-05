package repositories.impl;

import model.order.Order;
import repositories.interfaces.OrderRepository;

import java.util.Set;
import java.util.UUID;

public final class OrderRepositoryImpl implements OrderRepository {

    @Override
    public Set<Order> findAll(UUID id) {
        return Set.of();
    }

    @Override
    public void save(Order order) {}
}
