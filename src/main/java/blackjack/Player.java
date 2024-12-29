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

    public void addCard(Card card) {
        hand.add(card);
        score += card.getValue();
        if (score > 21) { //this is for ace adjustment, to suit the player depending on their current hand
            for (Card c : hand) {
                if (c.getRank().equals("Ace") && score > 21) {
                    score -= 10;
                }
            }
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
