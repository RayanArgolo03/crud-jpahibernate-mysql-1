package domain.client;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.*;

import java.util.UUID;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Client {
    @NonFinal
    UUID id;
    String name;
    String cpf;
}
