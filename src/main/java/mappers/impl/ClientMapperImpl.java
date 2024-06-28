package mappers.impl;

import domain.client.Client;
import dtos.ClientInputDTO;
import dtos.ClientOutputDTO;
import mappers.interfaces.ClientMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-28T17:58:56-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class ClientMapperImpl implements ClientMapper {

    private final DateTimeFormatter dateTimeFormatter_dd_MM_yyyy_0650712384 = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );

    @Override
    public Client inputToClient(ClientInputDTO inputDTO) {
        if ( inputDTO == null ) {
            return null;
        }

        LocalDateTime createdAt = null;
        String username = null;
        String name = null;
        String password = null;
        String cpf = null;

        createdAt = inputDTO.getCreationDate();
        username = inputDTO.getUsername();
        name = inputDTO.getName();
        password = inputDTO.getPassword();
        cpf = inputDTO.getCpf();

        UUID id = null;

        Client client = new Client( id, username, name, password, cpf, createdAt );

        return client;
    }

    @Override
    public ClientOutputDTO clientToOutput(Client client) {
        if ( client == null ) {
            return null;
        }

        String sinceDateFormatted = null;
        String clientName = null;
        UUID id = null;

        if ( client.getCreatedAt() != null ) {
            sinceDateFormatted = dateTimeFormatter_dd_MM_yyyy_0650712384.format( client.getCreatedAt() );
        }
        clientName = client.getName();
        id = client.getId();

        ClientOutputDTO clientOutputDTO = new ClientOutputDTO( id, clientName, sinceDateFormatted );

        return clientOutputDTO;
    }
}
