package utils;

import enums.Option;
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

    public static Option readOption() {

        for (Option o : Option.values()) System.out.printf("%d - %s\n", o.getId(), o);
        System.out.print("Your choice: ");

        final int choice = readInt();
        return Arrays.stream(Option.values())
                .filter(o -> o.getId() == choice)
                .findFirst()
                .orElseThrow(InputMismatchException::new);
    }

    public static int readInt() {
        return sc.nextInt();
    }

    public static String readString(final String title) {
        System.out.printf("Enter with %s:", title);
        return sc.next();
    }

}
