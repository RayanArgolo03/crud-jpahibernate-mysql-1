package mappers.impl;

import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import mappers.interfaces.ClientMapper;
import model.client.Client;

import javax.annotation.processing.Generated;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-07-05T14:47:59-0300",
        comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class ClientMapperImpl implements ClientMapper {

    private final DateTimeFormatter dateTimeFormatter_dd_MM_yyyy_0650712384 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Client inputToClient(ClientInputDTO inputDTO) {
        if (inputDTO == null) {
            return null;
        }

        Client client = new Client(null, inputDTO.getUsername(), inputDTO.getName(), inputDTO.getPassword(), inputDTO.getCpf(), null);

        return client;
    }

    @Override
    public ClientOutputDTO clientToOutput(Client client) {
        if (client == null) {
            return null;
        }

        String sinceDateFormatted = null;
        String clientUsername = null;
        UUID id = null;

        if (client.getCreatedAt() != null) {
            sinceDateFormatted = dateTimeFormatter_dd_MM_yyyy_0650712384.format(client.getCreatedAt());
        }
        clientUsername = client.getUsername();
        id = client.getId();

        ClientOutputDTO clientOutputDTO = new ClientOutputDTO(id, clientUsername, sinceDateFormatted);

        return clientOutputDTO;
    }
}
