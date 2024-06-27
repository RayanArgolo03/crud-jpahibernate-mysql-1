package services;

import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import exceptions.ClientException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import repositories.ClientRepository;

import java.util.Objects;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ClientService {

    ClientRepository repository;
    //Mapper here

    public void findUsername(final String username) {
        //Se existir, estoure exceptio
    }

    public String validateAndFormatName(final String name) {

        Objects.requireNonNull(name, "Name can´t be null!");

        if (name.length() < 3) throw new ClientException("Short name!");

        if (!name.matches("^([A-Za-z])+$")) {
            throw new ClientException("Name contains special character!");
        }

        return name.substring(0, 1).toUpperCase().concat(name.substring(1).toLowerCase());
    }

    public void validatePassword(final String password) {
        Objects.requireNonNull(password, "Password can´t be null!");
        if (password.length() < 3) throw new ClientException("Short password!");
    }

    public void validateCpf(final String cpf) {

        Objects.requireNonNull(cpf, "CPF can´t be null!");

        if (!cpf.matches("(^\\d{3}\\x2E\\d{3}\\x2E\\d{3}\\x2D\\d{2}$)")) {
            throw new ClientException("Name contains special character!");
        }
    }

    public ClientInputDTO buildInput(final String username,
                                     final String password,
                                     final String name,
                                     final String cpf) {
        return ClientInputDTO.builder()
                .username(username)
                .password(password)
                .name(name)
                .cpf(cpf)
                .build();
    }

    public ClientOutputDTO saveClient(final ClientInputDTO clientInputDTO) {
        //Mapeia de input pra concreto com Mapstruct
        //Salva concreto no database chamando repo
        //retorna concreto mapeado pra output
        return null;
    }

}
