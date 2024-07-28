package repositories.interfaces;

import model.order.Product;

import java.util.Set;

public interface ProductRepository {

    Set<Product> findAll();

    void addAll(Set<Product> products);

}
