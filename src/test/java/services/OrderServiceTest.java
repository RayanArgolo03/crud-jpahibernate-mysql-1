package services;

import criteria.OrderFilterParam;
import enums.Category;
import enums.ContinueOption;
import enums.FilterOrderOption;
import exceptions.DatabaseException;
import exceptions.OrderException;
import lombok.SneakyThrows;
import mappers.OrderMapper;
import model.client.Client;
import model.order.Order;
import model.order.OrderItem;
import model.order.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.interfaces.OrderRepository;
import uk.org.webcompere.systemstubs.SystemStubs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService service;

    @Mock
    private OrderRepository repository;

    @Spy
    private OrderMapper mapper = OrderMapper.INSTANCE;

    @Nested
    @DisplayName("SetFilterParamTests with SystemStubs")
    class SetFilterParamTests {

        private OrderFilterParam orderFilterParam;

        @BeforeEach
        void setUp() {
            orderFilterParam = new OrderFilterParam();
        }

        @SneakyThrows
        @Test
        void givenSetFilterParam_whenFilterOptionIsOrderDateAndHasBeenOrderDate_thenReplacingTheOrderDate() {

            final FilterOrderOption filterOption = FilterOrderOption.ORDER_DATE;
            final LocalDate expected = LocalDate.of(2024, 8, 2);

            orderFilterParam.setOrderDate(LocalDate.now());
            assertNotNull(orderFilterParam.getOrderDate());

            SystemStubs.withTextFromSystemIn("02/08/2024")
                    .execute(() -> {
                        service.setFilterParam(filterOption, orderFilterParam);

                        assertNotNull(orderFilterParam.getOrderDate());
                        assertEquals(expected, orderFilterParam.getOrderDate());
                    });
        }

        @SneakyThrows
        @Test
        void givenSetFilterParam_whenFilterOptionIsTotalPriceAndHasBeenTotalPrice_thenReplacingTheTotalPrice() {

            final FilterOrderOption filterOption = FilterOrderOption.TOTAL_PRICE;
            final BigDecimal expected = new BigDecimal("1200");

            orderFilterParam.setTotalPrice(new BigDecimal("1000"));
            assertNotNull(orderFilterParam.getTotalPrice());

            SystemStubs.withTextFromSystemIn("1200")
                    .execute(() -> {
                        service.setFilterParam(filterOption, orderFilterParam);

                        assertNotNull(orderFilterParam.getTotalPrice());
                        assertEquals(expected, orderFilterParam.getTotalPrice());
                    });
        }

//        @SneakyThrows
//        @Test
//        void givenSetFilterParam_whenFilterOptionIsCategoryAndHasBeenCategory_thenReplacingTheCategory() {
//
//            final FilterOrderOption filterOption = FilterOrderOption.CATEGORY;
//            final Category expected = Category.FOODS;
//
//            orderFilterParam.setCategory(Category.ELETRONICS);
//            assertNotNull(orderFilterParam.getCategory());
//
//            SystemStubs.withTextFromSystemIn(String.valueOf(expected.ordinal()))
//                    .execute(() -> {
//                        service.setFilterParam(filterOption, orderFilterParam);
//
//                        assertNotNull(orderFilterParam.getCategory());
//                        assertEquals(expected, orderFilterParam.getCategory());
//                    });
//        }

        @SneakyThrows
        @Test
        void givenSetFilterParam_whenFilterOptionIsProductNameAndHasBeenProductName_thenReplacingTheProductName() {

            final FilterOrderOption filterOption = FilterOrderOption.PRODUCT_NAME;
            final String expected = "Banana".toLowerCase();

            orderFilterParam.setProductName("Pickles");
            assertNotNull(orderFilterParam.getProductName());

            SystemStubs.withTextFromSystemIn(expected)
                    .execute(() -> {
                        service.setFilterParam(filterOption, orderFilterParam);

                        assertNotNull(orderFilterParam.getProductName());
                        assertEquals(expected, orderFilterParam.getProductName());
                    });
        }
    }

    @Nested
    @DisplayName("** ValidateAndFormatDate order tests **")
    class ValidateAndFormatDateTests {

        @Test
        void givenValidateAndFormatDateTests_whenDateIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.validateAndFormatDate(null));

            final String expected = "Order date can´t be null!";
            assertEquals(expected, e.getMessage());

        }

        @ParameterizedTest
        @ValueSource(strings = {"2024-12-25", "12-25-2024", "25/12/24", "25-12-2024", "Dec 25, 2024", "25-12-24", "25/12/2024 12:00", "2024/12/25", "25/12", "12/2024", "12/25/2024", "2024.12.25", "25.12.2024", "25/12-2024", "12/25/24", "December 25th, 2024", "12/25", "25/Dec/2024", "12 25 2024", "25 12 2024", "25/13/2024", "25/00/2024", "32/12/2024", "25/12/202", "25/12/02024", "25/12/abcd", "day/month/year", "01/January/2024", "MM/DD/YYYY", "YYYY/MM/DD", "13/25/2024", "2024/25/12", "12-25-24", "25/12/2024!", "25/12/20", "25th/12th/2024", "2024 December 25", "25.12.24", "24-12-25", "31/04/2024", "25/12/2024abc"})
        void givenValidateAndFormatDateTests_whenDateDoesNotMatchTheRequiredPattern_thenThrowOrderException(final String dateInString) {

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.validateAndFormatDate(dateInString));

            final String expected = format("%s is invalid date!", dateInString);
            assertEquals(expected, e.getMessage());


        }

        @Test
        void givenValidateAndFormatDateTests_whenDateIsValid_thenReturnTheDateInStringParsedToLocalDate() {

            final String dateInString = "10/10/2010";
            final LocalDate expected = LocalDate.of(2010, 10, 10);

            final LocalDate actual = service.validateAndFormatDate(dateInString);

            assertNotNull(actual);
            assertEquals(expected, actual);

        }

    }

    @Nested
    @DisplayName("** ValidateAndFormatTotalPrice order tests **")
    class ValidateAndFormatTotalPriceTests {

        @Test
        void givenValidateAndFormatTotalPrice_whenTotalPriceIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.validateAndFormatTotalPrice(null));

            final String expected = "Total price can´t be null!";
            assertEquals(expected, e.getMessage());

        }

        @ParameterizedTest
        @ValueSource(strings = {"abc", "123.45.67", "12,34,56", "12.34.56.78", "one", "123.45a", "12 34 56", "123-45", "12.34.5.6", "NaN", "-123-45", "123..45", "12..34", "1.2.3.4", "123.45-", "1e10.5", "1.23e10.5", "12,34", "123 45", "12-34", "12,34.56", "123.45.678", "1.2.3", "123..456", "12..34.56", "123.4.5", "12 34", "123 4 5", "12-34-56", "123-45-6", "12-34.56", "123.4..56", "12,34.5", "12,34 56", "1..23", "1e.10", "1e10..5", "123 45.6", "12-34.5", "1.2e.3"
        })
        void givenValidateAndFormatTotalPrice_whenTotalPriceIsInvalid_thenThrowOrderException(final String totalPriceInString) {

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.validateAndFormatTotalPrice(totalPriceInString));

            final String expected = format("%s is an invalid price", totalPriceInString);
            assertEquals(expected, e.getMessage());


        }

        @Test
        void givenValidateAndFormatTotalPrice_whenTotalPriceIsValid_thenReturnTheTotalPriceInStringParsedToBigDecimal() {

            final String totalPriceInstring = "10.50";
            final BigDecimal expected = new BigDecimal(totalPriceInstring);

            final BigDecimal actual = service.validateAndFormatTotalPrice(totalPriceInstring);

            assertNotNull(actual);
            assertEquals(expected, actual);

        }

    }


    @Nested
    @DisplayName("** ValidateAndFormatProductName order tests **")
    class ValidateAndFormatProductNameTests {

        @Test
        void givenValidateAndFormatProductName_whenProductNameIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.validateAndFormatProductName(null));

            final String expected = "Name can´t be null!";
            assertEquals(expected, e.getMessage());

        }


        @Test
        void givenValidateProductName_whenNameHasLessThanThreeCharacters_thenThrowOrderException() {

            final String productName = "ab";

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.validateAndFormatProductName(productName));

            final String expected = format("%s is a short name (less than three characters)", productName);
            assertEquals(expected, e.getMessage());


        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "abc123", "João3", "@Maria", "Hello!", "456def", "João Silva", "Maria@", "99RedBalloons", "*Star*", "foo_bar", "3littlePigs", "#hashtag", "Big$bucks", "money£", "piñata2", "João&Maria", "€uro", "Françoise3", "Jean-Luc", "smile:)", "9lives", ":colon:", "time4tea", "100%", "user_name", "-dash-", "dollar$", "hello_world", "star*t", "plus+sign", "white space", "question?", "open(", "close)", "up^", "down|", "quote\"", "double'quote"
        })
        void givenValidateProductName_whenNameContainsSpecialSymbol_thenThrowOrderException(final String productName) {

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.validateAndFormatProductName(productName));

            final String expected = format("%s contains special symbol or white space", productName);
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenValidateProductName_whenNameIsValid_thenReturnNameInLowerCase() {

            final String productName = "pOTTatOo";

            final String actual = service.validateAndFormatProductName(productName);

            assertEquals(productName.toLowerCase(), actual);
        }

    }

    @Nested
    @DisplayName("** CreateOrderItem tests **")
    class CreateOrderItemTests {

        private Product product;

        @BeforeEach
        void setUp() {
            product = new Product(null, "Tomato", null, null, null);
        }

        @Test
        void givenCreateOrderItem_whenProductIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.createOrderItem(null, 1));

            final String expected = "Product can´t be null!";
            assertEquals(expected, e.getMessage());
        }

        @Test
        void givenCreateOrderItem_whenQuantityIsNegative_thenThrowOrderException() {

            final int quantity = -1;

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.createOrderItem(product, quantity));

            final String expected = format("%d is a invalid quantity (only positive numbers less than 100)", quantity);
            assertEquals(expected, e.getMessage());
        }

        @Test
        void givenCreateOrderItem_whenQuantityIsZero_thenThrowOrderException() {

            final int quantity = 0;

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.createOrderItem(product, quantity));

            final String expected = format("%d is a invalid quantity (only positive numbers less than 100)", quantity);
            assertEquals(expected, e.getMessage());
        }

        @Test
        void givenCreateOrderItem_whenQuantityIsMoreNinetyNine_thenThrowOrderException() {

            final int quantity = 100;

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.createOrderItem(product, quantity));

            final String expected = format("%d is a invalid quantity (only positive numbers less than 100)", quantity);
            assertEquals(expected, e.getMessage());
        }

        @Test
        void givenCreateOrderItem_whenProductAndQuantityAreValid_thenReturnOrderItem() {

            final int quantity = 99;
            final OrderItem expected = OrderItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .build();

            assertEquals(expected, service.createOrderItem(product, quantity));
        }
    }

    @Nested
    @DisplayName("** PlaceOrder tests **")
    class PlaceOrderTests {

        private Client client;
        private Set<Product> products;

        @BeforeEach
        void setUp() {
            client = Client.builder().build();
            products = Set.of(new Product(null, "Banana", new BigDecimal("1.00"), Set.of(Category.FOODS), null));
        }


        @SneakyThrows
        @Test
        void givenPlaceOrder_whenProductHasAlredyBeenPurchasedInThisOrder_thenIncrementQuantity() {

            final int productIndex = 0, initialQuantity = 10;

            final OrderItem orderItem = OrderItem.builder()
                    .product(new ArrayList<>(products).get(productIndex))
                    .quantity(initialQuantity)
                    .build();

            final Set<OrderItem> mockOrderItems = Set.of(orderItem);

            final String inputReadOption = String.valueOf(ContinueOption.NO.ordinal()),
                    inputReadEnum = String.valueOf(Category.FOODS.ordinal()),
                    inputReadProduct = String.valueOf(productIndex),
                    inputReadQuantity = String.valueOf(10);

            SystemStubs.withTextFromSystemIn(inputReadEnum,
                    inputReadProduct,
                    inputReadQuantity,
                    inputReadOption).execute(() -> {

                final Order order = service.placeOrder(client, products, mockOrderItems);

                assertNotNull(order);
                assertEquals(1, order.getOrderItems().size());

                final OrderItem orderItemOfOrder = new ArrayList<>(order.getOrderItems()).get(productIndex);
                int quantityExpected = initialQuantity * 2;

                assertNotNull(orderItemOfOrder);
                assertEquals(quantityExpected, orderItemOfOrder.getQuantity());
            });

        }
    }

    @Nested
    @DisplayName("** SaveOrder tests **")
    class SaveOrderTests {

        private Order order;

        @BeforeEach
        void setUp() {
            order = Order.builder().build();
        }

        @Test
        void givenSaveOrder_whenOrderHasNotBennSaved_thenThrowOrderException() {

            doThrow(DatabaseException.class).when(repository).save(order);

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.save(order));

            assertNotNull(e.getCause());

            final String expected = format("Error in save new order: %s", e.getCause().getMessage());

            assertEquals(DatabaseException.class, e.getCause().getClass());
            assertEquals(expected, e.getMessage());

            verify(repository).save(order);
        }

        @Test
        void givenSaveOrder_whenOrderHasBennSaved_thenSetIdInOrder() {

            doAnswer((param) -> order = Order.builder()
                    .id(UUID.randomUUID())
                    .build()
            ).when(repository).save(order);

            assertNull(order.getId());

            service.save(order);

            assertNotNull(order.getId());

            verify(repository, atLeastOnce()).save(any());

        }
    }

    @Nested
    @DisplayName("** DeleteAll orders tests **")
    class DeletAllTests {

        private Set<Order> orders;

        @BeforeEach
        void setUp() {
            OrderItem oi = OrderItem.builder()
                    .product(new Product(null, "Banana", new BigDecimal("1.00"), Set.of(Category.FOODS), null))
                    .quantity(10)
                    .build();

            orders = Set.of(
                    Order.builder()
                            .client(Client.builder().name("Peter").build())
                            .createdAt(LocalDateTime.now())
                            .orderItems(Set.of(oi))
                            .build()
            );
        }
    }

}