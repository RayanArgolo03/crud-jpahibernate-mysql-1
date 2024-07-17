package model.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import utils.FormatterUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode

@Entity
@DynamicInsert
@Table(name = "order_items")
public final class OrderItem {

    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne
    @JoinColumn(name = "id_product")
    final Product product;

    @Column(name = "quantity", columnDefinition = "int default 10", nullable = false)
    int quantity;

    @Override
    public String toString() {
        return String.format("%s - %d units purchased - Total %s", product, quantity, getTotal());
    }

    private String getTotal() {
        return FormatterUtils.formatCurrency(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

}
