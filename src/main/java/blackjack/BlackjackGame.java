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

    //creates a new game with num players, their names and dealer
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

    //each round that goes by
    private void playRound() {
        // Deal initial cards to players and dealer
        for (Player player : players) {
            dealInitialCards(player);
        }
        dealInitialCards(dealer);

        // Player turns
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s turn:");
            System.out.println(player);
            while (getPlayerChoice(player)) {
                Card card = deck.dealCard();
                System.out.println("You drew: " + card);
                player.addCard(card, false, scanner); // Pass false for players
                System.out.println(player);

                if (player.getScore() > 21) {
                    System.out.println(player.getName() + " busts!");
                    break;
                }
            }
        }

        // Dealer's turn
        System.out.println("\nDealer's turn:");
        while (dealer.getScore() < 17) { // Dealer must hit until score is 17+
            Card card = deck.dealCard();
            System.out.println("Dealer drew: " + card);
            dealer.addCard(card, true, scanner); // Pass true for the dealer
        }
        System.out.println(dealer);

        // Determine the winner
        determineWinner();
    }

    //deals the initial cards
    private void dealInitialCards(Player player) {
        player.addCard(deck.dealCard(), player.getName().equals("Dealer"), scanner);
        player.addCard(deck.dealCard(), player.getName().equals("Dealer"), scanner);
    }

    //handles the players choce of hit or stand
    private boolean getPlayerChoice(Player player) {
        System.out.print(player.getName() + ", do you want to 'hit' or 'stand'? ");
        String choice = scanner.nextLine().trim().toLowerCase();
        while (!choice.equals("hit") && !choice.equals("stand")) {
            System.out.print("Invalid input. Please enter 'hit' or 'stand': ");
            choice = scanner.nextLine().trim().toLowerCase();
        }
        return choice.equals("hit");
    }

    private void determineWinner() {
        Player winner = null;
        int highestScore = 0;

        for (Player player : players) {
            if (player.getScore() <= 21 && player.getScore() > highestScore) {
                winner = player;
                highestScore = player.getScore();
            }
        }

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

    //play again choice is given at the end after winner is announced
    private boolean askToPlayAgain() {
        System.out.print("Do you want to play again? (yes/no): ");
        String choice = scanner.nextLine().toLowerCase();
        return choice.equals("yes");
    }

    //getters and setters for testing
    public List<Player> getPlayers() {
        return players;
    }

    public Player getDealer() {
        return dealer;
    }
}
