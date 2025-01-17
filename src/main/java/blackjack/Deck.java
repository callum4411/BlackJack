package blackjack;

import blackjack.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;
    //this is how cards are randomly chosen. the "deck" is shuffled randomly to output random cards
    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                String imagePath = "/cards/" + rank.toLowerCase() + "_of_" + suit + ".png";
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
    //this is important so there are no duplicates
    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null; // Handle empty deck scenario
    }
}
