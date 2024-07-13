package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatterUtils {
    private final static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String formatCurrency(final BigDecimal value) {return CURRENCY_FORMATTER.format(value);}

    public static String formatDate(final LocalDateTime dateTime){
        return DATE_TIME_FORMATTER.format(dateTime);
    }
}
