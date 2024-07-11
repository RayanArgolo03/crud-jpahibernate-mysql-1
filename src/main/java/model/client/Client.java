package model.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter

@DynamicInsert
@Entity
@Table(name = "clients")
public final class Client {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(name = "id", columnDefinition = "binary(16)")
    UUID id;

    @Column(name = "user_name", columnDefinition = "varchar(250) unique")
    String username;

    @Column(name = "client_name")
    String name;

    @Column(name = "password")
    String password;

    @Column(name = "cpf", columnDefinition = "varchar(11) unique")
    String cpf;

    @Column(name = "created_at", columnDefinition = "timestamp default NOW()")
    LocalDateTime createdAt;
}

