package dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientOutputDTO {

    UUID id;
    String clientName, sinceDateFormatted;

    @Override
    public String toString() {
        return String.format("%s - Since %s", clientName, sinceDateFormatted);
    }
}


