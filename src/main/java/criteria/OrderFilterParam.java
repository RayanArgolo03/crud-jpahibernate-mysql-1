package criteria;

import enums.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class OrderFilterParam {

    LocalDate orderDate;
    BigDecimal totalPrice;
    String productName;
    Category category;

}
