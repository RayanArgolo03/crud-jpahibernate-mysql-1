package app;

import controllers.ClientController;
import controllers.OrderController;
import controllers.ProductController;
import domain.client.Client;
import enums.ClientOption;
import enums.MenuOption;
import exceptions.ProductException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import mappers.interfaces.ClientMapper;
import mappers.interfaces.OrderMapper;
import repositories.impl.ClientRepositoryImpl;
import repositories.impl.OrderRepositoryImpl;
import repositories.impl.ProductRepositoryImpl;
import services.ClientService;
import services.OrderService;
import services.ProductService;

import java.util.InputMismatchException;

import static utils.ReaderUtils.readEnum;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Main {
    static ClientController clientController = new ClientController(
            new ClientService(new ClientRepositoryImpl()), ClientMapper.INSTANCE
    );
    static OrderController orderController = new OrderController(
            new OrderService(new OrderRepositoryImpl()), OrderMapper.INSTANCE
    );
    static ProductController productController = new ProductController(
            new ProductService(new ProductRepositoryImpl())
    );


    public static void main(String[] args) {

        productController.addAll();

        loop:
        do {
            try {

                switch (readEnum(MenuOption.class)) {
                    case CREATE_CLIENT -> clientMenu(clientController.create());
                    case LOGIN -> clientMenu(clientController.login());
                    case OUT -> {
                        log.info("Thanks for the use :)");
                        break loop;
                    }
                }
            } catch (InputMismatchException e) {
                log.error("Input data error, stopping the program.. Thanks for the use");
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        } while (true);

    }


    public static void clientMenu(final Client client) {

        log.info("\nWelcome Sr/a {}", client.getName());

        do {
            try {

                switch (readEnum(ClientOption.class)) {
                    case SHOW_ORDERS -> orderController.findAll(client).forEach(System.out::println);
                    case PLACE_AN_ORDER -> orderController.create(client, productController.findAll());
                    case LOGOUT -> {
                        log.info("{} has left the system!", client);
                        return;
                    }
                }

            }
            catch (ProductException e) {
                log.info(e.getMessage());
                System.exit(0);
            }
            catch (InputMismatchException | IndexOutOfBoundsException e) {
                log.error("Input data error, stopping the program.. Thanks for the use");
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }

        } while (true);

    }

}