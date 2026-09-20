package resp;

import java.util.Optional;

public enum Command {
    PING ("PING"),
    ECHO ("ECHO"),
    GET("GET"),
    SET ("SET"),
    UNKNOWN("UNKNOWN");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<Command> fromValue(String value) {
        for (Command command : values()) {
            if (command.value.equals(value)) {
                return Optional.of(command);
            }
        }

        return Optional.empty();
    }
}
