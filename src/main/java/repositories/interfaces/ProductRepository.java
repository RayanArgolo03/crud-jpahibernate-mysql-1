package repositories.interfaces;

import model.order.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    void addAll();
}
