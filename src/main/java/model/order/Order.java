package model.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import model.client.Client;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import utils.FormatterUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode

@Entity
@DynamicInsert
@Table(name = "orders")
public final class Order {

    @GeneratedValue
    @Id
    @Column(name = "order_id")
    UUID id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<OrderItem> orderItems;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    //Deffensive programing
    public Set<OrderItem> getOrderItems() {
        return Collections.unmodifiableSet(orderItems);
    }

    public String getFormattedDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getTotal() {

        final BigDecimal total = orderItems.stream()
                .map(i -> i.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return FormatterUtils.formatCurrency(total);
    }

    @Override
    public String toString() {
        return "Order items quantity: " + getOrderItems().size();
    }
}
