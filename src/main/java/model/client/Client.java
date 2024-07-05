package model.client;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.hibernate.annotations.CreationTimestamp;
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

    @NonFinal
    @Setter
    @Column(name = "id")
    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "user_name", columnDefinition = "VARCHAR(250) UNIQUE")
    String username;

    @Column(name = "client_name")
    String name;

    @Column(name = "password")
    String password;

    @Column(name = "cpf", columnDefinition = "VARCHAR(11) UNIQUE")
    String cpf;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;
}

