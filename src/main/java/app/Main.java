package app;

import controllers.ClientController;
import dtos.ClientOutputDTO;
import enums.ClientOption;
import enums.MenuOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import mappers.interfaces.ClientMapper;
import repositories.impl.ClientRepositoryImpl;
import services.ClientService;

import java.util.InputMismatchException;

import static utils.ReaderUtils.readEnum;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Main {
    static ClientController clientController = new ClientController(
            new ClientService(new ClientRepositoryImpl()), ClientMapper.INSTANCE
    );

    public static void main(String[] args) {

        try {
            loop:
            do {
                switch (readEnum(MenuOption.class)) {
                    case CREATE_CLIENT -> clientMenu(clientController.create());
                    case LOGIN -> {
                        clientMenu(clientController.login());
                    }
                    case OUT -> {
                        log.info("Thanks for the use :)");
                        break loop;
                    }
                }
            } while (true);

        } catch (InputMismatchException e) {
            log.error("Input data error, stopping the program.. Thanks for the use");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static void clientMenu(final ClientOutputDTO outputDTO) {

        //Print if is a new client or not
        log.info("Welcome {}", outputDTO);

        try {
            do {
                switch (readEnum(ClientOption.class)) {
                    case SHOW_ORDERS -> {
                        //Todo
                    }
                    case PLACE_AN_ORDER -> {
                    }
                    case LOGOUT -> {
                        return;
                    }
                }
            } while (true);

        } catch (InputMismatchException e) {
            log.error("Input data error, stopping the program.. Thanks for the use");
        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }
}