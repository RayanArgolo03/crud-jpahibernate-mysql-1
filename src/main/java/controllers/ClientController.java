package controllers;

import domain.client.Client;
import dtos.ClientInputDTO;
import dtos.ClientOutputDTO;
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

    public ClientOutputDTO create() {

        log.info("New user informations.. \n");

        final String username = readString("username");
        service.findUsername(username);

        final String password = readString("password (with more than 3 characters)");
        service.validatePassword(password);

        log.info("Your informations! \n");
        String name = readString("name (without special characters and with more than 3 characters)");
        name = service.validateAndFormatName(name);

        final String cpf = readString("cpf (pattern XXX.XXX.XXX-XX with dots and dash)");
        service.validateCpf(cpf);

        final ClientInputDTO inputDTO = new ClientInputDTO(username, name, password, cpf, LocalDateTime.now());
        final Client client = mapper.inputToClient(inputDTO);

        service.saveClient(client);
        System.out.println();

        return mapper.clientToOutput(client);
    }

    public ClientOutputDTO login() {

        final String username = readString("username");
        final String password = readString("password");

        final Client client = service.findClientUserInfo(username, password);
        return mapper.clientToOutput(client);
    }

}
