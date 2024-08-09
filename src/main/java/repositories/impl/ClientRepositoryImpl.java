package repositories.impl;

import jpa.JpaTransactionManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import repositories.interfaces.ClientRepository;

import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientRepositoryImpl implements ClientRepository {

    JpaTransactionManager transactionManager;

    @Override
    public Optional<String> findUsername(final String username) {

        final Optional<String> optionalUsername = transactionManager.getEntityManager()
                .createQuery("SELECT c.username FROM Client c WHERE c.username = :username", String.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();

        transactionManager.clearContextPersistence();
        return optionalUsername;
    }

    @Override
    public Optional<Client> findClient(final String username, final String password) {

        final Optional<Client> optionalClient = transactionManager.getEntityManager()
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

        transactionManager.clearContextPersistence();
        return optionalClient;
    }

    @Override
    public void save(final Client client) {
        transactionManager.executeAction((aux) -> aux.persist(client));
    }
}
