package services;

import enums.Category;
import enums.ContinueOption;
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

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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

    public Order placeOrder(final Client client, final LinkedHashSet<Product> products) {

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
            if (!orderItems.add(oi)) incrementQuantity(oi, quantity);

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
        System.out.printf("**  Product %s was purchased in this order with %d units, increasing quantity.. \n",
                oi.getProduct(),
                oi.getQuantity());

        oi.increaseQuantity(quantity);
    }

    public OrderItem createOrderItem(final Product product, final int quantity) {

        Objects.requireNonNull(product, "Product can´t be null!");

        if (quantity < 1) throw new OrderException("Quantity is negative number!");

        if (quantity > 99) throw new OrderException("Quantity is more than 100!");

        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }

    public void save(Order order) {
        Objects.requireNonNull(order, "Order can´t be null");
        try {
            repository.save(order);
        } catch (DatabaseException e) {
            throw new OrderException(String.format("Error in save new order: %s", e.getMessage()), e);
        }
    }

}
