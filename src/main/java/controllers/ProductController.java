package controllers;

import model.order.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import services.ProductService;

import java.util.List;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductController {

    ProductService service;

    public void addAll() {
        log.info("Adding products in the database if not exists..");
        service.addAllProducts();
    }

    public List<Product> findAll() {
        log.info("Finding products.. ");
        return service.findAllProducts();
    }
}
