package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.NumberFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatterCurrencyUtils {
    private final static NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
    public static String formatCurrency(final BigDecimal value) {
        return CURRENCY_FORMATTER.format(value);
    }

}
