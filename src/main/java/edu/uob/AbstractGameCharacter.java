package edu.uob;

public abstract class AbstractGameCharacter extends GameEntity{
    private String description;
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
