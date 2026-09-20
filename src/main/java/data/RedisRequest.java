package data;

import resp.Command;

import java.util.ArrayList;
import java.util.List;

public class RedisRequest {

    private Command command;
    private int numberOfElements;
    private List<String> elements;

    public RedisRequest() {
        this.command = null;
        this.numberOfElements = -1;
        this.elements = new ArrayList<>();
    }

    public RedisRequest(Command command, int numberOfElements, List<String> elements) {
        this.command = command;
        this.numberOfElements = numberOfElements;
        this.elements = elements;
    }

    public boolean isValid() {
        return this.numberOfElements == this.elements.size();
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        System.out.println("Current command: " + command + command.getValue());
        this.command = command;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public void addElement(String element) {
        elements.add(element);
    }

    @Override
    public String toString() {
        return "RedisRequest{" +
                "command=" + command +
                ", numberOfElements=" + numberOfElements +
                ", elements=" + elements +
                '}';
    }
}
