package repositories.impl;

import jakarta.persistence.EntityManager;
import jpautil.TransactionManagerUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.order.Product;
import repositories.interfaces.ProductRepository;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ProductRepositoryImpl implements ProductRepository {

    EntityManager em;

    @Override
    public void addAll(final Set<Product> mockProducts) {
        TransactionManagerUtil.execute(em, (aux) -> mockProducts.forEach(em::persist));
    }

    @Override
    public List<Product> findAll() {
        return null;
    }
}
