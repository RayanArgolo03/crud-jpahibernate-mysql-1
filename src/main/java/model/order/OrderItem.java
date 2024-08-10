package model.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import utils.FormatterUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@ToString

@Entity
@DynamicInsert
@Table(name = "order_items")
public final class OrderItem {

    @Id
    @GeneratedValue
    UUID id;


    final Product product;

    int quantity;

    public String getTotal() {
        return FormatterUtils.formatCurrency(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    public void increaseQuantity(int quantity) {this.quantity += quantity;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(product, orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
