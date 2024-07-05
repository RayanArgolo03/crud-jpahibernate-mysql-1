package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    FOODS("Foods"), ELETRONICS("Eletronics");
    private final String formattedName;
}
