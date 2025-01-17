package blackjack;

import java.util.ArrayList;
import java.util.List;

public class BlackjackGame {
    private final Deck deck;
    private final List<Card> playerHand;
    private final List<Card> dealerHand;
    private boolean gameOver;
    private boolean revealDealerCard;

    // basically sets up the game to start
    public BlackjackGame() {
        deck = new Deck();
        deck.shuffle();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        gameOver = false;
        revealDealerCard = false;

        // Initial deal to start the game
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard()); // Face-up card
        playerHand.add(deck.drawCard());
        dealerHand.add(deck.drawCard()); // Face-down card
    }
    // sends out the cards in the players hand
    public List<Card> getPlayerHand() {
        return playerHand;
    }
    // sends out the cards in the dealers hand
    public List<Card> getDealerHand() {
        return dealerHand;
    }
    // boolean that indicates weather the dealers second card should be shown
    public boolean isRevealDealerCard() {
        return revealDealerCard;
    }
    // if the game is not over it adds a card to the players hand and then checks if the player loses by exceeding a score of 21
    public void hit() {
        if (!gameOver) {
            playerHand.add(deck.drawCard());
            if (calculateScore(playerHand) > 21) {
                gameOver = true;
            }
        }
    }
    //if the player stands game will be over and they will win if there total is greater than the dealers total after the stop drawing once they get to 17
    public void stand() {
        gameOver = true;
        revealDealerCard = true; // Reveal the dealer's face-down card
        while (calculateScore(dealerHand) < 17) {
            dealerHand.add(deck.drawCard());
        }
    }
    // calculates the score to see the state of the game. it also accounts for aces being high or low and prevents the player from busting with a high ace
    public int calculateScore(List<Card> hand) {
        int score = 0;
        int aceCount = 0;
        //score tally up, aces low
        for (Card card : hand) {
            score += card.getValue();
            if ("Ace".equals(card.getRank())) {
                aceCount++;
            }
        }

        while (score > 21 && aceCount > 0) {
            score -= 10; // Convert Ace from 11 to 1
            aceCount--;
        }

        return score;
    }





    public boolean isGameOver() {
        return gameOver;
    }
}
