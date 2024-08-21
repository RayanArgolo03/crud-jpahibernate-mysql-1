package utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import model.order.OrderItem;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Set;

import static java.lang.String.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatterUtils {

    private final static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private final static String ORDER_ITEM_FORMAT = "%s - %d units purchased - Total %s";

    @Getter
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static String formatCurrency(final BigDecimal value) {
        return CURRENCY_FORMATTER.format(value);
    }

    public static String formatDate(final LocalDate date) {
        return DATE_FORMATTER.format(date);
    }

    public static String formatOrderItems(final Set<OrderItem> items) {

        final StringBuilder sb = new StringBuilder();

        for (OrderItem item : items) {
            sb.append(
                    format(ORDER_ITEM_FORMAT,
                            item.getProduct(),
                            item.getQuantity(),
                            item.getTotal())).append("\n");
        }

        return sb.toString();
    }

}
