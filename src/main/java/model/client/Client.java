package model.client;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@ToString
@EqualsAndHashCode

@Entity
@DynamicInsert
@Table(name = "clients")
public final class Client {

    @Id
    @GeneratedValue
    UUID id;


    String username;


    String name;

    String password;


    String cpf;


    LocalDateTime createdAt;
}


