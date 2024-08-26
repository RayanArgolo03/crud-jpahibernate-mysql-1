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
@NamedQueries({@NamedQuery(name = "Order.findByOrderDate", query = """
        SELECT o
        FROM Order o
        WHERE o.client = :client
        AND DATE(o.createdAt) = :orderDate
        """), @NamedQuery(name = "Order.findByTotalPrice", query = """
        SELECT o
        FROM Order o
        JOIN FETCH o.orderItems oi
        JOIN FETCH oi.product p
        WHERE o.client = :client
        GROUP BY o.id, oi.id, p.id
        HAVING SUBSTRING(CAST(SUM(p.unitPrice * oi.quantity) AS STRING), 1, 1) LIKE :total
        """), @NamedQuery(name = "Order.findByCategory", query = """
        SELECT o
        FROM Order o
        JOIN FETCH o.orderItems oi
        JOIN FETCH oi.product p
        JOIN FETCH p.categories c
        WHERE o.client = :client
        AND c = :category
        """)})
public final class Order {

    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client")
    Client client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_order")
    Set<OrderItem> orderItems;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    //Deffensive programing
    public Set<OrderItem> getOrderItems() {
        return Collections.unmodifiableSet(orderItems);
    }

    public String getFormattedDate() {
        return createdAt.format(FormatterUtils.getDATE_FORMATTER());
    }

    public String getTotal() {

        final BigDecimal total = orderItems.stream()
                .map(i -> i.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return FormatterUtils.formatCurrency(total);
    }
}
