package mappers;

import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import model.client.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import utils.FormatterUtils;


@Mapper(imports = FormatterUtils.class)
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    Client inputToClient(ClientInputDTO inputDTO);

    @Mapping(target = "sinceDateFormatted", expression = "java(FormatterUtils.formatDate(client.getCreatedAt().toLocalDate()))")
    @Mapping(target = "clientUsername", source = "username")
    ClientOutputDTO clientToOutput(Client client);

}
