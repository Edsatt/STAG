package edu.uob;
import java.util.ArrayList;
import java.util.Arrays;

public class GameCommand {
    private Map map;
    private final String command;
    private String subject;
    private final ArrayList<String> commandWords;

    public GameCommand(String input) {
        this.command = input.toLowerCase();
        this.commandWords = new ArrayList<>();
    }

    public String handleCommand(Map map) {
        this.map = map;
        commandWords.addAll(Arrays.asList(command.split(" ")));
        String response = interpretCommand();
        if(response == null) {
            return "Error: Please enter a valid command";
        }else return response;
    }

    private String interpretCommand(){
        return subjectCheck();
    }

    public String subjectCheck(){
        String response = "";
        switch (subjectCount()){
            case 0,1 -> response = basicCommandCheck();
            case 2 -> response = "Error: Command cannot contain more than one subject";
        }
        return response;
    }

    public int subjectCount(){
        int count = 0;
        StringBuilder subjects = new StringBuilder();
        for(String subject: map.getSubjects()){
            subjects.append(subject).append("|");
        }
        subjects.deleteCharAt(subjects.length()-1);
        String regex = "\\b("+subjects+")\\b";

        for(String word: commandWords){
            if(word.matches(regex)){
                count ++;
                setSubject(word);
            }
        }
        return count;
    }

    private void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return subject;
    }

    private String basicCommandCheck() {
        String response = "";
        switch(basicCommandCount()){
            case 0 -> response = null;
            case 1 -> response = handleBasicCommand();
            case 2 -> response = "Input cannot contain more than one command";
        }
        return response;
    }

    public int basicCommandCount() {
        String regex = "\\b(inventory|inv|get|drop|goto|look)\\b";
        int count = 0;
        for (String word : commandWords) {
            if (word.matches(regex)) {
                count++;
            }
        }
        return count;
    }

    private String handleBasicCommand(){
        BasicCommand basicCommand = new BasicCommand(commandWords, map, subject);
        return basicCommand.handleBasicCommand();
    }
}
