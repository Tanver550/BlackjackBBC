package blackjack;

/**
 * A specialised Player representing the Dealer
 * uses a DealerHitStrategy
 */
public class Dealer extends Player {

    public Dealer() {
        super("Dealer");
    }

    /**
     * The dealer automatically hits (or stands) based on DealerHitStrategy
     * until the score >= 17 in typical Blackjack
     */
    public void autoPlay(Deck deck) {
        // keep dealing cards while strategy says hit
        while (shouldHit(this)) {
            addCard(deck.dealCard());
        }
    }
}
