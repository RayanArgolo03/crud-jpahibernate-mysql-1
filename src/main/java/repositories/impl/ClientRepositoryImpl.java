package repositories.impl;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import repositories.interfaces.ClientRepository;
import utils.TransactionManagerUtils;

import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientRepositoryImpl implements ClientRepository {

    EntityManager em;

    @Override
    public Optional<String> findUsername(final String username) {
        return em.createQuery("SELECT c.name FROM Client c WHERE c.username = :username", String.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<Client> findClient(final String username, final String password) {
        return em.createQuery("""
                        SELECT c
                        FROM Client c
                        WHERE c.username = :username
                        AND c.password = :password
                        """, Client.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void save(final Client client) {
        TransactionManagerUtils.executePersistence(em, (aux) -> aux.persist(client));
    }
}
