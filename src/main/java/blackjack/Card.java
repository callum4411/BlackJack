package blackjack;

public class Card {
    private final String rank;
    private final String suit;

    //cards have 2 atributes these being rank and suit. both are used to find image path
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;

    }

    public String getRank() {
        return rank;
    }


    public int getValue() {
        switch (rank) {
            case "Ace":
                return 11; // Adjust dynamically in game logic for soft/hard hands
            case "King":
            case "Queen":
            case "Jack":
                return 10;
            default:
                return Integer.parseInt(rank);
        }
    }
    //this gets the image name based off of the suit and rank
    public String getImageName() {
        return rank.toLowerCase() + "_of_" + suit.toLowerCase() + ".png";
    }
}
