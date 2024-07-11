package repositories.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.order.Order;
import repositories.interfaces.OrderRepository;

import javax.persistence.EntityManager;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderRepositoryImpl implements OrderRepository {
    EntityManager manager;
    @Override
    public Set<Order> findAll() {
        return Set.of();
    }

    @Override
    public void save(Order order) {}
}
