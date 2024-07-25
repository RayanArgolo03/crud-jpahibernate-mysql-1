package jpa;

import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.function.Consumer;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public final class JpaTransactionManager {

    EntityManager entityManager;

    public JpaTransactionManager(String unitPersitence) {
        this.entityManager = Persistence.createEntityManagerFactory(unitPersitence)
                .createEntityManager();
    }

    public void execute(final Consumer<EntityManager> action) {

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        }
        catch (Exception e) {
            if (Objects.nonNull(transaction)) transaction.rollback();
            throw new DatabaseException(e.getCause().getMessage(), e);
        }
        finally {
           this.clearContextPersistence();
        }
    }

    public void clearContextPersistence() {entityManager.clear();}

}
