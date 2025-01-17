package blackjack;

import java.io.Serializable;
// this manages who the player playing is. it also handles storing the wins and losses for the start of the game to call onto
public class Player implements Serializable {
    private String name;
    private int wins;
    private int losses;

    public Player(String name) {
        this.name = name;
        this.wins = 0;
        this.losses = 0;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    // Add these setter methods
    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    @Override
    public String toString() {
        return name + " - Wins: " + wins + ", Losses: " + losses;
    }
}
