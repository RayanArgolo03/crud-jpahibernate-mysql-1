package app;

import controllers.ClientController;
import controllers.OrderController;
import controllers.ProductController;
import enums.ClientOption;
import enums.MenuOption;
import exceptions.ProductException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import mappers.interfaces.ClientMapper;
import mappers.interfaces.OrderMapper;
import model.client.Client;
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
    static ClientController CLIENT_CONTROLLER = new ClientController(
            new ClientService(new ClientRepositoryImpl()), ClientMapper.INSTANCE
    );
    static OrderController ORDER_CONTROLLER = new OrderController(
            new OrderService(new OrderRepositoryImpl()), OrderMapper.INSTANCE
    );
    static ProductController PRODUCT_CONTROLLER = new ProductController(
            new ProductService(new ProductRepositoryImpl())
    );


    public static void main(String[] args) {

        //TODO use Jpql
        PRODUCT_CONTROLLER.addAll();
        log.info("This application use two databases: H2 and MySQL. The CRUD logic is in MySQL");

        loop:
        do {
            try {

                switch (readEnum(MenuOption.class)) {
                    //Todo mostra produtos no H2 e no MySQL
                    case SHOW_PRODUCTS -> {
                    }
                    case CREATE_CLIENT -> clientMenu(CLIENT_CONTROLLER.create());
                    case LOGIN -> clientMenu(CLIENT_CONTROLLER.login());
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

                    case SHOW_ORDERS -> ORDER_CONTROLLER.findAll(client).forEach(System.out::println);
                    case PLACE_AN_ORDER -> ORDER_CONTROLLER.create(client, PRODUCT_CONTROLLER.findAll());
                    case LOGOUT -> {
                        log.info("{} has left the system!", client);
                        return;
                    }
                }

            } catch (ProductException e) {
                log.info(e.getMessage());
                System.exit(0);
            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                log.error("Input data error, stopping the program.. Thanks for the use");
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        } while (true);

    }

}