package blackjack;

/**
 * Strategy interface for deciding whether a player should hit or stand
 * each player has a hitStrategy
 */
public interface HitStrategy {
    /**
     * @param player the Player who is deciding to hit
     * @param dealer the Dealer
     * @return true if the player should take another card, false otherwise
     */
    boolean shouldHit(Player player, Dealer dealer);
}
