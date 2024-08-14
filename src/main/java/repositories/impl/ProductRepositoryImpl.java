package repositories.impl;

import jpa.JpaManager;
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

    JpaManager jpaManager;

    @Override
    public Set<Product> findAll() {

        //Returning ordered products by tree set
        final Set<Product> products = new TreeSet<>(Comparator.comparing(Product::getName));

        products.addAll(
                jpaManager.getEntityManager()
                        .createQuery("SELECT p FROM Product p", Product.class)
                        .getResultList()
        );

       jpaManager.clearContextPersistence();
        return products;
    }

    @Override
    public void saveAll(final Set<Product> products) {
        jpaManager.executeAction((aux) -> products.forEach(aux::persist));
    }

}
