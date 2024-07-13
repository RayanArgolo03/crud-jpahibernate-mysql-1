package repositories.impl;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import repositories.interfaces.ClientRepository;

import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientRepositoryImpl implements ClientRepository {

    EntityManager manager;

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
