package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    ELETRONICS("Eletronics"), FOODS("Foods");
    private final String formattedName;
}
