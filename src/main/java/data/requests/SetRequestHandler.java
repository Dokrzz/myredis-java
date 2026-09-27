package data.requests;

import data.CacheStore;
import enums.Option;
import resp.IParser;

import java.util.*;

import static enums.DataType.SIMPLE_STRING;
import static enums.DataType.TERMINATE;

public class SetRequestHandler implements IRequestHandler, IParser {

    private Option expiryType;
    private long expirtyAmount;
    int expiryInMilliSeconds = 0;
    String key;
    String value;

    List<String> requiredArguments;
    HashMap<Option, List<String>> options = new HashMap<>();


    @Override
    public String handle(RedisRequest redisRequest) {
        List<String> elements = redisRequest.getElements();

        if(elements.size() < 2) {
            throw new IllegalStateException();
        }

        parse(elements);

        if(this.options.containsKey(Option.EX)) {
            List<String> args = this.options.get(Option.EX);
            int expiryInSeconds = Integer.parseInt(args.getFirst());
            this.expiryInMilliSeconds = expiryInSeconds *1000;
        }

        else if(this.options.containsKey(Option.PX)) {
            List<String> args = this.options.get(Option.PX);
            this.expiryInMilliSeconds = Integer.parseInt(args.getFirst());
        }

        CacheStore.put(this.key, this.value, expiryInMilliSeconds);

        System.out.println("Expiry is : " + expiryInMilliSeconds);

        String sb = SIMPLE_STRING.getValue() + "OK" + TERMINATE.getValue();

        return sb;
    }

    private void setExpiryInMilliSeconds(int expiryInMilliSeconds) {

    }

    private void parse(List<String> elements) {

        Option currentOption = null;

        for(int i = 0; i < elements.size(); i++) {

            String currentToken = elements.get(i);
            switch(i) {
                case 0 -> this.key = currentToken;
                case 1 -> this.value = currentToken;

                default -> {


                    boolean currentTokenIsOption = Option.fromValue(currentToken).isPresent();

                    if(!currentTokenIsOption) {

                        if(currentOption == null) {
                            throw new IllegalStateException();
                        }

                        if(!this.options.isEmpty()) {
                            mapArgumentToOption(currentOption, currentToken);
                        }
                    }

                    else {
                        Optional<Option> optionFromToken = Option.fromValue(currentToken);

                        if(optionFromToken.isPresent()) {
                            currentOption = optionFromToken.get();
                            this.options.put(currentOption, new ArrayList<>());
                        }
                    }

                }

            }

        }
    }


    private void mapArgumentToOption(Option option, String currentToken) {
        boolean argumentExists = this.options.containsKey(option);

        if(argumentExists) {
            List<String> argValues = this.options.get(option);
            argValues.add(currentToken);
        }
    }

    @Override
    public String[] getRequiredArguments(String[] tokens) {
        return new String[0];
    }

    @Override
    public Map<Option, Optional<String>> getOptionalArguments(String[] token) {
        return Map.of();
    }

}
