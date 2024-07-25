package services;

import exceptions.ClientException;
import exceptions.DatabaseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import repositories.interfaces.ClientRepository;

import java.util.Objects;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientService {

    ClientRepository repository;

    public void findUsername(final String username) {
        if (repository.findUsername(username).isPresent()) {
            throw new ClientException(String.format("User with username %s already exists!", username));
        }
    }

    public String validateAndFormatName(final String name) {

        Objects.requireNonNull(name, "Name can´t be null!");

        if (name.length() < 3) throw new ClientException("Short name!");

        if (!name.matches("^([A-Za-z])+$")) {
            throw new ClientException("Name contains special character!");
        }

        return name.substring(0, 1).toUpperCase().concat(name.substring(1).toLowerCase());
    }

    public void validatePassword(final String password) {
        Objects.requireNonNull(password, "Password can´t be null!");
        if (password.length() < 3) throw new ClientException("Short password!");
    }

    public String validateAndFormatCpf(final String cpf) {

        Objects.requireNonNull(cpf, "CPF can´t be null!");

        if (!cpf.matches("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)")) {
            throw new ClientException("CPF does not match the pattern XXX.XXX.XXX-XX with dots and dash!");
        }

        return cpf.replaceAll("[^0-9]", "");
    }

    public void saveClient(final Client client) {

        Objects.requireNonNull(client, "Client can´t be null! Problem in mapping");

        try {
            repository.save(client);
            System.out.println();
        }
        catch (DatabaseException e) {
            throw new ClientException(String.format("Error in save client: %s", e.getMessage()), e);
        }

    }

    public Client findClient(final String username, final String password) {

        Objects.requireNonNull(username, "Username can´t be null! Problem in mapping");
        Objects.requireNonNull(password, "Password can´t be null! Problem in mapping");

        return repository.findClient(username, password)
                .orElseThrow(
                        () -> new ClientException(String.format("Client with username %s not found!", username))
                );
    }

}
