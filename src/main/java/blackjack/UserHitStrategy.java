package blackjack;

import java.util.Scanner;

/**
 * A HitStrategy that prompts the user via console whether to hit or stand
 */
public class UserHitStrategy implements HitStrategy {
    private final Scanner scanner;

    public UserHitStrategy(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public boolean shouldHit(Player player, Dealer dealer) {
        // If the player is already bust return false
        if (player.getScore() > 21) return false;

        System.out.print(player.getName() + ", do you want to 'hit' or 'stand'? ");
        String choice = scanner.nextLine().trim().toLowerCase();
        while (!choice.equals("hit") && !choice.equals("stand")) {
            System.out.print("Invalid input. Please enter 'hit' or 'stand': ");
            choice = scanner.nextLine().trim().toLowerCase();
        }
        return choice.equals("hit");
    }
}
