package repositories.interfaces;

import domain.order.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    void addAll();
}
