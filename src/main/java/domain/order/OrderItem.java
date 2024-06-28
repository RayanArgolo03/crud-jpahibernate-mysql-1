package domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.*;

import java.util.UUID;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class OrderItem {
    @NonFinal
    UUID id;
    Product product;
    int quantity;
}
