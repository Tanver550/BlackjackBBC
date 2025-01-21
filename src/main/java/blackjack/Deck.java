package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        this(1); // use only 1 deck when creating a deck, used for small games and testing
    }

    /**
     * New constructor for multiple decks, a shoe
     * e.g. new Deck(6) => 6 * 52 = 312 cards total
     */
    public Deck(int numberOfDecks) {
        cards = new ArrayList<>();
        for (int i = 0; i < numberOfDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
        shuffle();
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

    public int cardsLeft() {
        return cards.size();
    }

    public int getSize(){return cards.size();}
}
