package model.order;

import lombok.*;
import model.client.Client;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.hibernate.annotations.CreationTimestamp;
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

    @NonFinal
    @Id
    @GeneratedValue
    @Column(name = "id")
    UUID id;

    //Todo relacione
    Client client;
    Set<OrderItem> orderItems;
    //Deffensive programing

    @CreationTimestamp
    @Column(name = "order_date")
    LocalDateTime orderDate;

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
