package controllers;

import dtos.input.ClientInputDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import mappers.ClientMapper;
import model.client.Client;
import services.ClientService;

import static utils.ReaderUtils.readString;

@Log4j2
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientController {

    ClientService service;
    ClientMapper mapper;

    public Client create() {

        log.info("Create client account: User informations.. \n");

//        final String username = readString("username");
        final String username = "abcd";
        service.findUsername(username);

       // final String password = readString("password (with more than 3 characters)");
        final String password = "abcd";
        service.validatePassword(password);

        log.info("Create client account: Client informations.. \n");

        //String name = readString("name (without special characters and with more than 3 characters)");
        String name = "aasassasa";
        name = service.validateAndFormatName(name);

        //final String cpf = readString("cpf (pattern XXX.XXX.XXX-XX with dots and dash)");
        String cpf = "121.221.121-92";
        cpf = service.validateAndFormatCpf(cpf);

        final ClientInputDTO inputDTO = new ClientInputDTO(username, name, password, cpf);
        final Client client = mapper.inputToClient(inputDTO);

        service.saveClient(client);
        log.info("{} online!", mapper.clientToOutput(client));

        return client;
    }

    public Client login() {

        final String username = readString("username");
        final String password = readString("password");

        final Client client = service.findClient(username, password);
        log.info("{} online!", mapper.clientToOutput(client));

        return client;
    }

}
