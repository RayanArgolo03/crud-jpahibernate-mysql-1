package domain.order;

import enums.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.UUID;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@EqualsAndHashCode
public final class Product {

    @NonFinal
    UUID id;
    String name;
    BigDecimal unitPrice;
    Category category;

    @Override
    public String toString() {
        return String.format("%s with unit value %s of %s category", name,
                NumberFormat.getCurrencyInstance().format(unitPrice), category.getFormattedName());
    }
}
