package blackjack;

/**
 * A HitStrategy for the Dealer, following standard Blackjack rules
 * The dealer hits while score < 17 stands otherwise
 */
public class DealerHitStrategy implements HitStrategy {

    @Override
    public boolean shouldHit(Player player, Dealer dealer) {
        // Usually the dealer hits until the score is at least 17
        return player.getScore() < 17;
    }
}
