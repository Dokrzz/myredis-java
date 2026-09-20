package enums;

import java.util.Optional;

public enum DataType {
    SIMPLE_STRING("+"),
    SIMPLE_ERROR("-"),
    INTEGER(":"),
    BULK_STRING("$"),
    ARRAY("*"),
    NULL_BULK_STRING("$-1\r\n"),
    TERMINATE("\r\n");

    private final String value;

    DataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<DataType> fromValue(String value) {
        for (DataType dataType : values()) {
            if (dataType.value.equals(value)) {
                return Optional.of(dataType);
            }
        }

        return Optional.empty();
    }
}
