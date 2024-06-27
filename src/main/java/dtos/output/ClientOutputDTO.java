package dtos.output;


import domain.client.Client;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClientOutputDTO {
    String username, name;
    public ClientOutputDTO(Client client) {
        this.username = client.getUsername();
        this.name = client.getName();
    }
}
