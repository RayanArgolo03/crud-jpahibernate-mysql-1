package app;

import controllers.ClientController;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import repositories.impl.ClientRepositoryImpl;
import services.ClientService;
import utils.ReaderUtils;

import java.util.InputMismatchException;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Main {
    public static void main(String[] args) {

        final ClientController clientController = new ClientController(new ClientService(
                new ClientRepositoryImpl()
        ));

        try {

            loop:
            do {
                switch (ReaderUtils.readOption()) {
                    case CREATE_CLIENT -> {

                    }
                    case LOGIN -> {
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
}