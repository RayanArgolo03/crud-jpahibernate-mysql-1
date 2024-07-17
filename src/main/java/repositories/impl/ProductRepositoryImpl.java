package repositories.impl;

import jakarta.persistence.EntityManager;
import utils.TransactionManagerUtils;
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
    public LinkedHashSet<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p ORDER BY p.name DESC", Product.class)
                .getResultStream()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void addAll(final Set<Product> mockProducts) {
        TransactionManagerUtils.executePersistence(em, (aux) -> mockProducts.forEach(aux::persist));
    }

}
