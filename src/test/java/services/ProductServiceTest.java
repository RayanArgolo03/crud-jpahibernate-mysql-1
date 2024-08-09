package services;

import enums.Category;
import exceptions.DatabaseException;
import exceptions.ProductException;
import model.order.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.interfaces.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService service;

    @Mock
    ProductRepository repository;

    @Nested
    @DisplayName("** FindAll products tests **")
    class FindAllTests {

        @Test
        void givenFindAll_whenProductsNotFound_thenThrowProductException() {

            when(repository.findAll()).thenReturn(Set.of());

            final ProductException e = assertThrows(ProductException.class,
                    () -> service.findAll());

            final String expected = "There are no products in the database!";
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenFindAll_whenProductsFound_thenReturnSetOfProducts() {

            final Product expected = new Product(null, "Banana", new BigDecimal("1.00"), Set.of(Category.FOODS), null);

            when(repository.findAll()).thenReturn(Set.of(expected));

            final Set<Product> products = service.findAll();

            assertNotNull(products);
            assertEquals(1, products.size());

            final Product actual = new ArrayList<>(products).get(0);

            assertEquals(expected, actual);
        }

        @Nested
        @DisplayName("** AddAll products tests **")
        class AddAllTests {

            @Test
            void givenAddAll_whenProductsHasNotBeenAdded_thenThrowProductException() {

                doThrow(DatabaseException.class).when(repository).saveAll(any());

                final ProductException e = assertThrows(ProductException.class,
                        () -> service.addAll());

                assertNotNull(e.getCause());

                final String expected = format("Error in add all products: %s", e.getCause().getMessage());

                assertEquals(DatabaseException.class, e.getCause().getClass());
                assertEquals(expected, e.getMessage());
            }

            @Test
            void givenAddAll_whenProductsHasBeenAdded_thenDoesNotThrowProductException() {
                doNothing().when(repository).saveAll(any());
                assertDoesNotThrow(() -> service.addAll());
            }

        }


    }

}