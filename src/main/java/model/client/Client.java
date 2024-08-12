package model.client;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import model.order.Order;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@ToString

@Entity
@DynamicInsert
@Table(name = "clients")
public final class Client {

    @GeneratedValue
    @Id
    @Column(name = "client_id")
    UUID id;

    @Column(name = "user_name", unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String password;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(11)")
    String cpf;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "client")
    Set<Order> orders;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


