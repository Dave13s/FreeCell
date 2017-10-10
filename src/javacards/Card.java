package javacards;

public class Card {

    private int rank, suit;

    public static String[] suits = {"hearts", "spades", "diamonds", "clubs"};
    public static String[] ranks = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};

    Card(int suit, int rank) {
        this.rank = rank;
        this.suit = suit;
    }

    public @Override
    String toString() {
        return ranks[rank] + " of " + suits[suit];
    }

    public String getRank() {
        return ranks[rank];
    }

    public String getSuit() {
        return suits[suit];
    }
}
