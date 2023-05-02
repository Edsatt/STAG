package edu.uob;

import java.util.ArrayList;
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
        this.subjects = subjects.stream().toList();
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
