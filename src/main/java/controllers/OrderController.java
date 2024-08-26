package controllers;

import dtos.output.OrderOutputDTO;
import enums.FindAllOption;
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

    public Set<OrderOutputDTO> findAll(final Client client) {

        log.info("Receiving find all option..");

        final FindAllOption option = readEnum(FindAllOption.class);
        return service.findAll(client, option);
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

    public void delete(final Client client) {
        log.info("{} orders deleted! ", service.deleteAllByParams(client));
    }

}
