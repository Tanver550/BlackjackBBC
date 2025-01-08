package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackjackGame {
    private Deck deck;
    private List<Player> players;
    private Dealer dealer;
    private final Scanner scanner;

    public BlackjackGame() {
        this.scanner = new Scanner(System.in);
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

    void initialiseGame() {
        deck = new Deck(); // builds a standard 52 deck
        players = new ArrayList<>();
        dealer = new Dealer();

        // et the dealer strategy
        dealer.setHitStrategy(new DealerHitStrategy());

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
            String name = "";
            while (name.trim().isEmpty() || "dealer".equalsIgnoreCase(name.trim())) {
                System.out.print("Enter name for Player " + i + ": ");
                name = scanner.nextLine();
                if (name.trim().isEmpty() || "dealer".equalsIgnoreCase(name.trim())) {
                    System.out.println("A valid (non-'Dealer') name is required. Please try again.");
                }
            }
            Player player = new Player(name);
            // Each human player uses a UserHitStrategy
            player.setHitStrategy(new UserHitStrategy(scanner));
            players.add(player);
        }
    }

    private void playRound() {
        // Shuffle if needed
        deck.shuffle();

        // Deal initial 2 cards to each player
        for (Player player : players) {
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
        }
        // Dealer gets 2 cards
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());

        // Each player turn
        handlePlayerTurns();

        // Dealer turn
        handleDealerTurn();

        // Determine winners
        determineWinners();
    }

    private void handlePlayerTurns() {
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s turn:");
            System.out.println(player);

            // while the player strategy says hit and they havent busted
            while (player.shouldHit(dealer)) {
                Card card = deck.dealCard();
                player.addCard(card);
                System.out.println(player.getName() + " draws: " + card);
                System.out.println(player);

                if (player.getScore() > 21) {
                    System.out.println(player.getName() + " busts with " + player.getScore() + "!");
                    break;
                }
            }
        }
    }

    private void handleDealerTurn() {
        System.out.println("\nDealer's turn:");
        System.out.println(dealer);

        // Let the Dealer autoplay with DealerHitStrategy
        dealer.autoPlay(deck);

        System.out.println(dealer);
    }

    private void determineWinners() {
        int dealerScore = dealer.getScore();
        System.out.println("\nDealer's final score is " + dealerScore);

        for (Player player : players) {
            int playerScore = player.getScore();
            if (playerScore > 21) {
                System.out.println(player.getName() + " loses with " + playerScore + ".");
            }
            else if (dealerScore > 21) {
                // if dealer busts all players who havent busted, win
                System.out.println(player.getName() + " wins with " + playerScore + "!");
            }
            else {
                if (playerScore > dealerScore && playerScore <= 21) {
                    System.out.println(player.getName() + " wins with " + playerScore + "!");
                } else if (playerScore < dealerScore) {
                    System.out.println(player.getName() + " loses with " + playerScore + ".");
                } else {
                    // tie or push
                    System.out.println(player.getName() + " pushes with " + playerScore + " (tie).");
                }
            }
        }
    }

    private boolean askToPlayAgain() {
        System.out.print("Do you want to play again? (yes/no): ");
        String choice = scanner.nextLine().toLowerCase();
        return choice.equals("yes");
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
