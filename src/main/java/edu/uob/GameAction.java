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
        subjects = new ArrayList<>();
    }

    public void setSubjects(String[] subjectArray) {
        this.subjects = Arrays.stream(subjectArray).toList();
    }

    public boolean checkSubjects(ArrayList<String> querySubjects){
        StringBuilder sb = new StringBuilder();
        for(String s: this.subjects){
            sb.append(s).append("|");
        }
        sb.deleteCharAt(sb.length()-1);
        String regex = "\\b("+sb+")\\b";

        for(String subject: querySubjects){
            if (!subject.matches(regex)) {
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



    public List<String> getSubjects() {
        return subjects;
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
