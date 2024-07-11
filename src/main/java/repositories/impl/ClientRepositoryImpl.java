package repositories.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import repositories.interfaces.ClientRepository;

import javax.persistence.EntityManager;
import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientRepositoryImpl implements ClientRepository {

    EntityManager manager;
    @Override
    public void save(final Client client) {
        //Implementar com banco de dados
        manager.persist("any");
    }

    @Override
    public Optional<Client> findUserClient(String username, String password) {
        //Implementar com banco de dados
        return Optional.empty();
    }
}
