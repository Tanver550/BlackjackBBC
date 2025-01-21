package blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple deck that deals from a predefined list of cards,
 * ignoring shuffle, for testing purposes.
 */
public class MockDeck extends Deck {

    private final List<Card> mockCards;

    public MockDeck(List<Card> mockCards) {
        super();
        this.mockCards = new ArrayList<>(mockCards);
    }

    @Override
    public void shuffle() {
        // do nothing, so the order is fixed
    }

    @Override
    public Card dealCard() {
        if (mockCards.isEmpty()) {
            throw new IllegalStateException("MockDeck is out of cards!");
        }
        return mockCards.remove(0);
        // remove from the front, deal in the given order
    }
}
