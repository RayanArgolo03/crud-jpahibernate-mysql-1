package dtos.output;

import model.order.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderOutputDTO {

    String clientName;
    String dateFormatted;
    Set<OrderItem> items;
    String total;

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(" --> Order made by " + clientName);

        sb.append("\n          ------ ").append(dateFormatted).append("  ------ ");
        sb.append("\n");

        items.forEach(i -> sb.append(i).append("\n"));
        sb.append("              Order Total ").append(total).append("\n");

        return sb.toString();
    }


}
