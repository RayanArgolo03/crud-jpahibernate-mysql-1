package repositories.interfaces;

import enums.Category;
import jpa.JpaTransactionManager;
import model.client.Client;
import model.order.Order;
import model.order.OrderItem;
import model.order.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import repositories.impl.ClientRepositoryImpl;
import repositories.impl.OrderRepositoryImpl;
import repositories.impl.ProductRepositoryImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository repository;
    private Client client;
    private JpaTransactionManager manager;
    private Product product;
    private Order order;


    @BeforeEach
    void setUp() {
        manager = new JpaTransactionManager("h2");
        repository = new OrderRepositoryImpl(manager);

        client = Client.builder().name("abcd").username("abcd").password("abcd").cpf("12112122192")
                .orders(null)
                .build();

        //Passing to the MANAGED state
        new ClientRepositoryImpl(manager).save(client);

        product = new Product(null, "Banana", new BigDecimal("50.00"), Set.of(Category.FOODS), null, null);
        new ProductRepositoryImpl(manager).saveAll(Set.of(product));

        OrderItem oi = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();

        order = Order.builder().client(client).orderItems(Set.of(oi))
                .build();

        oi.setOrder(order);

    }

    @Nested
    @DisplayName("** FindAll orders tests **")
    class FindAllTests {

        @Test
        void givenFindAll_whenOrdersNotFound_thenReturnEmptySet() {
            //Passing to the MANAGED state
            assertEquals(Collections.EMPTY_SET, repository.findAll(client));
        }

        @Test
        void givenFindAll_whenOrdersFound_thenReturnSetOfOrderedOrdersAndClearPersistenceContext() {

            repository.save(order);

            //Expected ordered set
            Set<Order> expected = new TreeSet<>(Comparator.comparing(Order::getCreatedAt));
            expected.add(order);

            final Set<Order> actual = repository.findAll(client);

            assertNotNull(actual);
            assertEquals(expected, actual);
            assertFalse(manager.getEntityManager().contains(order));
        }

    }

    @Nested
    @DisplayName("** FindByOrderDate tests **")
    class FindByOrderDateTests {

        private LocalDate orderDate;

        @BeforeEach
        void setUp() {
            orderDate = LocalDate.now();
        }

        @Test
        void givenFindByOrderDate_whenOrdersNotFound_thenReturnEmptySet() {
            assertEquals(Collections.EMPTY_SET, repository.findByOrderDate(client, orderDate));
        }


    }

    @Nested
    @DisplayName("** FindByTotalPrice tests **")
    class FindByTotalPriceTests {

        private BigDecimal total;

        @BeforeEach
        void setUp() {
            total = new BigDecimal("1");
        }

        @Test
        void givenFindByTotalPrice_whenOrdersNotFound_thenReturnEmptySet() {
            assertEquals(Collections.EMPTY_SET, repository.findByTotalPrice(client, total));
        }

        @Test
        void givenFindByTotalPrice_whenOrdersFound_thenReturnSetOfOrders() {

            repository.save(order);

            //Expected ordered set
            Set<Order> expected = new HashSet<>(Set.of(order));
            final Set<Order> actual = repository.findByTotalPrice(client, total);

            assertNotNull(actual);
        }

    }
}

