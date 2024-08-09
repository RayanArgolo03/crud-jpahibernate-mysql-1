package model.order;

import enums.Category;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import utils.FormatterUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@EqualsAndHashCode

@Entity
@DynamicInsert
@Table(name = "products")
public final class Product {

    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "name", unique = true, nullable = false)
    String name;

    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "categories",
            joinColumns = @JoinColumn(name = "id"),
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE")
    )
    @Enumerated(value = EnumType.STRING)
    @Column(name = "name", nullable = false)
    Set<Category> categories;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    private String getFirstCategoryFormattedName() {
        return categories.stream().findFirst().get().getFormattedName();
    }

    @Override
    public String toString() {
        return String.format("%s with unit price %s of %s category", name,
                FormatterUtils.formatCurrency(unitPrice),
                Objects.requireNonNull(getFirstCategoryFormattedName(), "Category name can´t be null!")
        );
    }
}
