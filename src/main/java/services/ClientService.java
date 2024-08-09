package services;

import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import exceptions.ClientException;
import exceptions.DatabaseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import mappers.ClientMapper;
import model.client.Client;
import repositories.interfaces.ClientRepository;

import java.util.Objects;

import static java.lang.String.format;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientService {

    ClientRepository repository;
    ClientMapper mapper;

    public void findUsername(final String username) {

        Objects.requireNonNull(username, "Username can´t be null!");

        if (repository.findUsername(username).isPresent()) {
            throw new ClientException(format("User with username %s already exists!", username));
        }
    }

    public String validateAndFormatName(final String name) {

        Objects.requireNonNull(name, "Name can´t be null!");

        if (name.length() < 3)
            throw new ClientException(format("Name %s is a short name! (less than 3 characters)", name));

        if (!name.matches("^([A-Za-z])+$")) {
            throw new ClientException(format("Name %s contains special character!", name));
        }

        return name.substring(0, 1).toUpperCase().concat(name.substring(1).toLowerCase());
    }

    public void validatePassword(final String password) {
        Objects.requireNonNull(password, "Password can´t be null!");
        if (password.length() < 3)
            throw new ClientException(format("Password %s is a short password! (less than 3 characters)", password));
    }

    public String validateAndFormatCpf(final String cpf) {

        Objects.requireNonNull(cpf, "CPF can´t be null!");

        if (!cpf.matches("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)")) {
            throw new ClientException(format("CPF %s does not match the pattern XXX.XXX.XXX-XX with dots and dash!", cpf));
        }

        return cpf.replaceAll("[^0-9]", "");
    }

    public void saveClient(final Client client) {

        Objects.requireNonNull(client, "Client can´t be null! Problem in mapping");

        try {
            repository.save(client);
        } catch (DatabaseException e) {
            throw new ClientException(format("Error in save client: %s", e.getMessage()), e);
        }

    }

    public Client findClient(final String username, final String password) {

        Objects.requireNonNull(username, "Username can´t be null! Problem in mapping");
        Objects.requireNonNull(password, "Password can´t be null! Problem in mapping");

        return repository.findClient(username, password)
                .orElseThrow(
                        () -> new ClientException(format("Client with username %s not found!", username))
                );
    }

    public Client mapperToClient(final ClientInputDTO inputDTO) {
        Objects.requireNonNull(inputDTO, "Client info can´t be null!");
        return mapper.inputToClient(inputDTO);
    }

    public ClientOutputDTO mapperToOutput(final Client client) {
        Objects.requireNonNull(client, "Client can´t be null!");
        return mapper.clientToOutput(client);
    }

}
