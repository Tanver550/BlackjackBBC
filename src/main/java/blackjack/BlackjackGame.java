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

    void initialiseGame() {
        deck = new Deck();
        deck.shuffle();
        players = new ArrayList<>(); // players are seperate from dealer
        dealer = new Dealer();   // dealer is now an extension of player for scalbaility polymorphism

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

        // validation to ensure player cant name themselves dealer or is empty
        for (int i = 1; i <= numPlayers; i++) {
            String name = "";
            while (name.trim().isEmpty() || "dealer".equalsIgnoreCase(name.trim())) {
                System.out.print("Enter name for Player " + i + ": ");
                name = scanner.nextLine();
                if (name.trim().isEmpty() || "dealer".equalsIgnoreCase(name.trim())) {
                    System.out.println("A valid (non-'Dealer') name is required. Please try again.");
                }
            }
            players.add(new Player(name));
        }
    }

    // simulates a round of black jack
    private void playRound() {
        // deal cards first
        dealInitialCardsToAll();

        // players take turns either hit or stand depending on how many there are in the game
        handlePlayerTurns();

        // dealer gets cards
        handleDealerTurn();

        // caculate the winner of the game comapre scores
        determineWinners();
    }

    private void dealInitialCardsToAll() {
        for (Player player : players) {
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
        }
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    private void handlePlayerTurns() {
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s turn:");
            showPlayerHand(player);

            // gives the player the choice of if they want ace to be 11 or 1 as said in the doc, try to keep them in the game and stop them from accidently going bust
            promptForAcesIfAny(player);

            while (playerWantsToHit(player)) {
                Card card = deck.dealCard();
                System.out.println(player.getName() + " draws: " + card);
                player.addCard(card);

                showPlayerHand(player);

                promptForAcesIfAny(player);

                if (player.getScore() > 21) {
                    System.out.println(player.getName() + " busts with " + player.getScore() + "!");
                    break;
                }
            }
        }
    }

    private void showPlayerHand(Player player) {
        System.out.println(player);
    }

    private void promptForAcesIfAny(Player player) {
        // Dealer is not prompted manually its handled automatically
        if (player instanceof Dealer) return;

        for (Card card : player.getHand()) {
            if (card.getRank() == Rank.ACE) {
                System.out.print(player.getName()
                        + ", you have an Ace. It's currently " + card.getValue()
                        + ". Do you want to keep it ("
                        + card.getValue() + ") or change it to " + (card.getValue() == 1 ? 11 : 1) + "? ");

                label:
                while (true) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    switch (input) {
                        case "keep":
                            break label;
                        case "1":
                            if (card.getValue() != 1) {
                                player.adjustAceValue(card, 1);
                            }
                            break label;
                        case "11":
                            if (card.getValue() != 11) {
                                // only do so if it won't bust them
                                int hypothetical = player.getScore() - card.getValue() + 11;
                                if (hypothetical <= 21) {
                                    player.adjustAceValue(card, 11);
                                } else {
                                    System.out.println("Sorry, that would bust you. Keeping it as " + card.getValue());
                                }
                            }
                            break label;
                        default:
                            System.out.print("Invalid input. Enter 'keep', '1' or '11': ");
                            break;
                    }
                }
                System.out.println("New score for " + player.getName() + " is " + player.getScore());
            }
        }
    }

    private boolean playerWantsToHit(Player player) {
        if (player.getScore() > 21) return false;
        System.out.print(player.getName() + ", do you want to 'hit' or 'stand'? ");
        String choice = scanner.nextLine().trim().toLowerCase();
        while (!choice.equals("hit") && !choice.equals("stand")) {
            System.out.print("Invalid input. Please enter 'hit' or 'stand': ");
            choice = scanner.nextLine().trim().toLowerCase();
        }
        return "hit".equals(choice);
    }

    private void handleDealerTurn() {
        System.out.println("\nDealer's turn:");
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
                // if the dealer busts, all players who haven't busted win
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
        return "yes".equals(choice);
    }

    // Getters
    public List<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
