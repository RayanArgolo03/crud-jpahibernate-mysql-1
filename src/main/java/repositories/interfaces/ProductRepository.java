package repositories.interfaces;

import model.order.Product;

import java.util.LinkedHashSet;
import java.util.Set;

public interface ProductRepository {

    LinkedHashSet<Product> findAll();

    void addAll(Set<Product> mockProducts);

}
