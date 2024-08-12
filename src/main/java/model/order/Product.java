package model.order;

import enums.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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


@Entity
@DynamicInsert
@Table(name = "products")
public final class Product {

    @GeneratedValue
    @Id
    @Column(name = "product_id")
    UUID id;

    @Column(unique = true, nullable = false)
    String name;

    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "categories",
            joinColumns = @JoinColumn(name = "id"),
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (id) REFERENCES products(product_id) ON DELETE CASCADE"))
    @Enumerated(value = EnumType.STRING)
    @Column(name = "category_name")
    Set<Category> categories;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "product")
    Set<OrderItem> orderItems;

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
