package blackjack;

import java.util.List;

// RoundManager now manages what happens in a single round of blackjack, e.g, dealing cards, player and dealer turns etc.

public class RoundManager {

    private final Deck deck;
    private final ConsoleIO io;

    public RoundManager(Deck deck, ConsoleIO io) {
        this.deck = deck;
        this.io = io;
    }

    /**
     * Plays a single round of Blackjack with the given players and dealer
     */
    public void playRound(List<Player> players, Dealer dealer) {
        deck.shuffle();
        dealInitialCards(players, dealer);
        handlePlayerTurns(players, dealer);
        handleDealerTurn(dealer);
        determineWinners(players, dealer);
    }

    private void dealInitialCards(List<Player> players, Dealer dealer) {
        for (Player player : players) {
            player.addCard(deck.dealCard());
            player.addCard(deck.dealCard());
        }
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    private void handlePlayerTurns(List<Player> players, Dealer dealer) {
        for (Player player : players) {
            io.println("\n" + player.getName() + "'s turn:");
            io.println(player.toString());

            while (player.shouldHit(dealer)) {
                Card card = deck.dealCard();

                // If its an Ace, ask the user if its 1 or 11
                if (card.getRank() == Rank.ACE) {
                    String input = io.readLine(
                            player.getName() + ", you've drawn an Ace! Do you want it to count as '1' or '11'? "
                    );
                    while (!input.equals("1") && !input.equals("11")) {
                        input = io.readLine("Invalid input. Please enter '1' or '11': ");
                    }
                    card.setOverrideValue(Integer.parseInt(input));
                }

                player.addCard(card);

                io.println(player.getName() + " draws: " + card);
                io.println(player.toString());

                if (player.getScore() > 21) {
                    io.println(player.getName() + " busts with " + player.getScore() + "!");
                    break;
                }
            }
        }
    }

    private void handleDealerTurn(Dealer dealer) {
        io.println("\nDealer's turn:");
        io.println(dealer.toString());

        // Let the Dealer autoplay with DealerHitStrategy
        dealer.autoPlay(deck);

        io.println(dealer.toString());
    }

    private void determineWinners(List<Player> players, Dealer dealer) {
        int dealerScore = dealer.getScore();
        io.println("\nDealer's final score is " + dealerScore);

        for (Player player : players) {
            int playerScore = player.getScore();
            if (playerScore > 21) {
                io.println(player.getName() + " loses with " + playerScore + ".");
            }
            else if (dealerScore > 21) {
                // if dealer busts, all players who havent busted win
                io.println(player.getName() + " wins with " + playerScore + "!");
            }
            else {
                if (playerScore > dealerScore && playerScore <= 21) {
                    io.println(player.getName() + " wins with " + playerScore + "!");
                } else if (playerScore < dealerScore) {
                    io.println(player.getName() + " loses with " + playerScore + ".");
                } else {
                    // tie
                    io.println(player.getName() + " pushes with " + playerScore + " (tie).");
                }
            }
        }
    }
}
