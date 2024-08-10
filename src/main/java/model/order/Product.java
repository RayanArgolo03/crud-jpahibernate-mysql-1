package model.order;

import enums.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
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

    String name;

    BigDecimal unitPrice;

    Set<Category> categories;

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
