package repositories;

import domain.client.Client;

import java.util.UUID;

public interface ClientRepository {

    Client save(Client client);

}
