package repositories.impl;

import jpa.JpaTransactionManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.order.Product;
import repositories.interfaces.ProductRepository;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductRepositoryImpl implements ProductRepository {

    JpaTransactionManager transactionManager;

    @Override
    public LinkedHashSet<Product> findAll() {

        final LinkedHashSet<Product> products = transactionManager.getEntityManager()
                .createQuery("""
                        SELECT p
                        FROM Product p
                        JOIN FETCH p.categories
                        ORDER BY p.name DESC
                        """, Product.class)
                .getResultStream()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        transactionManager.clearContextPersistence();
        return products;
    }

    @Override
    public void addAll(final Set<Product> mockProducts) {
        transactionManager.execute((aux) -> mockProducts.forEach(aux::persist));
    }

}
