package repositories.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import model.order.Product;
import repositories.interfaces.ProductRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductRepositoryImpl implements ProductRepository {

    EntityManager manager;

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public void addAll() {}
}
