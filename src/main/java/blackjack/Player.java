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
     * Adds a card to the player hand.
     * If its an Ace by Rank check to treat it as 11 without busting
     */
    public void addCard(Card card) {
        hand.add(card);

        if (card.getRank() == Rank.ACE) {
            // If 11 wont bust we pick 11 else 1
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
     * Adjust an Ace value from 1 to 11 or 11 to 1 if needed
     */
    public void adjustAceValue(Card card, int newValue) {
        if (card.getRank() == Rank.ACE) {
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
