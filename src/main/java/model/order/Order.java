package model.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import org.hibernate.annotations.DynamicInsert;
import utils.FormatterUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter

@DynamicInsert
@Entity
@Table(name = "orders")
public final class Order {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(name = "id", columnDefinition = "binary(16)")
    UUID id;

    @ManyToOne
    @JoinColumn(name = "id_client")
    Client client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_order")
    Set<OrderItem> orderItems;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    LocalDateTime orderDate;

    //Deffensive programing

    public Set<OrderItem> getOrderItems() {
        return Collections.unmodifiableSet(orderItems);
    }

    public String getFormattedDate() {
        return FormatterUtils.formatDate(orderDate);
    }

    public String getTotal() {

        final BigDecimal total = orderItems.stream()
                .map(i -> i.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return FormatterUtils.formatCurrency(total);
    }
}
