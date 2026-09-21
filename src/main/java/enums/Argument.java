package enums;

import java.util.Optional;

public enum Argument {

    PX ("PX"),
    EX ("EX");

    private final String value;

    Argument(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<Argument> fromValue(String value) {
        for (Argument argument : values()) {
            if (argument.value.equals(value)) {
                return Optional.of(argument);
            }
        }

        return Optional.empty();
    }
}
