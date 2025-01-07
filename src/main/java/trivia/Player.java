package trivia;

import static java.lang.System.out;
import static trivia.Game.*;

public class Player {
    private final String name;
    private int coins = 0;
    private int location = 0;
    private boolean isInPenaltyBox = false;

    public Player(String name) {
        this.name = name;
    }

    boolean didPlayerWin() {
        return coins != WINNING_COINS;
    }

    public void movePlayerToNewLocation(int roll) {
        location = location + roll;
        if (location >= BOARD_SIZE) location = location - BOARD_SIZE;

        out.println(name
                + "'s new location is "
                + location);
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getLocation() {
        return location;
    }
    
    public boolean isNotInPenaltyBox() {
        return !isInPenaltyBox;
    }

    public void setInPenaltyBox(boolean inPenaltyBox) {
        isInPenaltyBox = inPenaltyBox;
    }

    public String getName() {
        return name;
    }
}