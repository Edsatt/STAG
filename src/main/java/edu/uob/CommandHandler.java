package edu.uob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler {
    private Map map;
    private final String command;
    private List<String> subjectList;
    private final List<String> commandWords;
    private BasicCommand basicCommand;
    private ActionCommand actionCommand;

    public CommandHandler(String input) {
        this.command = input.toLowerCase();
        this.commandWords = new ArrayList<>();
    }

    public String handleCommand(Map map) {
        this.map = map;
        this.subjectList = new ArrayList<>();
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
        String response;
        if(subjectCount()<=1) {
            response = basicCommandCheck();
        }else{
            response = handleActionCommand();
        }
        return response;
    }

    public int subjectCount(){
        int count = 0;
        StringBuilder subjects = new StringBuilder();
        for(String subject: map.getSubjectsList()){
            subjects.append(subject).append("|");
        }
        subjects.deleteCharAt(subjects.length()-1);
        String regex = "\\b("+subjects+")\\b";

        for(String word: commandWords){
            if(word.matches(regex)){
                count ++;
                subjectList.add(word);
            }
        }
        return count;
    }

    public ArrayList<String> getSubjectList(){
        return (ArrayList<String>)subjectList;
    }

    private String basicCommandCheck() {
        String response = "";
        switch(basicCommandCount()){
            case 0 -> response = handleActionCommand();
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
        basicCommand = new BasicCommand((ArrayList<String>) commandWords, map);
        if(!subjectList.isEmpty()){
            basicCommand.setSubject(subjectList.get(0));
        }
        return basicCommand.handleBasicCommand();
    }

    private String handleActionCommand(){
        actionCommand = new ActionCommand((ArrayList<String>)commandWords, map);
        actionCommand.setSubjectList((ArrayList<String>)subjectList);
        return actionCommand.handleActionCommand();
    }

    public BasicCommand getBasicCommand(){
        return basicCommand;
    }

    public ActionCommand getActionCommand() {
        return actionCommand;
    }
}
