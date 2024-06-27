package domain.order;

import enums.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.*;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Product {
    @NonFinal
    UUID id;
     String name;
     BigDecimal unitPrice;
    Category category;
}
