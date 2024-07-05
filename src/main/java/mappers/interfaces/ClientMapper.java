package mappers.interfaces;

import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import model.client.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.factory.Mappers.getMapper;


@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = getMapper(ClientMapper.class);

    Client inputToClient(ClientInputDTO inputDTO);

    @Mapping(target = "sinceDateFormatted", source = "createdAt", dateFormat = "dd/MM/yyyy")
    @Mapping(target = "clientUsername", source = "username")
    ClientOutputDTO clientToOutput(Client client);

}
