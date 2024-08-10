package repositories.interfaces;

import enums.Category;
import jpa.JpaTransactionManager;
import lombok.extern.log4j.Log4j2;
import model.order.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import repositories.impl.ProductRepositoryImpl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
class ProductRepositoryTest {
    private ProductRepository repository;
    private Set<Product> productsExpected;


    @BeforeEach
    void setUp() {
        repository = new ProductRepositoryImpl(new JpaTransactionManager("h2"));

        productsExpected = new TreeSet<>(Comparator.comparing(Product::getName));
        productsExpected.addAll(Set.of(
                new Product(null, "Banana", new BigDecimal("1.00"), Set.of(Category.FOODS), null),
                new Product(null, "Strawberry", new BigDecimal("2.00"), Set.of(Category.FOODS), null)
        ));
    }

    @Nested
    @DisplayName("** FindAll products tests **")
    class FindAllTests {

        @Test
        void givenFindAll_whenProductsNotFound_thenReturnEmptySet() {
            final Set<Product> productsActual = repository.findAll();
            assertNotNull(productsActual);
            assertEquals(Set.of(), productsActual);
        }


        @Test
        void givenFindAll_whenProductsFound_thenReturnOrderedProductsSet() {

            repository.saveAll(productsExpected);
            final Set<Product> productsActual = repository.findAll();

            assertNotNull(productsActual);
            assertEquals(productsExpected, productsActual);
        }

    }

    @Nested
    @DisplayName("** AddAll products tests **")
    class AddAllTests {

        @Test
        void givenAddAll_whenProductsHaveBeenAdded_thenFindThemInDatabase() {

            repository.saveAll(productsExpected);
            productsExpected.forEach(p -> assertNotNull(p.getId()));

            final Set<Product> productsActual = repository.findAll();
            assertNotNull(productsActual);
            assertEquals(productsExpected, productsActual);
        }

    }
}