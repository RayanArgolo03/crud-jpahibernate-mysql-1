package services;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.order.Product;
import repositories.interfaces.ProductRepository;

import java.util.List;

import static utils.GetMockProductsUtils.getMockProducts;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductService {

    ProductRepository repository;

    public List<Product> findProducts() {
        return repository.findAll();
    }

    public void addProducts() {
        repository.addAll(getMockProducts());
    }
}