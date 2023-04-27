package edu.uob;

public class Token {
    TokenType tokenType;
    String tokenValue;

    public Token(String inputString){
        setTokenValue(inputString);
    }

    private void setTokenValue(String inputString){
        this.tokenValue = inputString.toUpperCase();
        this.tokenType = setTokenType();
    }

    private TokenType setTokenType(){
        switch (this.tokenValue){
            case "INVENTORY", "INV" -> {
                return TokenType.INVENTORY;
            }
            case "GET" -> {
                return TokenType.GET;
            }
            case "DROP" -> {
                return TokenType.DROP;
            }
            case "GOTO" -> {
                return TokenType.GOTO;
            }
            case "LOOK" -> {
                return TokenType.LOOK;
            }
        }
        return TokenType.NULL;
    }

    public String getTokenValue(){
        return this.tokenValue;
    }

    public TokenType getTokenType() {
        return tokenType;
    }
}
