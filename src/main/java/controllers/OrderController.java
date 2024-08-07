package controllers;

import dtos.output.OrderOutputDTO;
import enums.FindOrderOption;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import model.client.Client;
import model.order.Order;
import model.order.Product;
import services.OrderService;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static utils.ReaderUtils.readEnum;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderController {

    OrderService service;

    public Set<Order> find(final Client client) {

        log.info("Receiving find option..");
        final FindOrderOption option = readEnum(FindOrderOption.class);

        return service.findByOption(client, option);
    }

    public Set<OrderOutputDTO> findAll(final Client client) {
        log.info("Finding orders..");
        return service.findAllOrders(client);
    }

    public void create(final Client client, final Set<Product> products) {

        //passing order item implementation (linked, hash, tree, etc.)
        final Order order = service.placeOrder(client, products, new LinkedHashSet<>());

        if (Objects.nonNull(order)) {
            service.save(order);
            log.info("Order placed!");
            System.out.println(service.mapperToOutput(order));
        }
    }

    public Set<OrderOutputDTO> delete(final Set<Order> orders) {

        log.info("Deleting orders..");
        final Set<OrderOutputDTO> ordersDeleted = service.deleteAll(orders);

        log.info("{} orders deleted: ", ordersDeleted.size());

        return ordersDeleted;
    }

}
