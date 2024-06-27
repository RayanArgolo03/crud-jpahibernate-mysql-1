package controllers;

import dtos.input.ClientInputDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import services.ClientService;

import static utils.ReaderUtils.*;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientController {

    ClientService service;

    public void save() {

        //Import static utils
        final String username = readString("username");
        service.findUsername(username);

        final String password = readString("password (with more than 3 characters)");
        service.validatePassword(password);

        String name = readString("name (without special characters and with more than 3 characters)");
        name = service.validateAndFormatName(name);

        final String cpf = readString("cpf (pattern XXX.XXX.XXX-XX with dots and dash)");
        service.validateCpf(cpf);

        final ClientInputDTO inputDTO = service.buildInput(username, password, name, cpf);
        service.saveClient(inputDTO);
    }

}
