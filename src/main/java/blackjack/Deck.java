package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                int value = (i < 9) ? i + 2 : 10; // Number cards 2-10, face cards = 10
                if (ranks[i].equals("Ace")) {
                    value = 11; // Aces default to 11
                }
                cards.add(new Card(ranks[i], suit, value));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty!");
        }
        return cards.remove(cards.size() - 1);
    }

    // Add this method to get the number of cards left in the deck
    public int cardsLeft() {
        return cards.size();
    }
}
