package services;


import dtos.input.ClientInputDTO;
import dtos.output.ClientOutputDTO;
import exceptions.ClientException;
import exceptions.DatabaseException;
import mappers.ClientMapper;
import model.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import repositories.interfaces.ClientRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService service;
    @Mock
    private ClientRepository repository;
    @Spy
    private ClientMapper mapper = ClientMapper.INSTANCE;


    @Nested
    @DisplayName("** FindUsername client tests **")
    class FindUsernameTests {

        private String username;

        @BeforeEach
        void setUp() {
            username = "Arariboia";
        }

        @Test
        void givenFindUsername_whenUsernameIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.findUsername(null));

            final String expected = "Username can´t be null!";
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenFindUsername_whenUsernameFound_thenThrowClientException() {

            when(repository.findUsername(username)).thenReturn(Optional.of(username));

            final ClientException e = assertThrows(ClientException.class,
                    () -> service.findUsername(username));

            final String expected = format("User with username %s already exists!", username);
            assertEquals(expected, e.getMessage());

            verify(repository).findUsername(username);

        }

        @Test
        void givenFindUsername_whenUsernameNotFound_thenDoesNotThrowClientException() {
            when(repository.findUsername(username)).thenReturn(Optional.empty());
            assertDoesNotThrow(() -> service.findUsername(username));

            verify(repository).findUsername(username);
        }

    }

    @Nested
    @DisplayName("** ValidateAndFormatName client tests **")
    class ValidateAndFormatNameTests {

        @Test
        void givenValidateAndFormatName_whenNameIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.validateAndFormatName(null));

            final String expected = "Name can´t be null!";
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenValidateAndFormatName_whenNameIsAShortName_thenThrowClientException() {

            final String name = "Pê";

            final ClientException e = assertThrows(ClientException.class,
                    () -> service.validateAndFormatName(name));

            final String expected = format("Name %s is a short name! (less than 3 characters)", name);
            assertEquals(expected, e.getMessage());

        }

        @ParameterizedTest
        @ValueSource(strings = {"123", "abc123", "ABC!@#", "hello world", "foo_bar", "java-programming", "regex*test", "example@example.com", "space ", " tab", "new\nline", "carriage\rreturn", "slash\\back", "quote\"double", "single'quote", "semi;colon", "colon:colon", "comma,separated", "period.end"})
        void givenValidateAndFormatName_whenNameContainsSpecialCharacter_thenThrowClientException(final String name) {

            final ClientException e = assertThrows(ClientException.class,
                    () -> service.validateAndFormatName(name));

            final String expected = format("Name %s contains special character!", name);
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenValidateAndFormatName_whenIsAValidName_thenReturnFormattedName() {

            final String name = "euClIDeS";
            final String expected = "Euclides";

            assertEquals(expected, service.validateAndFormatName(name));
        }

    }

    @Nested
    @DisplayName("** ValidatePassword client tests **")
    class ValidatePasswordTests {

        @Test
        void givenValidatePassword_whenPasswordIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.validatePassword(null));

            final String expected = "Password can´t be null!";
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenValidatePassword_whenPasswordIsAShortPassword_thenThrowClientException() {

            final String password = "ab";

            final ClientException e = assertThrows(ClientException.class,
                    () -> service.validatePassword(password));

            final String expected = format("Password %s is a short password! (less than 3 characters)", password);
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenValidatePassword_whenIsAValidPassword_thenDoesNotThrowClientException() {
            final String password = "abc";
            assertDoesNotThrow(() -> service.validatePassword(password));
        }

    }

    @Nested
    @DisplayName("** ValidateAndFormatCpf client tests **")
    class ValidateAndFormatCpfPasswordTests {

        @Test
        void givenValidateAndFormatCpf_whenCpfIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.validateAndFormatCpf(null));

            final String expected = "CPF can´t be null!";
            assertEquals(expected, e.getMessage());

        }

        @ParameterizedTest
        @ValueSource(strings = {"abc", "12345678901", "123.456.789.01", "123-456-789-01", "123.456.789-012", "12.345.678-90", "123.45.678-90", "123.4567.89-01", "123.456.78-901", "abc.def.ghi-jk", "123.456.789-xx", "12a.456.789-01", "123.45b.789-01", "123.456.c89-01", "123.456.789-0d", "123.456.789-0@", "123.456.789-0!", "12..456.789-01", "123.456..789-01", "123..456.789-01", "12 3.456.789-01", "123.4 56.789-01", "123.456.7 89-01", "123.456.789- 01", " 123.456.789-01", "123.456.789-01 ", ".123.456.789-01", "123.456.789-01.", "-123.456.789-01", "123.456.789-01-", "123.456,789-01", "123.456.789/01", "123/456.789-01", "123.456/789-01", "123.456.789/01", "123:456.789-01", "123.456:789-01", "123.456.789:01", "123;456.789-01", "123.456;789-01", "123.456.789;01"})
        void givenValidateAndFormatCpf_whenCpfDoesNotMatchThePattern_thenThrowClientException(final String cpf) {

            final ClientException e = assertThrows(ClientException.class,
                    () -> service.validateAndFormatCpf(cpf));

            final String expected = format(format("CPF %s does not match the pattern XXX.XXX.XXX-XX with dots and dash!", cpf));
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenValidateAndFormatCpf_whenIsAValidCpf_thenReturnFormattedCpf() {

            final String cpf = "121.121.121-12";
            final String expected = "12112112112";

            assertEquals(expected, service.validateAndFormatCpf(cpf));
        }

    }

    @Nested
    @DisplayName("** SaveClient tests **")
    class SaveClientTests {

        private Client client;

        @BeforeEach
        void setUp() {
            client = Client.builder().build();
        }

        @Test
        void givenSaveClient_whenClientIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.saveClient(null));

            final String expected = "Client can´t be null! Problem in mapping";
            assertEquals(expected, e.getMessage());
        }

        @Test
        void givenSaveClient_whenHasProblemInTransaction_thenThrowClientException() {

            doThrow(DatabaseException.class).when(repository).save(client);

            final ClientException e = assertThrows(ClientException.class,
                    () -> service.saveClient(client));

            assertNotNull(e.getCause());

            final String expected = format("Error in save client: %s", e.getCause().getMessage());

            assertEquals(DatabaseException.class, e.getCause().getClass());
            assertEquals(expected, e.getMessage());

            verify(repository).save(client);
        }

        @Test
        void givenSaveClient_whenClientHasBeenSaved_thenSetIdInClient() {

            assertNull(client.getId());

            doAnswer((param) -> {
                client = Client.builder()
                        .id(UUID.randomUUID())
                        .build();
                return client;
            }).when(repository).save(client);

            service.saveClient(client);
            assertNotNull(client.getId());

            verify(repository).save(any());

        }

    }

    @Nested
    @DisplayName("** FindClient tests **")
    class FindClientTests {

        private String username, password;

        @BeforeEach
        void setUp() {
            username = "username";
            password = "password";
        }

        @Test
        void givenFindClient_whenUsernameIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.findClient(null, password));

            final String expected = "Username can´t be null! Problem in mapping";
            assertEquals(expected, e.getMessage());
        }

        @Test
        void givenFindClient_whenPasswordIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.findClient(username, null));

            final String expected = "Password can´t be null! Problem in mapping";
            assertEquals(expected, e.getMessage());
        }


        @Test
        void givenFindClient_whenClientNotFound_thenThrowClientException() {

            when(repository.findClient(username, password)).thenReturn(Optional.empty());

            final ClientException e = assertThrows(ClientException.class,
                    () -> service.findClient(username, password));

            final String expected = format("Client with username %s not found!", username);
            assertEquals(expected, e.getMessage());

            verify(repository).findClient(username, password);

        }

        @Test
        void givenFindClient_whenClientFound_thenReturnClient() {

            when(repository.findClient(username, password)).thenReturn(Optional.of(
                    Client.builder()
                            .id(UUID.randomUUID())
                            .username(username)
                            .password(password)
                            .build()
            ));

            final Client client = service.findClient(username, password);

            assertNotNull(client);
            assertNotNull(client.getId());
            assertEquals(username, client.getUsername());
            assertEquals(password, client.getPassword());

            verify(repository).findClient(username, password);

        }

    }

    @Nested
    @DisplayName("** Mapper client tests **")
    class MapperTests {

        @Test
        void givenMapperToClient_whenInputIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.mapperToClient(null));

            final String expected = "Client info can´t be null!";
            assertEquals(expected, e.getMessage());

        }

        @Test
        void givenMapperToClient_whenInputHasBeenMappedToClient_thenReturnClient() {

            final ClientInputDTO inputDTO = new ClientInputDTO("abcd", "abcd", "abcd", "12112112191");

            when(mapper.inputToClient(inputDTO)).thenReturn(
                    Client.builder()
                            .username(inputDTO.getUsername())
                            .password(inputDTO.getPassword())
                            .name(inputDTO.getName())
                            .cpf(inputDTO.getCpf())
                            .build());

            final Client client = service.mapperToClient(inputDTO);

            assertNotNull(client);
            assertEquals(inputDTO.getUsername(), client.getUsername());
            assertEquals(inputDTO.getPassword(), client.getPassword());
            assertEquals(inputDTO.getName(), client.getName());
            assertEquals(inputDTO.getCpf(), client.getCpf());

            verify(mapper).inputToClient(inputDTO);
        }


        @Test
        void givenMapperToOutput_whenClientHasBeenMappedToOutput_thenReturnClientOutputDTO() {

            //Formatter for convert local date time to string date formatted
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            final Client client = Client.builder()
                    .id(UUID.randomUUID())
                    .username("abcd")
                    .createdAt(LocalDateTime.now())
                    .build();

            when(mapper.clientToOutput(client)).thenReturn(
                    new ClientOutputDTO(client.getId(),
                            client.getUsername(),
                            client.getCreatedAt().format(formatter)
                    )
            );

            final ClientOutputDTO outputDTO = service.mapperToOutput(client);

            assertNotNull(outputDTO);
            assertEquals(outputDTO.getId(), client.getId());
            assertEquals(outputDTO.getClientUsername(), client.getUsername());
            assertEquals(outputDTO.getSinceDateFormatted(),client.getCreatedAt().format(formatter));

            verify(mapper).clientToOutput(client);
        }

        @Test
        void givenMapperToOutput_whenClientIsNull_thenThrowNullPointerException() {

            final NullPointerException e = assertThrows(NullPointerException.class,
                    () -> service.mapperToOutput(null));

            final String expected = "Client can´t be null!";
            assertEquals(expected, e.getMessage());

        }

    }

}