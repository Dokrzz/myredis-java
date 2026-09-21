package resp;

import data.requests.RedisRequest;
import enums.Command;

public class Parser {
    private static final String TERMINATOR = "\r\n";

    private Parser() {}

    public static RedisRequest parse(String request) {
        String[] tokens = request.split(TERMINATOR);

        validate(tokens);

        RedisRequest redisRequest = new RedisRequest();

        for(int i = 0; i < tokens.length; i++) {
            String text = tokens[i];
            if(text.charAt(0) == '*') {
                String[] numberOfElementsString = text.split("");

                int numberOfElements = Integer.parseInt(numberOfElementsString[1]);
                redisRequest.setNumberOfElements(numberOfElements);
            }

            else if (text.charAt(0) == '$'){
                continue;
            }

            else {
                if(i == 2) {
                    Command command = Command.valueOf(tokens[i]);

                    if(command != null) {
                        redisRequest.setCommand(command);
                    }

                    else {
                        redisRequest.setCommand(Command.UNKNOWN);
                    }
                }

                else {
                    redisRequest.addElement(text);
                }
            }
        }

        return redisRequest;

    }

    private static void validate(String[] tokens) {
        if (tokens.length < 1) {
            throw new IllegalArgumentException();
        }
    }
}
