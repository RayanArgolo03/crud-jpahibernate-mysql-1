package repositories.interfaces;

import jpa.JpaTransactionManager;
import model.client.Client;
import org.junit.jupiter.api.*;
import repositories.impl.ClientRepositoryImpl;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientRepositoryTest {

    private ClientRepository repository;
    private Client client;

    @BeforeEach
    void setUp() {
        repository = new ClientRepositoryImpl(new JpaTransactionManager("h2"));
        client = Client.builder()
                .name("abcd")
                .username("abcd")
                .password("abcd")
                .cpf("12112122192")
                .orders(Set.of())
                .build();
    }

    @Nested
    @DisplayName("** FindUsername client tests **")
    class FindUsernameTests {

        @Test
        void givenFindUsername_whenUsernameFound_thenReturnOptionalOfUsername() {
            repository.save(client);
            assertEquals(Optional.of(client.getUsername()), repository.findUsername(client.getUsername()));
        }

        @Test
        void givenFindUsername_whenUsernameNotFound_thenReturnOptionalEmpty() {
            assertEquals(Optional.empty(), repository.findUsername(client.getUsername()));
        }
    }


    @Nested
    @DisplayName("** FindClient tests **")
    class FindClientTests {

        @Test
        void givenFindClient_whenClientFound_thenReturnOptionalOfClient() {
            repository.save(client);
            assertEquals(Optional.of(client), repository.findClient(client.getUsername(), client.getPassword()));
        }

        @Test
        void givenFindClient_whenClientNotFound_thenReturnOptionalEmpty() {
            assertEquals(Optional.empty(), repository.findClient(client.getUsername(), client.getPassword()));
        }

        @Test
        void givenFindClient_whenClientIsFindByUsernameWithDifferentCaseButNotFound_thenReturnOptionalEmpty() {

            String username = client.getUsername().toLowerCase().substring(0, 1)
                    .concat(
                            client.getUsername().toUpperCase().substring(1)
                    );

            assertEquals(Optional.empty(), repository.findClient(username, client.getPassword()));
        }


    }

}