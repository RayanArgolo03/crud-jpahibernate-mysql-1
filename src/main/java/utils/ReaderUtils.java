package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ReaderUtils {
    static Scanner SCANNER = new Scanner(System.in);

    public static <E extends Enum<E>> E readEnum(Class<E> enumClass) {

        final E[] enums = enumClass.getEnumConstants();
        for (E e : enums) System.out.printf("%d - %s\n", (e.ordinal() + 1), e);

        return enums[readInt("your choice") - 1];
    }

    public static <T> T readElement(final List<T> elements) {

        elements.forEach(p -> {
            System.out.printf("%d - %s\n", elements.indexOf(p) + 1, p);
        });

        return elements.get(readInt("Product to buy: ") - 1);
    }

    public static int readInt(final String title) {
        System.out.printf("Enter with %s: ", title);
        return SCANNER.nextInt();
    }

    public static String readString(final String title) {
        System.out.printf("Enter with %s: ", title);
        return SCANNER.next();
    }

}
