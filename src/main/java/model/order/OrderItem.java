package model.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import utils.FormatterUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode

@DynamicInsert
@Entity
@Table(name = "order_items")
public final class OrderItem {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(name = "id", columnDefinition = "binary(16)")
    UUID id;

    @ManyToOne()
    @JoinColumn(name = "id_product")
    final Product product;

    @Min(value = 1, message = "Quantity min is 1!")
    @Max(value = 10, message = "Quantity max is 10!")
    @Column(name = "quantity")
    int quantity;

    @Override
    public String toString() {
        return String.format("%s - %d units purchased - Total %s", product, quantity, getTotal());
    }

    private String getTotal() {
        return FormatterUtils.formatCurrency(product.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    public void incrementQuantity(int quantity) {
        this.quantity += quantity;
    }
}
