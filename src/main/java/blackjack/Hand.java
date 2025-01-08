package blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * A Hand represents the cards a player or dealer is holding
 */
public class Hand {
    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Calculates the best Blackjack score for this hand
     * treating Aces as 11 initially then reducing them to 1 if needed.
     */
    public int calculateScore() {
        int total = 0;
        int aceCount = 0;

        // first treat all Aces as 11
        for (Card card : cards) {
            total += card.getValue();
            if (card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        // While over 21 and we have an ace counted as 11 drop it to 1
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
