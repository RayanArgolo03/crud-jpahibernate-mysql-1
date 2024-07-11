package repositories.interfaces;

import model.order.Product;

import java.util.List;

public interface ProductRepository {

    void addAll();

    List<Product> findAll();
}
