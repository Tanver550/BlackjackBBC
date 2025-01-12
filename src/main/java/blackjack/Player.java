package blackjack;
/**
 represents a player, each player has a hand and HitStrategy
 keeps track of the player score, add cards to the hand and manage a human player
 */
public class Player {
    private final String name;
    private final Hand hand;
    private HitStrategy hitStrategy;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    public void setHitStrategy(HitStrategy strategy) {
        this.hitStrategy = strategy;
    }

    // player acts as a facade, when the game wants to add a card to a player, it calls player add card
    // can have multiple hands in the future
    public void addCard(Card card) {
        hand.addCard(card);
    }

    public int getScore() {
        return hand.calculateScore();
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    /**
     * Decide if the player should hit using their HitStrategy.
     */
    public boolean shouldHit(Dealer dealer) {
        if (hitStrategy == null) {
            // If theres no strategy set default to stand
            return false;
        }
        return hitStrategy.shouldHit(this, dealer);
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand + " | Score: " + getScore();
    }
}
