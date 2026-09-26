package resp;

import enums.Option;

import java.util.Map;
import java.util.Optional;

public interface IParser {

    public abstract String[] getRequiredArguments(String[] tokens);
    public abstract Map<Option, Optional<String>> getOptionalArguments(String[] token);
}
