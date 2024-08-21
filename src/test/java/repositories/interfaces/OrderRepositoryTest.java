package repositories.interfaces;

import criteria.OrderFilterParam;
import enums.Category;
import jpa.JpaManager;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository repository;
    private Client client;
    private JpaManager manager;
    private Product product;
    private Product product1;
    private Order order;


    @BeforeEach
    void setUp() {
        manager = new JpaManager("h2");
        repository = new OrderRepositoryImpl(manager);

        client = Client.builder().name("abcd").username("abcd").password("abcd").cpf("12112122192")
                .build();

        //Passing to the MANAGED state
        new ClientRepositoryImpl(manager).save(client);

        product = new Product(null, "Banana", new BigDecimal("50.00"), Set.of(Category.ELETRONICS, Category.FOODS), null);
        product1 = new Product(null, "Potato", new BigDecimal("50.00"), Set.of(Category.FOODS, Category.ELETRONICS), null);
        new ProductRepositoryImpl(manager).saveAll(Set.of(product, product1));

        order = Order.builder()
                .client(client)
                .createdAt(LocalDateTime.of(LocalDate.of(2010, 10, 10), LocalTime.now()))
                .orderItems(Set.of(OrderItem.builder()
                                .product(product)
                                .quantity(2)
                                .build(),
                        OrderItem.builder()
                                .product(product1)
                                .quantity(19)
                                .build()))
                .build();

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
    @DisplayName("** FindAllByParams tests **")
    class FindAllByParamsTests {

        private OrderFilterParam orderFilterParam;

        @BeforeEach
        void setUp() {
            repository.save(order);
            orderFilterParam = new OrderFilterParam();
        }

        @Test
        void givenFindAllByParams_whenOrderDateIsNotNull_thenFindWithOrderDate() {
            orderFilterParam.setOrderDate(order.getCreatedAt().toLocalDate());
            assertEquals(1, repository.findAllByParams(client, orderFilterParam).size());
        }

        @Test
        void givenFindAllByParams_whenTotalPriceIsNotNull_thenFindWithTotalPrice() {
            orderFilterParam.setTotalPrice(new BigDecimal("1"));
            assertEquals(1, repository.findAllByParams(client, orderFilterParam).size());
        }

        @Test
        void givenFindAllByParams_whenProductNameIsNotNull_thenFindWithProductName() {
            orderFilterParam.setProductName(product.getName());
            assertEquals(1, repository.findAllByParams(client, orderFilterParam).size());
        }

        @Test
        void givenFindAllByParams_whenCategoryIsNotNull_thenFindWithCategory() {
            orderFilterParam.setCategory(new ArrayList<>(product.getCategories()).get(0));
            assertEquals(1, repository.findAllByParams(client, orderFilterParam).size());
        }

        @Test
        void givenFindAllByParams_whenAllParamsIsNotNull_thenFindAllByParams() {

            orderFilterParam.setCategory(new ArrayList<>(product.getCategories()).get(0));
            orderFilterParam.setProductName(product.getName());
            orderFilterParam.setTotalPrice(new BigDecimal("1"));
            orderFilterParam.setOrderDate(order.getCreatedAt().toLocalDate());


            assertEquals(1, repository.findAllByParams(client, orderFilterParam).size());
        }

    }

    @Nested
    @DisplayName("** FindByOrderDate tests **")
    class FindByOrderDateTests {

        private LocalDate orderDate;

        @BeforeEach
        void setUp() {
            repository.save(order);
            orderDate = LocalDate.now();
        }


        @Test
        void givenFindByOrderDate_whenOrdersFound_thenReturnSetOfOrders() {
            Set<Order> orders = repository.findByOrderDate(client, orderDate);
            assertEquals(1, orders.size());
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
            Set<Order> byTotalPrice = repository.findByTotalPrice(client, total);
            assertEquals(1, byTotalPrice.size());
        }

    }

    @Nested
    @DisplayName("** FindByCategory tests **")
    class FindByCategoryTests {

        private Category category;

        @BeforeEach
        void setUp() {
            category = Category.ELETRONICS;
            repository.save(order);
        }

        @Test
        void givenFindByCategory_whenOrdersFound_thenReturnSetOfOrders() {
            Set<Order> byCategory = repository.findByCategory(client, category);
            for (Order o : byCategory) System.out.println(o.getOrderItems());
            assertFalse(byCategory.isEmpty());
        }

    }

    @Nested
    @DisplayName("** FindByProductName tests **")
    class FindByProductNameTests {

        private String productName;

        @BeforeEach
        void setUp() {
            productName = "BAnaNA".toLowerCase();
        }

        @Test
        void givenFindByProductName_whenOrdersNotFound_thenReturnEmptySet() {
            assertEquals(0, repository.findByProductName(client, productName).size());
        }

        @Test
        void givenFindByProductName_whenOrdersFound_thenReturnSetOfOrders() {
            repository.save(order);
            assertEquals(1, repository.findByProductName(client, productName).size());
        }

    }
}

