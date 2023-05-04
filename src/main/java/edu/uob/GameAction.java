package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameAction
{
    private List<String> subjects;
    private String consumed;
    private String produced;
    private String narration;
    public GameAction(){
    }

    public void setSubjects(String[] subjectArray) {
        subjects = new ArrayList<>();
        subjects.addAll(Arrays.stream(subjectArray).toList());
    }

    public boolean checkSubjects(ArrayList<String> querySubjects){
        for(String subject: querySubjects){
            if (!subjects.contains(subject)){
                return false;
            }
        }
        return true;
    }

    public void setConsumed(String consumed) {
        this.consumed = consumed;
    }

    public void setProduced(String produced) {
        this.produced = produced;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getConsumed() {
        return consumed;
    }

    public String getProduced() {
        return produced;
    }

    public String getNarration() {
        return narration;
    }
}
