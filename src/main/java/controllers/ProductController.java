package controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import model.order.Product;
import services.ProductService;

import java.util.List;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductController {

    ProductService service;

    public void addAll() {
        log.info("Products not found, adding mock products..");
        service.addProducts();
    }

    public List<Product> findAll() {
        log.info("Finding products in database.. ");
//        return service.findAllProducts();
        return List.of();
    }
}
