package repositories.interfaces;

import model.order.Product;

import java.util.List;
import java.util.Set;

public interface ProductRepository {

    List<Product> findAll();

    void addAll(Set<Product> mockProducts);
}
