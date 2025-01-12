package blackjack;

// represents a single card

public class Card {
    private final Rank rank;
    private final Suit suit;

    // used for the decision if the player wants to make the ace 1 or 11
    private Integer overrideValue;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.overrideValue = null;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        // If its an Ace and we have an override return that
        if (rank == Rank.ACE && overrideValue != null) {
            return overrideValue;
        }
        return rank.getDefaultValue();
    }

    public void setOverrideValue(Integer overrideValue) {
        this.overrideValue = overrideValue;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
