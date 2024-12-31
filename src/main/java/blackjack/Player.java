package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String name;
    private List<Card> hand;
    private int score;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    public void addCard(Card card, boolean isDealer, Scanner scanner) {
        hand.add(card);

        // If the card is an Ace
        if (card.getRank().equals("Ace")) {
            if (isDealer) {
                // Automated logic for the dealer
                if (score + 11 <= 21) {
                    score += 11;
                } else {
                    score += 1;
                }
            } else {
                // Prompt the player for Ace value
                System.out.print(name + ", you received an Ace! Do you want it to count as 1 or 11? ");
                while (true) {
                    String input = scanner.nextLine().trim();
                    if (input.equals("1")) {
                        score += 1;
                        break;
                    } else if (input.equals("11")) {
                        score += 11;
                        break;
                    } else {
                        System.out.print("Invalid input. Please enter 1 or 11: ");
                    }
                }
            }
        } else {
            // Add the value of other cards
            score += card.getValue();
        }
    }

    // Getters
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
