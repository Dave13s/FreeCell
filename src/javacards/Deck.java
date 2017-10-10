package javacards;

import java.util.Random;

public class Deck {

    private Card[] cards;
    int i;

    public Deck() {
        i = 51;
        cards = new Card[52];
        int x = 0;
        for (int a = 0; a <= 3; a++) {
            for (int b = 0; b <= 12; b++) {
                cards[x] = new Card(a, b);
                x++;
            }
        }
    }

    public Card drawFromDeck() {
        if (i > 0) {
            Random generator = new Random();
            int index = 0;
            index = generator.nextInt(i);

            Card temp = cards[index];
            cards[index] = cards[i];
            cards[i] = null;
            i--;
            return temp;
        } else {
            return cards[0];
        }
    }
}
