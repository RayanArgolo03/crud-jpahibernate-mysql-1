package services;

import exceptions.DatabaseException;
import exceptions.ProductException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import model.order.Product;
import repositories.interfaces.ProductRepository;
import utils.GetMockProductsUtils;

import java.util.LinkedHashSet;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductService {

    ProductRepository repository;

    public LinkedHashSet<Product> findProducts() {

        final LinkedHashSet<Product> products = repository.findAll();
        if (products.isEmpty()) throw new ProductException("There are no products in the database!");

        return products;
    }

    public void addProducts() {
        try {
            repository.addAll(GetMockProductsUtils.getMockProducts());
        }
        catch (DatabaseException e) {
            throw new ProductException(String.format("Error in add all products: %s", e.getMessage()), e);
        }
    }
}