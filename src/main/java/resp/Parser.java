package resp;

import java.util.*;

public class Parser {
    private static final String terminator = "\r\n";

    public static HashMap<Command, List<String>> parse(String request) {
        String[] tokens = Arrays.stream(request.split(terminator)).iterator();

        validate(tokens);

        HashMap<Command, List<String>> commandToArgs = new HashMap<>();

        String commandKey = "";
        List<String> commandArgs = new ArrayList<>();

        int firstArgumentIndex = 2;
        for(int i = firstArgumentIndex; i < tokens.length; i++) {
            String text = tokens[i];
            if(i == firstArgumentIndex) {
                commandKey = text.toUpperCase();
                System.out.println(commandKey);
            }

            else {
                if(text.charAt(0) == '$')
                    continue;

                commandArgs.add(tokens[i]);
            }
        }

        if(!commandKey.isEmpty()) {
            Optional<Command> command = Command.fromValue(commandKey);

            command.ifPresent(key -> commandToArgs.put(key, commandArgs));
        }

        return commandToArgs;

    }

    private static void validate(String[] tokens) {
        if (tokens.length < 1) {
            throw new IllegalArgumentException();
        }
    }
}
