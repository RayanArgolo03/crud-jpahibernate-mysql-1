package jpautil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class JPAUtil {
    static EntityManager INSTANCE;
    public static EntityManager getInstance(final String unitPersistence) {

        if (Objects.isNull(INSTANCE)) {
            INSTANCE = Persistence.createEntityManagerFactory(unitPersistence).createEntityManager();
        }

        return INSTANCE;
    }

}
