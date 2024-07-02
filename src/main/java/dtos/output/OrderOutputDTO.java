package dtos.output;

import domain.order.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderOutputDTO {

    String dateFormatted;
    Set<OrderItem> items;
    String total;

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("  ------ " + dateFormatted + "  ------ ");
        sb.append("\n");

        items.forEach(i -> sb.append(i).append("\n"));
        sb.append("  ------ Total ").append(total).append("  ------ ");

        return sb.toString();
    }


}
