package model.order;

import enums.Category;
import exceptions.ProductException;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import utils.FormatterUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@EqualsAndHashCode

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "products")
public final class Product {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(name = "id", columnDefinition = "binary(16)")
    UUID id;

    @NonFinal
    @Setter
    @Column(name = "name", columnDefinition = "VARCHAR(50) UNIQUE")
    String name;

    @Column(name = "unit_price", columnDefinition = "DECIMAL(15,2) DEFAULT 10.00")
    BigDecimal unitPrice;

    @ElementCollection
    @CollectionTable(
            name = "categories",
            joinColumns = @JoinColumn(name = "id", columnDefinition = "binary(16)"),
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE")
    )
    @Enumerated(value = EnumType.STRING)
    @Column(name = "category")
    Set<Category> categories;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdAt;

    private String getFirstCategoryFormattedName() {
        if (categories.isEmpty()) throw new ProductException(String.format("Without categories in product %s! ", name));
        return categories.stream().findFirst().get().getFormattedName();
    }

    @Override
    public String toString() {
        return String.format("%s with unit value %s of %s category", name,
                FormatterUtils.formatCurrency(unitPrice),
                Objects.requireNonNull(getFirstCategoryFormattedName(), "Category name can´t be null!")
        );
    }
}
