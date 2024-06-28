package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ReaderUtils {
    static Scanner sc = new Scanner(System.in);

    public static <E extends Enum<E>> E readEnum(Class<E> enumClass) {

        E[] enums = enumClass.getEnumConstants();
        for (E e : enums) System.out.printf("%d - %s\n", (e.ordinal() + 1), e);

        System.out.print("Your choice: ");
        final int choice = readInt();

        return Arrays.stream(enums)
                .filter(e -> (e.ordinal() + 1) == choice)
                .findFirst()
                .orElseThrow(InputMismatchException::new);
    }

    public static int readInt() {
        return sc.nextInt();
    }

    public static String readString(final String title) {
        System.out.printf("Enter with %s: ", title);
        return sc.next();
    }

}
