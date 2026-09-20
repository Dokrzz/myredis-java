package resp;

import dao.RedisRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Parser {
    private static final String terminator = "\r\n";

    public static HashMap<Command, List<String>> parse(String request) {
        String[] tokens = request.split(terminator);

        validate(tokens);

        RedisRequest redisRequest = new RedisRequest();

        String commandKey = "";
        List<String> commandArgs = new ArrayList<>();


        for(int i = 0; i < tokens.length; i++) {
            String text = tokens[i];
            if(i == 0) {
                String[] numberOfElementsString = text.split("*");

                int numberOfElements = Integer.parseInt(numberOfElementsString[1]);
                redisRequest.setNumberOfElements(numberOfElements);
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
