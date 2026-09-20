package resp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Parser {
    private static final String terminator = "\r\n";

    public static HashMap<Command, List<String>> parse(String request) {
        String[] tokens = request.split(terminator);

        boolean isValid = validate(tokens);
        if(!isValid) {
            throw new IllegalArgumentException();
        }

        HashMap<Command, List<String>> commandToArgs = new HashMap<>();

        String commandKey = "";
        List<String> commandArgs = new ArrayList<>();

        for(int i = 2; i < tokens.length; i++) {
            String text = tokens[i];
            if(i == 2) {
                commandKey = text.toUpperCase();
                System.out.println(commandKey);
            }

            else {
                if(text.charAt(0) == '$')
                    continue;

                commandArgs.add(tokens[i]);
            }
        }

        if(!commandKey.isEmpty() && !commandArgs.isEmpty()) {
            Optional<Command> command = Command.fromValue(commandKey);

            command.ifPresent(key -> commandToArgs.put(key, commandArgs));
        }

        return commandToArgs;

    }

    private static boolean validate(String[] tokens) {
        if(tokens.length < 1) {
            return false;
        }

        return true;
    }
}
