package jpa;

import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import java.util.Objects;
import java.util.function.Consumer;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Log4j2
public final class JpaManager {

    EntityManager entityManager;
    CriteriaBuilder builder;

    public JpaManager(String unitPersitence) {
        this.entityManager = Persistence.createEntityManagerFactory(unitPersitence)
                .createEntityManager();

        builder = entityManager.getCriteriaBuilder();
    }

    public void executeAction(final Consumer<EntityManager> action) {

        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();

        } catch (Exception e) {

            if (Objects.nonNull(transaction)) {
                try {
                    transaction.rollback();
                } catch (Exception ee) {
                    log.error(ee.getMessage());
                }
            }

            throw new DatabaseException(e.getCause().getMessage(), e);

        } finally {
            clearContextPersistence();
        }

    }

    public void clearContextPersistence() {
        entityManager.clear();
    }


}
