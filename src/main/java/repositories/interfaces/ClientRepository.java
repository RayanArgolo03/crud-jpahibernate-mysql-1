package repositories.interfaces;

import model.client.Client;

import java.util.Optional;

public interface ClientRepository {

    void save(Client client);
    Optional<Client> findUserClient(String username, String password);

}
