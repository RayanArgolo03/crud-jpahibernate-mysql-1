package repositories.interfaces;

import domain.client.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    void save(Client client);
    Optional<Client> findUserClient(String username, String password);

}
