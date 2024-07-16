package repositories.impl;

import jakarta.persistence.EntityManager;
import jpautil.TransactionManagerUtil;
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

    EntityManager em;

    @Override
    public void addAll(final Set<Product> mockProducts) {
        TransactionManagerUtil.executePersistence(em, (aux) -> mockProducts.forEach(em::persist));
    }

    @Override
    public LinkedHashSet<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class)
                        .getResultStream()
                        .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
