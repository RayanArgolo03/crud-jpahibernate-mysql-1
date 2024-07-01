package services;

import domain.client.Client;
import exceptions.ClientException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import repositories.interfaces.ClientRepository;

import java.util.Objects;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientService {

    ClientRepository repository;

    public void findUsername(final String username) {
        //Se existir, estoure exceptio
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

    public void validateCpf(final String cpf) {

        Objects.requireNonNull(cpf, "CPF can´t be null!");

        if (!cpf.matches("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)")) {
            throw new ClientException("Name contains special character!");
        }
    }

    public void saveClient(final Client client) {
        Objects.requireNonNull(client, "Client can´t be null! Problem in mapping");
        repository.save(client);
    }

    public Client findClientUserInfo(final String username, final String password) {

        Objects.requireNonNull(username, "Username can´t be null! Problem in mapping");
        Objects.requireNonNull(password, "Password can´t be null! Problem in mapping");

        return repository.findUserClient(username, password)
                .orElseThrow(() -> new ClientException("Client not found!"));
    }

}
