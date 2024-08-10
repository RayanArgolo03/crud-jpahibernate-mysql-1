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

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductRepositoryImpl implements ProductRepository {

    JpaTransactionManager transactionManager;

    @Override
    public Set<Product> findAll() {

        final Set<Product> products = new TreeSet<>(Comparator.comparing(Product::getName));
        products.addAll(
                transactionManager.getEntityManager()
                        .createQuery("""
                                SELECT p
                                FROM Product p
                                JOIN FETCH p.categories
                                """, Product.class)
                        .getResultList()
        )
        ;
        transactionManager.clearContextPersistence();
        return products;
    }

    @Override
    public void saveAll(final Set<Product> products) {
        transactionManager.executeAction((aux) -> products.forEach(aux::persist));
    }

}
