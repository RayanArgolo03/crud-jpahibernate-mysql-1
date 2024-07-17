package utils;

import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TransactionManagerUtils {

    public static void executePersistence(final EntityManager em, final Consumer<EntityManager> action) {

        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            action.accept(em);
            transaction.commit();
        }
        catch (Exception e) {
            if (Objects.nonNull(transaction)) transaction.rollback();
            throw new DatabaseException(e.getCause().getMessage(), e);
        }
        finally {
            em.clear();
        }


    }

}
