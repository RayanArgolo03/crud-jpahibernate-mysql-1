package services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import model.order.Product;
import repositories.interfaces.ProductRepository;

import java.util.LinkedHashSet;

import static utils.GetMockProductsUtils.getMockProducts;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductService {

    ProductRepository repository;

    public LinkedHashSet<Product> findProducts() {
        return repository.findAll();
    }

    public void addProducts() {
        repository.addAll(getMockProducts());
    }
}