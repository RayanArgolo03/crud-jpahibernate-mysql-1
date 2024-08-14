package app;

import controllers.ClientController;
import controllers.OrderController;
import controllers.ProductController;
import enums.ClientOption;
import enums.MenuOption;
import exceptions.ProductException;
import jpa.JpaManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import mappers.ClientMapper;
import mappers.OrderMapper;
import model.client.Client;
import model.order.Order;
import repositories.impl.ClientRepositoryImpl;
import repositories.impl.OrderRepositoryImpl;
import repositories.impl.ProductRepositoryImpl;
import services.ClientService;
import services.OrderService;
import services.ProductService;

import java.util.InputMismatchException;
import java.util.Set;

import static utils.ReaderUtils.readEnum;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Main {
    static JpaManager JPA_MANAGER;
    static ClientController CLIENT_CONTROLLER;
    static OrderController ORDER_CONTROLLER;
    static ProductController PRODUCT_CONTROLLER;

    static {

        System.out.println("                                     -> INITIALISE Docker Hub and run docker-compose up -d!! <-     \n\n\n");

        JPA_MANAGER = new JpaManager("h2");

        CLIENT_CONTROLLER = new ClientController(
                new ClientService(new ClientRepositoryImpl(JPA_MANAGER), ClientMapper.INSTANCE)
        );

        ORDER_CONTROLLER = new OrderController(
                new OrderService(new OrderRepositoryImpl(JPA_MANAGER), OrderMapper.INSTANCE)
        );

        PRODUCT_CONTROLLER = new ProductController(
                new ProductService(new ProductRepositoryImpl(JPA_MANAGER))
        );
    }


    public static void main(String[] args) {


        log.info("This application use two databases: H2 to tests and MariaDB to CRUD. Products instanced GetMockProductsUtils class");

        try {
            PRODUCT_CONTROLLER.findAllProducts();
        }

        //If there are no products in the database
        catch (ProductException e) {
            log.info(e.getMessage());
            PRODUCT_CONTROLLER.addAll();
        }

        catch (Exception e) {
            log.error("Severe error: {}", e.getMessage());
            System.exit(0);
        }

        loop:
        do {
            try {
                switch (readEnum(MenuOption.class)) {

                    case SHOW_AVAILABLE_PRODUCTS -> {
                        PRODUCT_CONTROLLER.findAllProducts().forEach(System.out::println);
                        System.out.println();
                    }

                    case CREATE_CLIENT -> clientMenu(CLIENT_CONTROLLER.create());

                    case LOGIN -> clientMenu(CLIENT_CONTROLLER.login());

                    case OUT -> {
                        log.info("Thanks for the use :)");
                        break loop;
                    }
                }
            }
            catch (InputMismatchException e) {
                log.error("Input data error, stopping the program.. Thanks for the use");
                System.exit(0);
            }
            catch (ProductException e) {
                log.error("{}, restart the program! ", e.getMessage());
                System.exit(0);
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }

        } while (true);

    }


    public static void clientMenu(final Client client) {

        System.out.printf("         --- Welcome Sr/a %s --- \n", client.getName());

        do {
            try {

                switch (readEnum(ClientOption.class)) {

                    case SHOW_ORDERS -> ORDER_CONTROLLER.findAll(client).forEach(System.out::println);

                    case PLACE_AN_ORDER -> ORDER_CONTROLLER.create(client, PRODUCT_CONTROLLER.findAllProducts());

                    case DELETE_ORDERS_PLACED -> {
                        final Set<Order> orders = ORDER_CONTROLLER.find(client);
                        ORDER_CONTROLLER.delete(orders).forEach(System.out::println);
                    }


                    case LOGOUT -> {
                        log.info("{} has left the system!", client.getUsername());
                        return;
                    }
                }

            }
            catch (ProductException e) {
                log.error("{}, restart the program! ", e.getMessage());
                System.exit(0);
            }
            catch (InputMismatchException | IndexOutOfBoundsException e) {
                log.error("Input data error, stopping the program.. Thanks for the use");
                System.exit(0);
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }

        } while (true);

    }

}