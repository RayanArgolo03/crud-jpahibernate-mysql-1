package repositories.impl;

import model.client.Client;
import repositories.interfaces.ClientRepository;

import java.util.Optional;

public final class ClientRepositoryImpl implements ClientRepository {
    @Override
    public void save(final Client client) {
        //Implementar com banco de dados
    }

    @Override
    public Optional<Client> findUserClient(String username, String password) {
        //Implementar com banco de dados
        return Optional.empty();
    }
}
