package repositories.impl;

import model.order.Product;
import repositories.interfaces.ProductRepository;

import java.util.List;

public final class ProductRepositoryImpl implements ProductRepository {

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public void addAll() {
    }
}
