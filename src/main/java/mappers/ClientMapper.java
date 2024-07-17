package mappers;

import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import model.client.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    Client inputToClient(ClientInputDTO inputDTO);

    @Mapping(target = "sinceDateFormatted", source = "createdAt", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "clientUsername", source = "username")
    ClientOutputDTO clientToOutput(Client client);

}
