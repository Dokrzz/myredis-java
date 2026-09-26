package enums;

import java.util.Optional;

public enum Option {

    PX ("PX"),
    EX ("EX");

    private final String value;

    Option(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<Option> fromValue(String value) {
        for (Option option : values()) {
            if (option.value.equals(value)) {
                return Optional.of(option);
            }
        }

        return Optional.empty();
    }
}
