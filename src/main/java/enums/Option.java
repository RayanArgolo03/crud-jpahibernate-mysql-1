package enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public enum Option {

    CREATE_CLIENT(1), LOGIN(2), OUT(3);
    int id;

    Option(int id) {
        this.id = id;
    }
}
