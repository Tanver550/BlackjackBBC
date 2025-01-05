package blackjack;

/**
 * Dealer is a type of Player with distinct auto-hit logic.
 * can still reuse Player's fields/methods for the dealer hand and score.
 */
public class Dealer extends Player {

    public Dealer() {
        super("Dealer");
    }

    /*
     * reuse the same addCard logic that the Player uses.
     * If there is need to add unique Ace behavior for the deale then override addCard() here.
     */

    /**
     * The dealer automatically hits until their score >= 17.
     */
    public void autoPlay(Deck deck) {
        while (this.getScore() < 17) {
            Card card = deck.dealCard();
            this.addCard(card);
        }
    }
}
