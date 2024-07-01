package controllers;

import domain.client.Client;
import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import mappers.interfaces.ClientMapper;
import services.ClientService;

import java.time.LocalDateTime;

import static utils.ReaderUtils.*;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientController {

    ClientService service;
    ClientMapper mapper;

    public Client create() {

        log.info("Create client account: New user informations.. \n");

        final String username = readString("username");
        service.findUsername(username);

        final String password = readString("password (with more than 3 characters)");
        service.validatePassword(password);

        log.info("Create client account: Client informations.. \n");
        String name = readString("name (without special characters and with more than 3 characters)");
        name = service.validateAndFormatName(name);

        final String cpf = readString("cpf (pattern XXX.XXX.XXX-XX with dots and dash)");
        service.validateCpf(cpf);

        final ClientInputDTO inputDTO = new ClientInputDTO(username, name, password, cpf, LocalDateTime.now());
        final Client client = mapper.inputToClient(inputDTO);

        service.saveClient(client);
        System.out.println();

        log.info("{} online!", mapper.clientToOutput(client));

        return client;
    }

    public Client login() {

        final String username = readString("username");
        final String password = readString("password");

        final Client client = service.findClientUserInfo(username, password);
        log.info("{} online!", mapper.clientToOutput(client));

        return client;
    }

}
