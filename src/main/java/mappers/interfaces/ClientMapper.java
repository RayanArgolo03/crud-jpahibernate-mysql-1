package mappers.interfaces;

import domain.client.Client;
import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.*;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = getMapper(ClientMapper.class);

    @Mapping(target = "createdAt", source = "creationDate")
    Client inputToClient(ClientInputDTO inputDTO);

    @Mapping(target = "sinceDateFormatted", source = "createdAt", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "clientUsername", source = "username")
    ClientOutputDTO clientToOutput(Client client);

}
