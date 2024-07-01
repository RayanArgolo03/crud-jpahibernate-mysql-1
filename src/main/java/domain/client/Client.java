package domain.client;

import lombok.*;
import lombok.experimental.*;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class Client {

    @NonFinal
    @Setter
    UUID id;
    String username, name, password, cpf;
    LocalDateTime createdAt;
}
