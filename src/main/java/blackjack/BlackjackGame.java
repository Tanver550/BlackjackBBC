package blackjack;

import java.util.Scanner;

public class BlackjackGame {
    private Deck deck;
    private Player player;
    private Player dealer;

    public BlackjackGame() {
        deck = new Deck();
        deck.shuffle();
        player = new Player("Player");
        dealer = new Player("Dealer");
    }

    public void play() {
        System.out.println("Welcome to Blackjack!");

        // Deal initial cards
        dealInitialCards(player);
        dealInitialCards(dealer);

        // Player's turn
        System.out.println(player);
        while (getPlayerChoice()) {
            Card card = deck.dealCard();
            System.out.println("You drew: " + card);
            player.addCard(card);
            System.out.println(player);

            if (player.getScore() > 21) {
                System.out.println("You bust! Dealer wins.");
                return;
            }
        }

        // Dealer's turn
        System.out.println("\nDealer's turn:");
        while (dealer.getScore() < 17) { // Dealer must hit until score is 17+
            Card card = deck.dealCard();
            System.out.println("Dealer drew: " + card);
            dealer.addCard(card);
        }
        System.out.println(dealer);

        // Determine winner
        determineWinner();
    }

    private void dealInitialCards(Player player) {
        player.addCard(deck.dealCard());
        player.addCard(deck.dealCard());
    }

    private boolean getPlayerChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to 'hit' or 'stand'? ");
        String choice = scanner.nextLine().toLowerCase();
        return choice.equals("hit");
    }

    private void determineWinner() {
        if (dealer.getScore() > 21 || player.getScore() > dealer.getScore()) {
            System.out.println("You win!");
        } else if (player.getScore() == dealer.getScore()) {
            System.out.println("It's a tie!");
        } else {
            System.out.println("Dealer wins!");
        }
    }
}
