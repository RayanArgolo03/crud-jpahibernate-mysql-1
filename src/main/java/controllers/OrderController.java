package controllers;

import dtos.output.OrderOutputDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import mappers.interfaces.OrderMapper;
import model.client.Client;
import model.order.Order;
import model.order.Product;
import services.OrderService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderController {

    OrderService service;
    OrderMapper mapper;

    public Set<OrderOutputDTO> findAll() {

        log.info("Find orders..");

        return service.findAllOrders().stream()
                .map(mapper::orderToOutput)
                .collect(Collectors.toSet());
    }

    public void create(final Client client, final Set<Product> products) {
        final Order order = service.placeOrder(client, products);
        //Else, order is cancelled
        if (Objects.nonNull(order)) {
            service.save(order);
            log.info("Order placed: {}", mapper.orderToOutput(order));
        }
    }

}
