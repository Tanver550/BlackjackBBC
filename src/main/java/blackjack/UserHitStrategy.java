package blackjack;

public class UserHitStrategy implements HitStrategy {
    private final ConsoleIO io;

    public UserHitStrategy(ConsoleIO io) {
        this.io = io;
    }

    @Override
    public boolean shouldHit(Player player, Dealer dealer) {
        // If the player is already bust, return false
        if (player.getScore() > 21) return false;

        String choice = io.readLine(player.getName() + ", do you want to 'hit' or 'stand'? ").trim().toLowerCase();
        while (!choice.equals("hit") && !choice.equals("stand")) {
            choice = io.readLine("Invalid input. Please enter 'hit' or 'stand': ").trim().toLowerCase();
        }

        return choice.equals("hit");
    }
}
