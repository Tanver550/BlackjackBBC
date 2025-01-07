package blackjack;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private int value;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;

        // use the rank default value
        this.value = rank.getDefaultValue();
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // allow chnging card value if its ace
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
