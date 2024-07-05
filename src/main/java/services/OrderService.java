package services;

import model.client.Client;
import model.order.Order;
import model.order.OrderItem;
import model.order.Product;
import enums.Category;
import enums.ContinueOption;
import exceptions.OrderException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import repositories.interfaces.OrderRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static utils.ReaderUtils.*;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderService {

    OrderRepository repository;

    public Set<Order> findOrdersByClient(final Client client) {

        Objects.requireNonNull(client, "Client can´t be null!");

        final Set<Order> orders = repository.findAll(client.getId());
        if (orders.isEmpty()) throw new OrderException("Orders not found");

        return orders;
    }

    public Order placeOrder(final Client client, final List<Product> products) {

        //Keeps the purchase order with LinkedHashSet
        final Set<OrderItem> orderItems = new LinkedHashSet<>();

        ContinueOption option;
        do {
            System.out.println("Enter with category option: ");
            Category category = readEnum(Category.class);

            List<Product> productsToChoose = products.stream()
                    .filter(p -> p.getCategory() == category)
                    .toList();

            Product product = readElement(productsToChoose);
            int quantity = readInt("product quantity (only positive numbers less than 11");
            validateQuantity(quantity);

            OrderItem oi = createOrderItem(product, quantity);

            boolean sucess = orderItems.add(oi);
            if (!sucess) {
                System.out.printf("Product %s was purchased in this order with %d units, increasing quantity..",
                        oi.getProduct(),
                        oi.getQuantity());

                increasingQuantity(oi, quantity);
            }

            System.out.println("Do continue buy? ");
            option = readEnum(ContinueOption.class);

            if (option == ContinueOption.CANCELLING) {
                System.out.println("Order cancelled!");
                return null;
            }

        } while (option != ContinueOption.NO);

        return Order.builder()
                .client(client)
                .orderDate(LocalDateTime.now())
                .orderItems(orderItems)
                .build();
    }

    public void validateQuantity(int quantity) {
        if (quantity < 1) throw new OrderException("Quantity is negative number!");
        if (quantity > 10) throw new OrderException("Quantity is more than 10!");
    }

    public void increasingQuantity(final OrderItem oi, final int quantity) {
        System.out.printf("Product %s was purchased in this order, increasing quantity", oi.getProduct());
        oi.incrementQuantity(quantity);
    }


    public OrderItem createOrderItem(final Product product, final int quantity) {

        Objects.requireNonNull(product, "Product can´t be null!");

        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }

    public void save(Order order) {
        Objects.requireNonNull(order, "Order can´t be null");
        repository.save(order);
    }

}
