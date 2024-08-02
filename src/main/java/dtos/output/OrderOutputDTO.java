package dtos.output;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class OrderOutputDTO {

    String clientName;
    String dateFormatted;
    String itemsFormatted;
    String total;

    @Override
    public String toString() {
        return " --> Order made by " + clientName + "\n          ------ " + dateFormatted + "  ------ " +
               "\n" +
               itemsFormatted +
               "             -> Order Total " + total + "\n";
    }

}
