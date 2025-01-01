package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackjackGame {
    public Deck deck;
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

        // validation for no name
        for (int i = 1; i <= numPlayers; i++) {
            String name = "";
            while (name.trim().isEmpty()) {
                System.out.print("Enter name for Player " + i + ": ");
                name = scanner.nextLine();
                if (name.trim().isEmpty()) {
                    System.out.println("A valid name is required. Please try again.");
                }
            }
            players.add(new Player(name));
        }

    }

    private void playRound() {
        // Deal initial cards
        dealInitialCardsToAll();

        // Each player's turn
        handlePlayerTurns();

        // Dealer's turn
        handleDealerTurn();

        // Determine winners
        determineWinners();
    }

    private void dealInitialCardsToAll() {
        // Each non-dealer gets 2 cards
        for (Player player : players) {
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
        }
        // Dealer gets 2 cards
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    private void handlePlayerTurns() {
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s turn:");
            showPlayerHand(player);

            // Let the user reassign Ace values if desired (they can see their hand now)
            promptForAcesIfAny(player);

            // Continue until player stands or busts
            while (playerWantsToHit(player)) {
                Card card = deck.dealCard();
                System.out.println(player.getName() + " draws: " + card);

                // Add card with default Ace logic
                player.addCard(card);

                // Show new hand
                showPlayerHand(player);

                // Prompt to reassign Aces again, if the new card was an Ace or to re-adjust
                promptForAcesIfAny(player);

                if (player.getScore() > 21) {
                    System.out.println(player.getName() + " busts with " + player.getScore() + "!");
                    break;
                }
            }
        }
    }

    private void showPlayerHand(Player player) {
        // Just print their current hand and score
        System.out.println(player);
    }

    /**
     * Goes through each Ace in the player's hand and offers them a chance to change it to 1 or 11
     * after they've seen their hand. This is only for non-dealer players.
     */
    private void promptForAcesIfAny(Player player) {
        if (player == dealer) return; // dealer logic is automated, no user prompt

        for (Card card : player.getHand()) {
            if (card.getRank().equals("Ace")) {
                // Ask the user if they want 1 or 11
                System.out.print(player.getName()
                        + ", you have an Ace. It's currently " + card.getValue()
                        + ". Do you want to keep it ("
                        + card.getValue() + ") or change it to " + (card.getValue() == 1 ? 11 : 1) + "? ");
                // The alternative is if card is 1, we offer 11; if card is 11, we offer 1.

                while (true) {
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("keep")) {
                        // do nothing
                        break;
                    } else if (input.equals("1")) {
                        if (card.getValue() != 1) { // only adjust if needed
                            player.adjustAceValue(card, 1);
                        }
                        break;
                    } else if (input.equals("11")) {
                        if (card.getValue() != 11) {
                            // but ensure we won't bust them automatically
                            if (player.getScore() - card.getValue() + 11 <= 21) {
                                player.adjustAceValue(card, 11);
                            } else {
                                System.out.println("Sorry, that would bust you. Keeping it as " + card.getValue());
                            }
                        }
                        break;
                    } else {
                        System.out.print("Invalid input. Enter 'keep', '1' or '11': ");
                    }
                }
                System.out.println("New score for " + player.getName() + " is " + player.getScore());
            }
        }
    }

    private boolean playerWantsToHit(Player player) {
        if (player.getScore() > 21) return false; // already bust
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
        // Standard dealer logic: hits until score >= 17
        while (dealer.getScore() < 17) {
            Card card = deck.dealCard();
            System.out.println("Dealer draws: " + card);
            dealer.addCard(card);
        }
        System.out.println(dealer);
    }

    /**
     * - If dealer busts, all non-busted players win.
     * - Otherwise, each player compares their final score to the dealer's.
     */
    private void determineWinners() {
        int dealerScore = dealer.getScore();
        System.out.println("\nDealer's final score is " + dealerScore);

        for (Player player : players) {
            int playerScore = player.getScore();
            if (playerScore > 21) {
                // already bust
                System.out.println(player.getName() + " loses with " + playerScore + ".");
            }
            else if (dealerScore > 21) {
                // dealer bust => player wins
                System.out.println(player.getName() + " wins with " + playerScore + "!");
            }
            else {
                // compare
                if (playerScore > dealerScore) {
                    System.out.println(player.getName() + " wins with " + playerScore + "!");
                } else if (playerScore < dealerScore) {
                    System.out.println(player.getName() + " loses with " + playerScore + ".");
                } else {
                    // tie => push
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

    // Getters testing
    public List<Player> getPlayers() {
        return players;
    }

    public Player getDealer() {
        return dealer;
    }
}
