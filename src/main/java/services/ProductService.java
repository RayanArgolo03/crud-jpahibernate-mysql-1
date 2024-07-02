package services;

import domain.order.Product;
import exceptions.ProductException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import repositories.interfaces.ProductRepository;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductService {

    ProductRepository repository;

    public List<Product> findAllProducts() {
        final List<Product> products = repository.findAll();
        if (products.isEmpty()) {
            throw new ProductException("Products not found: Restart the program immediately!");
        }
        return products;
    }

    public void addAllProducts() {
        repository.addAll();
    }
}