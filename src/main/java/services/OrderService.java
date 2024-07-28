package services;

import enums.Category;
import enums.ContinueOption;
import enums.FindOrderOption;
import exceptions.DatabaseException;
import exceptions.OrderException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import model.order.Order;
import model.order.OrderItem;
import model.order.Product;
import repositories.interfaces.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.format;
import static utils.ReaderUtils.*;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderService {

    OrderRepository repository;

    public Set<Order> findAllOrders(final Client client) {

        final Set<Order> orders = repository.findAll(client);
        if (orders.isEmpty()) throw new OrderException("Orders not found");

        return orders;
    }


    public Set<Order> findByOption(final Client client, final FindOrderOption option) {

        final Set<Order> ordersFound = switch (option) {

            case ORDER_DATE -> {
                final String dateInString = readString("order date (pattern dd/MM/yyyy with separators!)");
                yield repository.findByOrderDate(client, this.validateAndFormatDate(dateInString));
            }

            case TOTAL_PRICE -> {
                final String priceInString = readString("price (with comma, dot and max three decimal places)");
                yield repository.findByTotalPrice(client, this.validateAndFormatTotalPrice(priceInString));
            }

            case CATEGORY -> repository.findByCategory(client, readEnum(Category.class));

            case PRODUCT_NAME -> {
                final String productName = readString("product name (no special symbols, at least 3 characters!)");
                validateProductName(productName);
                yield repository.findByProductName(client, productName);
            }
        };

        if (ordersFound.isEmpty()) throw new OrderException(format("Orders not found by %s", option));

        return ordersFound;
    }

    public LocalDate validateAndFormatDate(final String dateInString) {

        Objects.requireNonNull(dateInString, "Order date can´t be null!");

        try {
            return LocalDate.parse(dateInString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            throw new OrderException(format("Date %s does not match the pattern dd/MM/yyyy", dateInString));
        }
    }

    public BigDecimal validateAndFormatTotalPrice(String priceInString) {

        Objects.requireNonNull(priceInString, "Total price can´t be null!");

        try {
            return new BigDecimal(priceInString.substring(0, 1));
        } catch (NumberFormatException e) {
            throw new OrderException(format("The price %s is an invalid price", priceInString));
        }
    }

    public void validateProductName(final String productName) {

        Objects.requireNonNull(productName, "Name can´t be null!");

        if (productName.length() < 3) throw new OrderException("Short product name");

        if (!productName.matches("^[A-Za-zÀ-ÖØ-öø-ÿ]+$")) throw new OrderException("Invalid product name");

    }

    public Order placeOrder(final Client client, final Set<Product> products) {

        //Keeps the purchase order with LinkedHashSet
        final Set<OrderItem> orderItems = new LinkedHashSet<>();

        ContinueOption option;
        do {
            System.out.println("Enter with category option: ");
            Category category = readEnum(Category.class);
            System.out.println();

            Product product = readProduct(products.stream()
                    .filter(p -> p.getCategories().contains(category))
                    .toList());
            System.out.println();

            int quantity = readInt("product quantity (only positive numbers less than 100)");
            System.out.println();

            OrderItem oi = createOrderItem(product, quantity);
            if (!orderItems.add(oi)) this.incrementQuantity(oi, quantity);

            System.out.println("Do continue buy?");
            option = readEnum(ContinueOption.class);
            System.out.println();

            if (option == ContinueOption.CANCELLING) {
                System.out.println("Order cancelled! Returning to menu..");
                return null;
            }

        } while (option != ContinueOption.NO);

        return Order.builder()
                .client(client)
                .createdAt(LocalDateTime.now())
                .orderItems(orderItems)
                .build();
    }

    public void incrementQuantity(final OrderItem oi, final int quantity) {
        System.out.printf("** %s was purchased in this order with %d units, increasing quantity.. \n",
                oi.getProduct().getName(),
                oi.getQuantity());

        oi.increaseQuantity(quantity);
    }

    public OrderItem createOrderItem(final Product product, final int quantity) {

        Objects.requireNonNull(product, "Product can´t be null!");

        if (quantity < 1) throw new OrderException("Quantity is negative number!");

        if (quantity > 99) throw new OrderException("Quantity is more or equals than 100!");

        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }

    public void save(Order order) {
        try {
            repository.save(order);
        } catch (DatabaseException e) {
            throw new OrderException(format("Error in save new order: %s", e.getMessage()), e);
        }
    }

    public Set<Order> deleteAll(final Set<Order> orders) {
        try {
            repository.delete(orders);
            return orders;
        } catch (DatabaseException e) {
            throw new OrderException(format("Error in delete orders: %s", e.getMessage()), e);
        }
    }
}
