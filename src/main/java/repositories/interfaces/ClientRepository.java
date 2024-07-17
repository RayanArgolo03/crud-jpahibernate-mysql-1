package repositories.interfaces;

import model.client.Client;

import java.util.Optional;

public interface ClientRepository {

    Optional<String> findUsername(String username);

    Optional<Client> findClient(String username, String password);

    void save(Client client);

}
