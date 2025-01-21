package blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Hand> hands;
    private HitStrategy hitStrategy;

    // Simple counters for scoreboard
    private int wins;
    private int losses;
    private int pushes;

    public Player(String name) {
        this.name = name;
        this.hands = new ArrayList<>();
        // Start with one empty hand
        this.hands.add(new Hand());
        this.wins = 0;
        this.losses = 0;
        this.pushes = 0;
    }

    public String getName() {
        return name;
    }

    public HitStrategy getHitStrategy() {
        return hitStrategy;
    }

    public void setHitStrategy(HitStrategy strategy) {
        this.hitStrategy = strategy;
    }

    /** Access the list of all hands for this player. */
    public List<Hand> getHands() {
        return hands;
    }

    /**
     *  add a card to the first hand
     */
    public void addCard(Card card) {
        hands.get(0).addCard(card);
    }

    public int getScore() {
        return hands.get(0).calculateScore();
    }

    // assign the user hit strategy as its a player. this gives the option to hit or stand
    public boolean shouldHit(Dealer dealer) {
        if (hitStrategy == null) {
            return false;
        }
        return hitStrategy.shouldHit(this, dealer);
    }

    // getters and setters for scoreboard
    public void recordWin() { wins++; }
    public void recordLoss() { losses++; }
    public void recordPush() { pushes++; }

    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getPushes() { return pushes; }

    @Override
    public String toString() {
        // Print each hand
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("'s Hands:\n");
        for (int i = 0; i < hands.size(); i++) {
            Hand h = hands.get(i);
            sb.append("  Hand ").append(i+1).append(": ")
                    .append(h).append(" (Score=")
                    .append(h.calculateScore()).append(")\n");
        }
        return sb.toString();
    }

}
