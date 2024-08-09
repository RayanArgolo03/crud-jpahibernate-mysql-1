package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import model.order.Product;

import java.util.List;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ReaderUtils {

    private static final Scanner SCANNER_MOCKED = new Scanner(System.in);

    public static <E extends Enum<E>> E readEnum(final Class<E> enumClass) {

        final E[] enums = enumClass.getEnumConstants();
        for (int i = 0; i < enums.length; i++) System.out.printf("%d - %s\n", i, enums[i]);

        int option = Integer.parseInt(readMockedString("your choice"));
        System.out.println();

        return enums[option];
    }

    public static Product readProduct(final List<Product> products) {
        for (int i = 0; i < products.size(); i++) System.out.printf("%d - %s\n", i, products.get(i));
        return products.get(Integer.parseInt(readMockedString("product to buy")));
    }

    public static String readMockedString(final String title) {
        System.out.printf("Enter with %s: ", title);
        return SCANNER_MOCKED.next();
    }

    public static String readSimpleString(final String title) {
        System.out.printf("Enter with %s: ", title);
        return new Scanner(System.in).next();
    }

}
