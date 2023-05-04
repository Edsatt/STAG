package edu.uob;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {
    private Map map;
    private final String command;
    private List<String> commandSubjects;
    private final List<String> commandWords;
    private ActionCommand actionCommand;

    public CommandHandler(String input) {
        this.command = input.toLowerCase();
        this.commandWords = new ArrayList<>();
    }

    public String handleCommand(Map map) {
        this.map = map;
        this.commandSubjects = new ArrayList<>();
        tokenizeCommand();
        return interpretCommand();
    }

    public void tokenizeCommand(){
        for(String basicCommand: map.getCommands()){
            addEntitiesToArray(basicCommand);
        }
        for(String subject: map.getSubjectsList()){
            addEntitiesToArray(subject);
        }
        for(String trigger: map.getTriggersList()){
            addEntitiesToArray(trigger);
        }
    }

    public void addEntitiesToArray(String entity){
        Pattern entityPattern = Pattern.compile("\\b("+entity+")\\b");
        Matcher countEntityMatches = entityPattern.matcher(command);
        int count = 0;
        while(countEntityMatches.find()){
            count++;
        }
        for(int i=0; i<count; i++){
            commandWords.add(entity);
        }
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
        for(String word: commandWords){
            if(map.getSubjectsList().contains(word)){
                count++;
                commandSubjects.add(word);
            }
        }
        return count;
    }

    public List<String> getCommandSubjects(){
        return commandSubjects;
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
        int count = 0;
        for (String word : commandWords) {
            if (word.matches("\\b(inventory|inv|get|drop|goto|look|health)\\b")) {
                count++;
            }
        }
        return count;
    }

    private String handleBasicCommand(){
        BasicCommand basicCommand = new BasicCommand(commandWords, map);
        if(!commandSubjects.isEmpty()){
            basicCommand.setSubject(commandSubjects.get(0));
        }
        return basicCommand.handleBasicCommand();
    }

    private String handleActionCommand(){
        actionCommand = new ActionCommand(commandWords, map);
        actionCommand.setSubjectList(commandSubjects);
        return actionCommand.handleActionCommand();
    }

    public ActionCommand getActionCommand() {
        return actionCommand;
    }

    public List<String> getCommandWords() {
        return commandWords;
    }
}
