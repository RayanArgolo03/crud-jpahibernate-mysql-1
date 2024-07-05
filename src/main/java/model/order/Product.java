package model.order;

import enums.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.hibernate.annotations.CreationTimestamp;
import utils.FormatterUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@EqualsAndHashCode

@Entity
@Table(name = "products")
public final class Product {

    @NonFinal
    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "name", columnDefinition = "VARCHAR(50) UNIQUE")
    String name;

    @Column(name = "unit_price", columnDefinition = "DECIMAL(15,2) DEFAULT 10.00")
    BigDecimal unitPrice;

    @Enumerated(value = EnumType.STRING)
    Category category;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Override
    public String toString() {
        return String.format("%s with unit value %s of %s category", name,
                FormatterUtils.formatCurrency(unitPrice),
                Objects.requireNonNull(category.getFormattedName(), "Category name can´t be null!")
        );
    }
}
