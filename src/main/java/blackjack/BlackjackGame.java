package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackjackGame {
    private Deck deck;
    private List<Player> players;
    private Player dealer;
    private final Scanner scanner;

    public BlackjackGame() {
        scanner = new Scanner(System.in);
    }

    public void play() {
        boolean keepPlaying = true;
        while (keepPlaying) {
            initialiseGame();
            playRound();
            keepPlaying = askToPlayAgain();
        }
        System.out.println("Thanks for playing Blackjack!");
    }

    // Sets up a new game: deck, number of players, players' names, and the dealer
    void initialiseGame() {
        deck = new Deck();
        deck.shuffle();
        players = new ArrayList<>();
        dealer = new Player("Dealer");

        System.out.println("Welcome to Blackjack!");
        int numPlayers = 0;
        while (numPlayers < 1 || numPlayers > 7) {
            try {
                System.out.print("Enter the number of players (1-7): ");
                numPlayers = Integer.parseInt(scanner.nextLine());
                if (numPlayers < 1 || numPlayers > 7) {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }
    }

    // Deals two cards to the given player
    private void dealInitialCards(Player player) {
        if (player.getName().equals("Dealer")) {
            player.addCard(deck.dealCard(), true, scanner);
            player.addCard(deck.dealCard(), true, scanner);
        } else {
            // this is to make sure the player has the choice of eitheer 1 or 11 when ace is handed out.
            player.addCard(deck.dealCard(), false, scanner);
            player.addCard(deck.dealCard(), false, scanner);
        }
    }

    private void playRound() {
        // 1) Deal initial cards
        dealInitialCardsToEveryone();

        // 2) Each player takes a turn
        handlePlayerTurns();

        // 3) Dealer takes a turn
        handleDealerTurn();

        // 4) Determine the winner
        determineWinner();
    }

    private void dealInitialCardsToEveryone() {
        for (Player player : players) {
            dealInitialCards(player);
        }
        dealInitialCards(dealer);
    }

    private void handlePlayerTurns() {
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s turn:");
            System.out.println(player);

            while (getPlayerChoice(player)) {  // "Hit" or "Stand"
                Card card = deck.dealCard();
                System.out.println("You drew: " + card);
                player.addCard(card, false, scanner);  // For subsequent hits, let the player decide Ace value
                System.out.println(player);

                if (player.getScore() > 21) {
                    System.out.println(player.getName() + " busts!");
                    break;
                }
            }
        }
    }

    // Asks the player if they want to "hit" or "stand"
    private boolean getPlayerChoice(Player player) {
        System.out.print(player.getName() + ", do you want to 'hit' or 'stand'? ");
        String choice = scanner.nextLine().trim().toLowerCase();
        while (!choice.equals("hit") && !choice.equals("stand")) {
            System.out.print("Invalid input. Please enter 'hit' or 'stand': ");
            choice = scanner.nextLine().trim().toLowerCase();
        }
        return choice.equals("hit");
    }

    private void handleDealerTurn() {
        System.out.println("\nDealer's turn:");
        while (dealer.getScore() < 17) {
            Card card = deck.dealCard();
            System.out.println("Dealer drew: " + card);
            dealer.addCard(card, true, scanner); // in my case, dealer is computer/casino, so ace handlig is done to ensure dealer doesnt go bust
        }
        System.out.println(dealer);
    }

    private void determineWinner() {
        Player winner = null;
        int highestScore = 0;

        // Find the highest-scoring player (<= 21)
        for (Player player : players) {
            if (player.getScore() <= 21 && player.getScore() > highestScore) {
                winner = player;
                highestScore = player.getScore();
            }
        }

        // Compare dealer against that highest score
        if (dealer.getScore() <= 21 && dealer.getScore() > highestScore) {
            winner = dealer;
            highestScore = dealer.getScore();
        }

        if (winner != null) {
            if (winner == dealer) {
                System.out.println("\nDealer wins with a score of " + dealer.getScore() + "!");
            } else {
                System.out.println("\n" + winner.getName() + " wins with a score of " + winner.getScore() + "!");
            }
        } else {
            System.out.println("\nNo one wins! All players busted.");
        }
    }

    // Asks the user if they want to play another round
    private boolean askToPlayAgain() {
        System.out.print("Do you want to play again? (yes/no): ");
        String choice = scanner.nextLine().toLowerCase();
        return choice.equals("yes");
    }

    // Getters for testing
    public List<Player> getPlayers() {
        return players;
    }

    public Player getDealer() {
        return dealer;
    }
}
