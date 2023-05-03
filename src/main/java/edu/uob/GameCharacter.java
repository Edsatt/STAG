package edu.uob;

public abstract class GameCharacter extends GameEntity{
    private Boolean alive;

    public void setAliveStatus(Boolean alive){
        this.alive = alive;
    }

    public Boolean isAlive(){
        return alive;
    }
}
