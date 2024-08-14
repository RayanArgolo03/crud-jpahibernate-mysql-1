package repositories.impl;

import jpa.JpaManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import repositories.interfaces.ClientRepository;

import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientRepositoryImpl implements ClientRepository {

    JpaManager jpaManager;

    @Override
    public Optional<String> findUsername(final String username) {

        final Optional<String> optionalUsername = jpaManager.getEntityManager()
                .createQuery("SELECT c.username FROM Client c WHERE c.username = :username", String.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();

        jpaManager.clearContextPersistence();
        return optionalUsername;
    }

    @Override
    public Optional<Client> findClient(final String username, final String password) {

        final Optional<Client> optionalClient = jpaManager.getEntityManager()
                .createQuery("""
                        SELECT c
                        FROM Client c
                        WHERE BINARY(c.username) = :username
                        AND BINARY(c.password) = :password
                        """, Client.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultStream()
                .findFirst();

        jpaManager.clearContextPersistence();
        return optionalClient;
    }

    @Override
    public void save(final Client client) {
        jpaManager.executeAction((aux) -> aux.persist(client));
    }
}
