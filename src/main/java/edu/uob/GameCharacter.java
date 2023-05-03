package edu.uob;

public abstract class GameCharacter extends GameEntity{

    private String description;
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
