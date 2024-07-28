package repositories.interfaces;

import jpa.JpaTransactionManager;
import model.client.Client;
import org.junit.jupiter.api.*;
import repositories.impl.ClientRepositoryImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientRepositoryTest {

    private ClientRepository repository;
    private String username;


    @BeforeEach
    void setUp() {
        repository = new ClientRepositoryImpl(new JpaTransactionManager("h2"));
        username = "abcd";
    }

    @Nested
    @DisplayName("** FindUsername client tests **")
    class FindUsernameTests {

        @Test
        void givenFindUsername_whenUsernameFound_thenReturnOptionalOfUsername() {

            final Client client = Client.builder()
                    .name(username)
                    .username(username)
                    .password(username)
                    .cpf("12112122192")
                    .build();

            repository.save(client);

            assertEquals(Optional.of(client.getUsername()), repository.findUsername(client.getUsername()));

        }

        @Test
        void givenFindUsername_whenUsernameNotFound_thenReturnOptionalEmpty() {
            assertEquals(Optional.empty(), repository.findUsername(username));
        }
    }


    @Nested
    @DisplayName("** FindClient tests **")
    class FindClientTests {

        @Test
        void givenFindClient_whenClientFound_thenReturnOptionalOfClient() {

            final Client client = Client.builder()
                    .name(username)
                    .username(username)
                    .password(username)
                    .cpf("12112122192")
                    .build();

            repository.save(client);

            assertEquals(Optional.of(client), repository.findClient("abcd", "abcd"));
        }

    }


}