package services;

import dtos.output.OrderOutputDTO;
import enums.Category;
import enums.ContinueOption;
import enums.FindOrderOption;
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
import utils.FormatterUtils;
import utils.GetMockProductsUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    @DisplayName("** Find methods **")
    class FindTests {
        private Client client;
        private Order order;

        @BeforeEach
        void setUp() {

            client = Client.builder().build();

            //Mocking order
            OrderItem orderItem = OrderItem.builder()
                    .product(new ArrayList<>(GetMockProductsUtils.getMockProducts()).get(0))
                    .quantity(10)
                    .build();

            order = Order.builder()
                    .client(client)
                    .createdAt(LocalDateTime.now())
                    .orderItems(Set.of(orderItem))
                    .build();
        }


        @Nested
        @DisplayName("** FindAllOrders tests **")
        class FindAllOrdersTest {

            @Test
            void givenFindAllOrders_whenOrdersNotFound_thenThrowOrderException() {

                when(repository.findAll(client)).thenReturn(Set.of());

                final OrderException e = assertThrows(OrderException.class,
                        () -> service.findAllOrders(client));

                final String expected = format("Orders of client %s not found!", client.getName());
                assertEquals(expected, e.getMessage());

                verify(repository).findAll(client);

            }

            @Test
            void givenFindAllOrders_whenOrdersHasBeenFound_thenReturnMappedOrdersToOutputDTO() {

                final OrderOutputDTO expected = new OrderOutputDTO(
                        client.getName(),
                        order.getFormattedDate(),
                        FormatterUtils.formatOrderItems(order.getOrderItems()),
                        order.getTotal()
                );


                when(repository.findAll(client)).thenReturn(Set.of(order));
                when(mapper.orderToOutput(order)).thenReturn(expected);

                final List<OrderOutputDTO> orders = new ArrayList<>(service.findAllOrders(client));

                assertNotNull(orders);
                assertFalse(orders.isEmpty());
                assertEquals(expected, orders.get(0));

                verify(repository).findAll(client);
                verify(mapper).orderToOutput(order);

            }
        }


        @Nested
        @DisplayName("** FindByOrderDate order tests with System.Lambda library **")
        class FindByOrderDateTests {

            private FindOrderOption option;
            private String dateInString;

            @BeforeEach
            void setUp() {
                option = FindOrderOption.ORDER_DATE;
                dateInString = "10/10/2010";
            }


            @Test
            @SneakyThrows
            void givenFindByOption_whenOptionIsOrderDateAndOrdersNotFoundByOrderDate_thenThrowOrderException() {

                when(repository.findByOrderDate(eq(client), any(LocalDate.class))).thenReturn(Set.of());

                SystemStubs.withTextFromSystemIn(dateInString)
                        .execute(() -> {
                            final OrderException e = assertThrows(OrderException.class,
                                    () -> service.findByOption(client, option));

                            final String expected = format("Orders not found by %s", option);
                            assertEquals(expected, e.getMessage());

                            System.setIn(System.in);
                        });

                verify(repository).findByOrderDate(eq(client), any(LocalDate.class));
            }

            @Test
            @SneakyThrows
            void givenFindByOption_whenOptionIsOrderDateAndOrdersHasBeenFoundByOrderDate_thenReturnSetOfOrders() {

                when(repository.findByOrderDate(eq(client), any(LocalDate.class))).thenReturn(Set.of(order));

                SystemStubs.withTextFromSystemIn(dateInString)
                        .execute(() -> {
                            final List<Order> orders = new ArrayList<>(service.findByOption(client, option));

                            assertNotNull(orders);
                            assertFalse(orders.isEmpty());
                            assertEquals(order, orders.get(0));
                        });

                verify(repository).findByOrderDate(eq(client), any(LocalDate.class));
            }
        }

        @Nested
        @DisplayName("** FindByTotalPrice order tests with System.Lambda library **")
        class FindByTotalPriceTests {

            private FindOrderOption option;
            private String totalPrice;

            @BeforeEach
            void setUp() {
                option = FindOrderOption.TOTAL_PRICE;
                totalPrice = "10.50";
            }


            @SneakyThrows
            @Test
            void givenFindByOption_whenOptionIsTotalPriceAndOrdersNotFoundByTotalPrice_thenThrowOrdersException() {

                when(repository.findByTotalPrice(eq(client), any(BigDecimal.class))).thenReturn(Set.of());

                SystemStubs.withTextFromSystemIn(totalPrice)
                        .execute(() -> {
                            final OrderException e = assertThrows(OrderException.class,
                                    () -> service.findByOption(client, option));

                            final String expected = format("Orders not found by %s", option);
                            assertEquals(expected, e.getMessage());
                        });


                verify(repository).findByTotalPrice(eq(client), any(BigDecimal.class));
            }

            @Test
            @SneakyThrows
            void givenFindByOption_whenOptionIsTotalPriceAndAndOrdersHasBeenFoundByTotalPrice_thenReturnSetOfOrders() {

                when(repository.findByTotalPrice(eq(client), any(BigDecimal.class))).thenReturn(Set.of(order));

                SystemStubs.withTextFromSystemIn(totalPrice)
                        .execute(() -> {
                            final List<Order> orders = new ArrayList<>(service.findByOption(client, option));

                            assertNotNull(orders);
                            assertFalse(orders.isEmpty());
                            assertEquals(order, orders.get(0));
                        });

                verify(repository).findByTotalPrice(eq(client), any(BigDecimal.class));
            }
        }

        @Nested
        @DisplayName("** FindByProductName order tests with System.Lambda library **")
        class FindByProductNameTests {

            private FindOrderOption option;


            @BeforeEach
            void setUp() {
                option = FindOrderOption.PRODUCT_NAME;
            }


            @SneakyThrows
            @Test
            void givenFindByOption_whenOptionIsProductNameAndOrdersNotFoundByProductName_thenThrowOrdersException() {

                final String productName = "Tomato";

                when(repository.findByProductName(client, "Tomato")).thenReturn(Set.of());

                SystemStubs.withTextFromSystemIn(productName).execute(() -> {
                    final OrderException e = assertThrows(OrderException.class,
                            () -> service.findByOption(client, option));

                    final String expected = format("Orders not found by %s", option);
                    assertEquals(expected, e.getMessage());
                });


                verify(repository).findByProductName(client, productName);
            }

            @Test
            @SneakyThrows
            void givenFindByOption_whenOptionIsProductNameAndAndOrdersHasBeenFoundByProductName_thenReturnSetOfOrders() {

                final String productName = "OnionRigs";


                when(repository.findByProductName(client, productName)).thenReturn(Set.of(order));

                SystemStubs.withTextFromSystemIn(productName).execute(() -> {
                    final List<Order> orders = new ArrayList<>(service.findByOption(client, option));

                    assertNotNull(orders);
                    assertFalse(orders.isEmpty());
                    assertEquals(order, orders.get(0));
                });

                verify(repository).findByProductName(client, productName);
            }

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
    @DisplayName("** ValidateProductName order tests **")
    class ValidateProductNameTests {

        @Test
        void givenValidateProductName_whenProductNameIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.validateProductName(null));

            final String expected = "Name can´t be null!";
            assertEquals(expected, e.getMessage());

        }


        @Test
        void givenValidateProductName_whenNameHasLessThanThreeCharacters_thenThrowOrderException() {

            final String productName = "ab";

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.validateProductName(productName));

            final String expected = format("%s is a short name (less than three characters)", productName);
            assertEquals(expected, e.getMessage());


        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "abc123", "João3", "@Maria", "Hello!", "456def", "João Silva", "Maria@", "99RedBalloons", "*Star*", "foo_bar", "3littlePigs", "#hashtag", "Big$bucks", "money£", "piñata2", "João&Maria", "€uro", "Françoise3", "Jean-Luc", "smile:)", "9lives", ":colon:", "time4tea", "100%", "user_name", "-dash-", "dollar$", "hello_world", "star*t", "plus+sign", "white space", "question?", "open(", "close)", "up^", "down|", "quote\"", "double'quote"
        })
        void givenValidateProductName_whenNameContainsSpecialSymbol_thenThrowOrderException(final String productName) {

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.validateProductName(productName));

            final String expected = format("%s contains special symbol or white space", productName);
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenValidateProductName_whenNameIsValid_thenDoesNotThrowOrderException() {
            final String productName = "Strawberry";
            assertDoesNotThrow(() -> service.validateProductName(productName));
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

        @Test
        void givenDeleteAll_whenOrdersHasNotBeenDeleted_thenThrowOrderException() {

            doThrow(DatabaseException.class).when(repository).delete(orders);

            final OrderException e = assertThrows(OrderException.class,
                    () -> service.deleteAll(orders));

            assertNotNull(e.getCause());

            final String expected = format("Error in delete orders: %s", e.getCause().getMessage());

            assertEquals(DatabaseException.class, e.getCause().getClass());
            assertEquals(expected, e.getMessage());

        }


        @Test
        void givenDeleteAll_whenOrdersHasBeenDeleted_thenReturnDeletedOrdersMappedToOutput() {

            final Order order = new ArrayList<>(orders).get(0);

            final String itemsFormatted = FormatterUtils.formatOrderItems(order.getOrderItems());
            final String total = order.getTotal();

            doNothing().when(repository).delete(orders);

            when(mapper.orderToOutput(order)).thenReturn(
                    new OrderOutputDTO(order.getClient().getName(),
                            order.getFormattedDate(),
                            itemsFormatted,
                            total)
            );

            final Set<OrderOutputDTO> outputs = service.deleteAll(orders);

            assertNotNull(outputs);
            assertEquals(1, outputs.size());

            final OrderOutputDTO outputDTO = new ArrayList<>(outputs).get(0);

            assertEquals(order.getClient().getName(), outputDTO.getClientName());
            assertEquals(order.getFormattedDate(), outputDTO.getDateFormatted());
            assertEquals(itemsFormatted, outputDTO.getItemsFormatted());
            assertEquals(total, outputDTO.getTotal());

            verify(mapper).orderToOutput(order);
        }
    }

}