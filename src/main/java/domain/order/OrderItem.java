package domain.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import utils.FormatterUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public final class OrderItem {

    UUID id;
    Product product;
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
