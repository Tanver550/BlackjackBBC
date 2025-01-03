package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private int score;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    /**
     * Adds a card to the player's hand without prompting.
     * By default, we pick 11 for an Ace if it won't bust, otherwise 1.
     * This is just a placeholder.
     * Later, the user can be prompted to reassign Ace values during their turn.
     */
    public void addCard(Card card) {
        hand.add(card);
        if (card.getRank().equals("Ace")) {
            if (score + 11 <= 21) {
                card.setValue(11);
                score += 11;
            } else {
                card.setValue(1);
                score += 1;
            }
        } else {
            score += card.getValue();
        }
    }

    /**
     * Allows adjusting an Ace's value manually after the card is already in the hand.
     * (Used when prompting the user during their turn.)
     */
    public void adjustAceValue(Card card, int newValue) {
        if (card.getRank().equals("Ace")) {
            // remove old value, set new, then add
            score -= card.getValue();
            card.setValue(newValue);
            score += card.getValue();
        }
    }

    public int getScore() {
        return score;
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand + " | Score: " + score;
    }
}
