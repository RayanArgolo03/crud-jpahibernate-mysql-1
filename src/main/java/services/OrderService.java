package services;

import criteria.OrderFilterParam;
import dtos.output.OrderOutputDTO;
import enums.Category;
import enums.ContinueOption;
import enums.FilterOrderOption;
import enums.FindAllOption;
import exceptions.DatabaseException;
import exceptions.OrderException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import mappers.OrderMapper;
import model.client.Client;
import model.order.Order;
import model.order.OrderItem;
import model.order.Product;
import repositories.interfaces.OrderRepository;
import utils.FormatterUtils;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static utils.ReaderUtils.*;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderService {

    OrderRepository repository;
    OrderMapper mapper;


    public Set<OrderOutputDTO> findAll(final Client client, final FindAllOption option) {

        final Set<Order> orders = switch (option) {
            case FIND_ALL -> repository.findAll(client);

            case FIND_ALL_BY_PARAMS -> {
                final OrderFilterParam orderFilterParam = this.createOrderFilterParam();

                if (Objects.isNull(orderFilterParam)) {
                    System.out.println("Without params! Finding all orders normally..");
                    yield repository.findAll(client);
                }

                yield repository.findAllByParams(client, orderFilterParam);
            }
        };

        if (orders.isEmpty()) throw new OrderException(format("Orders of client %s not found!", client.getName()));

        return orders.stream()
                .map(this::mapperToOutput)
                .collect(Collectors.toSet());
    }

    public OrderFilterParam createOrderFilterParam() {

        final OrderFilterParam filterParams = new OrderFilterParam();

        ContinueOption continueOption;
        do {

            System.out.println("Enter with parameter option");
            FilterOrderOption filterOption = readEnum(FilterOrderOption.class);
            this.setFilterParam(filterOption, filterParams);

            System.out.println("Do continue passing params?");
            continueOption = readEnum(ContinueOption.class);

            if (continueOption == ContinueOption.CANCELLING) return null;

        } while (continueOption != ContinueOption.NO);

        return filterParams;

    }

    public void setFilterParam(final FilterOrderOption filterOption, final OrderFilterParam orderFilterParam) {

        switch (filterOption) {

            case ORDER_DATE -> {
                final LocalDate orderDate = this.validateAndFormatDate(
                        readSimpleString("order date (pattern dd/MM/yyyy with separators!)")
                );

                if (Objects.nonNull(orderFilterParam.getOrderDate())) {
                    this.printReplacingFilter("order date", FormatterUtils.formatDate(orderFilterParam.getOrderDate()),
                            FormatterUtils.formatDate(orderDate));
                }

                orderFilterParam.setOrderDate(orderDate);
            }

            case TOTAL_PRICE -> {
                final BigDecimal totalPrice = this.validateAndFormatTotalPrice(
                        readSimpleString("price (with dot and max three decimal places)")
                );

                if (Objects.nonNull(orderFilterParam.getTotalPrice())) {
                    this.printReplacingFilter("total price", FormatterUtils.formatCurrency(orderFilterParam.getTotalPrice()),
                            FormatterUtils.formatCurrency(totalPrice));
                }

                orderFilterParam.setTotalPrice(totalPrice);
            }
            case CATEGORY -> {

                final Category category = readEnum(Category.class);

                if (Objects.nonNull(orderFilterParam.getCategory())) {
                    this.printReplacingFilter("category", orderFilterParam.getCategory().getFormattedName(), category.getFormattedName());
                }

                orderFilterParam.setCategory(category);
            }

            case PRODUCT_NAME -> {

                final String productName = this.validateAndFormatProductName(
                        readSimpleString("product name (no special symbols, at least 3 characters!)")
                );

                if (Objects.nonNull(orderFilterParam.getProductName())) {
                    this.printReplacingFilter("product name", orderFilterParam.getProductName(), productName);
                }

                orderFilterParam.setProductName(productName);
            }
        }
    }

    private void printReplacingFilter(final String title, final String old, final String neww) {
        System.out.printf("Replacing %s filter: %s to %s", title, old, neww);
    }

    public Set<Order> findByOption(final Client client, final FilterOrderOption option) {

        final Set<Order> orders = switch (option) {

            case ORDER_DATE -> repository.findByOrderDate(client, this.validateAndFormatDate(
                    readSimpleString("order date (pattern dd/MM/yyyy with separators!)")
            ));

            case TOTAL_PRICE -> repository.findByTotalPrice(client, this.validateAndFormatTotalPrice(
                    readSimpleString("price (with dot and max three decimal places)")
            ));

            case CATEGORY -> repository.findByCategory(client, readEnum(Category.class));

            case PRODUCT_NAME -> repository.findByProductName(client, this.validateAndFormatProductName(
                    readSimpleString("product name (no special symbols, at least 3 characters!)")
            ));

        };

        if (orders.isEmpty()) throw new OrderException(format("Orders not found by %s", option));

        return orders;
    }

    public LocalDate validateAndFormatDate(final String dateInString) {

        Objects.requireNonNull(dateInString, "Order date can´t be null!");

        try {
            return LocalDate.parse(dateInString,FormatterUtils.getDATE_FORMATTER());

        } catch (DateTimeException e) {
            throw new OrderException(format("%s is invalid date!", dateInString));
        }
    }

    public BigDecimal validateAndFormatTotalPrice(String totalPriceInString) {

        Objects.requireNonNull(totalPriceInString, "Total price can´t be null!");

        try {
            return new BigDecimal(totalPriceInString);
        } catch (NumberFormatException e) {
            throw new OrderException(format("%s is an invalid price", totalPriceInString));
        }
    }

    public String validateAndFormatProductName(final String productName) {

        Objects.requireNonNull(productName, "Name can´t be null!");

        if (productName.length() < 3)
            throw new OrderException(format("%s is a short name (less than three characters)", productName));

        if (!productName.matches("^[A-Za-zÀ-ÖØ-öø-ÿ]+$"))
            throw new OrderException(format("%s contains special symbol or white space", productName));

        return productName.toLowerCase();

    }

    public Order placeOrder(final Client client, final Set<Product> products, final Set<OrderItem> orderItems) {

        ContinueOption option;
        do {
            System.out.println("Enter with category option: ");
            Category category = readEnum(Category.class);

            Product product = readProduct(products.stream()
                    .filter(p -> p.getCategories().contains(category))
                    .toList());

            int quantity = Integer.parseInt(readMockedString("product quantity (only positive numbers less than 100)"));

            OrderItem oi = createOrderItem(product, quantity);

            orderItems.stream()
                    .filter(oii -> oii.equals(oi))
                    .findFirst()
                    .ifPresentOrElse(
                            (existingOi) -> this.incrementQuantity(existingOi, quantity),
                            () -> orderItems.add(oi)
                    );

            System.out.println("Do continue buy?");
            option = readEnum(ContinueOption.class);

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

    public OrderItem createOrderItem(final Product product, final int quantity) {

        Objects.requireNonNull(product, "Product can´t be null!");

        if (quantity < 1 || quantity > 99)
            throw new OrderException(format("%d is a invalid quantity (only positive numbers less than 100)", quantity));

        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .build();
    }

    public void incrementQuantity(final OrderItem oi, final int quantity) {
        System.out.printf("** %s was purchased in this order with %d units, increasing quantity.. \n",
                oi.getProduct().getName(),
                oi.getQuantity());

        oi.increaseQuantity(quantity);
    }

    public void save(Order order) {
        try {
            repository.save(order);
        } catch (DatabaseException e) {
            throw new OrderException(format("Error in save new order: %s", e.getMessage()), e);
        }
    }

    public Set<OrderOutputDTO> deleteAll(final Set<Order> orders) {

        try {
            repository.delete(orders);

            return orders.stream()
                    .map(this::mapperToOutput)
                    .collect(Collectors.toSet());

        } catch (DatabaseException e) {
            throw new OrderException(format("Error in delete orders: %s", e.getMessage()), e);
        }
    }

    public OrderOutputDTO mapperToOutput(final Order order) {
        Objects.requireNonNull(order, "Order can´t be null!");
        return mapper.orderToOutput(order);
    }
}
