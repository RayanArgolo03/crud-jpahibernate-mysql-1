package app;

import controllers.ClientController;
import controllers.OrderController;
import controllers.ProductController;
import enums.ClientOption;
import enums.MenuOption;
import exceptions.DatabaseException;
import exceptions.ProductException;
import jakarta.persistence.EntityManager;
import jpautil.JPAUtil;
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
    static EntityManager MANAGER;
    static ClientController CLIENT_CONTROLLER;
    static OrderController ORDER_CONTROLLER;
    static ProductController PRODUCT_CONTROLLER;

    //Print the need to initialise docker
    static {
        System.out.println("                                     -> INITIALISE Docker Hub and run docker-compose up -d!! <-     \n\n\n");

        MANAGER = JPAUtil.getInstance("h2");

        CLIENT_CONTROLLER = new ClientController(
                new ClientService(new ClientRepositoryImpl(MANAGER)), ClientMapper.INSTANCE
        );

        ORDER_CONTROLLER = new OrderController(
                new OrderService(new OrderRepositoryImpl(MANAGER)), OrderMapper.INSTANCE
        );

        PRODUCT_CONTROLLER = new ProductController(
                new ProductService(new ProductRepositoryImpl(MANAGER))
        );
    }


    public static void main(String[] args) {


        log.info("This application use two databases: H2 to tests and MySQL to CRUD.");

        try {
            if (PRODUCT_CONTROLLER.findAll().isEmpty()) PRODUCT_CONTROLLER.addAll();
        } catch (DatabaseException e) {
            log.error("Severe error: {}", e.getMessage());
            System.exit(0);
        }

        loop:
        do {
            try {

                switch (readEnum(MenuOption.class)) {
                    case SHOW_PRODUCTS -> PRODUCT_CONTROLLER.findAll().forEach(System.out::println);
                    case CREATE_CLIENT -> clientMenu(CLIENT_CONTROLLER.create());
                    case LOGIN -> clientMenu(CLIENT_CONTROLLER.login());
                    case OUT -> {
                        log.info("Thanks for the use :)");
                        break loop;
                    }
                }
            } catch (InputMismatchException e) {
                log.error("Input data error, stopping the program.. Thanks for the use");
                System.exit(0);
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

                    case SHOW_ORDERS -> ORDER_CONTROLLER.findAll().forEach(System.out::println);
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
                System.exit(0);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        } while (true);

    }

}