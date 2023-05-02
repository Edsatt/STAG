package edu.uob;

import java.util.ArrayList;

public class ActionCommand extends GameCommand{
    private final ArrayList<String> triggerList;
    private ArrayList<String> subjectList;
    private String trigger;

    public ActionCommand(ArrayList<String> commandWords, Map map){
        super(commandWords, map);
        this.triggerList = map.getTriggersList();
    }

    public String handleActionCommand(){
        String response = "";
        if(!checkActionCommand()){
            response = "Error: Command must contain one trigger phrase and at least one subject";
        }
        return response;
    }

    //checks that command contains one trigger and at least one subject
    public boolean checkActionCommand(){
        return(checkTriggers() && checkSubjectList());
    }

    public boolean checkTriggers(){
        int count=0;
        StringBuilder sb = new StringBuilder();
        for(String trigger: triggerList){
            sb.append(trigger).append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        String regex = "\\b("+sb+")\\b";

        for(String word: commandWords){
            if(word.matches(regex)){
                count++;
                this.trigger = word;
            }
        }
        return count==1;
    }

    public boolean checkSubjectList(){
        return subjectList.size()>=1;
    }

    public void setSubjectList(ArrayList<String> subjectList){
        this.subjectList = subjectList;
    }


    //for testing
    public String getTrigger(){
        return trigger;
    }

    public ArrayList<String> getSubjectList(){
        return subjectList;
    }





}
