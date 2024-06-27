package domain.client;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.*;

import java.util.UUID;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Client {
    @NonFinal
    UUID id;
    String username;
    String name;
    String password;
    String cpf;
}
