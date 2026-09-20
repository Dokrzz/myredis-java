package resp;

import data.RedisRequest;

public class Parser {
    private static final String terminator = "\r\n";

    public static RedisRequest parse(String request) {
        String[] tokens = request.split(terminator);

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

                if(i == 1) {
                    Command command = Command.valueOf(tokens[i]);

                    if(command.getValue() != null)
                        redisRequest.setCommand(command);
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
