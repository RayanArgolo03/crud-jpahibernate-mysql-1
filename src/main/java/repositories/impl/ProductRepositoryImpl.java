package repositories.impl;

import jpa.JpaTransactionManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.order.Product;
import repositories.interfaces.ProductRepository;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductRepositoryImpl implements ProductRepository {

    JpaTransactionManager transactionManager;

    @Override
    public Set<Product> findAll() {

        //Returning ordered products by tree set
        final Set<Product> products = transactionManager.getEntityManager()
                .createQuery("""
                        SELECT p
                        FROM Product p
                        JOIN FETCH p.categories
                        """, Product.class)
                .getResultStream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Product::getName))));

        transactionManager.clearContextPersistence();
        return products;
    }

    @Override
    public void addAll(final Set<Product> products) {
        transactionManager.execute((aux) -> products.forEach(aux::persist));
    }

}
