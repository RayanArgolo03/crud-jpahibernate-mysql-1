package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.Objects;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JPAUtils {
    static EntityManager MANAGER;

    public static EntityManager getManager(final String unitPersistence) {

        if (Objects.isNull(MANAGER)) {
            MANAGER = Persistence.createEntityManagerFactory(unitPersistence).createEntityManager();
        }

        return MANAGER;
    }
}
