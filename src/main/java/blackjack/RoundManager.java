package blackjack;

import java.util.List;

public class RoundManager {

    private final Deck deck;
    private final ConsoleIO io;

    public RoundManager(Deck deck, ConsoleIO io) {
        this.deck = deck;
        this.io = io;
    }

    public void playRound(List<Player> players, Dealer dealer) {
        deck.shuffle();
        dealInitialCards(players, dealer);

        // each player can split repeatedly,
        // then hit or stand each resulting hand.
        for (Player player : players) {
            io.println("\n" + player.getName() + "'s turn:");
            seatOrderSplits(player);
            seatOrderHitStand(player, dealer);
        }

        // Dealer turn
        handleDealerTurn(dealer);

        // get the scores and determine the winner
        determineWinners(players, dealer);
    }

    private void dealInitialCards(List<Player> players, Dealer dealer) {
        // Each player FIRST hand gets 2 cards
        for (Player player : players) {
            player.getHands().get(0).addCard(deck.dealCard());
            player.getHands().get(0).addCard(deck.dealCard());
        }
        // Dealer gets 2 cards
        dealer.addCard(deck.dealCard());
        dealer.addCard(deck.dealCard());
    }

    /**
     * Allows a player to split up to 3 times (ending with 4 total hands)
     * in seat order, check if any hand is a pair, and ask the user if they want to split.
     * If they do, create a new hand, remove the second card from the original,
     * deal one extra card to each
     *  keep going as for upto a total of 4 hands
     */
    private void seatOrderSplits(Player player) {
        boolean splittingPossible = true;

        while (splittingPossible) {
            splittingPossible = false;

            //  use a normal for-loop with index
            for (int hIndex = 0; hIndex < player.getHands().size(); hIndex++) {
                // If player reached 4 total hands, break out (no more splitting allowed)
                if (player.getHands().size() == 4) {
                    break;
                }

                Hand currentHand = player.getHands().get(hIndex);
                List<Card> cardsInHand = currentHand.getCards();

                // Only check for split if exactly 2 cards in that hand
                if (cardsInHand.size() == 2) {
                    Card c1 = cardsInHand.get(0);
                    Card c2 = cardsInHand.get(1);
                    // If they the same rank, ask user
                    if (c1.getRank() == c2.getRank()) {
                        io.print(player.getName() + ", you have a pair of "
                                + c1.getRank()
                                + "s in Hand #" + (hIndex+1)
                                + ". Do you want to split? (yes/no): ");
                        String choice = io.readLine("").trim().toLowerCase();
                        if (choice.equals("yes")) {
                            performSplit(player, hIndex, c1, c2);
                            splittingPossible = true;
                            //  might do multiple splits so keep the while loop going
                            // in case the newly created hand is also a pair.
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * split the hand
     * Remove second card from the old hand,
     * create a new hand with that card,
     * deal one extra card to each hand.
     */
    private void performSplit(Player player, int handIndex, Card c1, Card c2) {
        Hand oldHand = player.getHands().get(handIndex);
        // remove c2 from oldHand so it only has card 1
        oldHand.getCards().remove(1);

        // create a new Hand with card 2
        Hand newHand = new Hand();
        newHand.addCard(c2);

        // Insert the new hand right after the old one
        player.getHands().add(handIndex+1, newHand);

        // deal an extra card to each
        Card extraForOldHand = deck.dealCard();
        Card extraForNewHand = deck.dealCard();

        oldHand.addCard(extraForOldHand);
        newHand.addCard(extraForNewHand);

        io.println(player.getName() + " splits Hand #" + (handIndex+1)
                + "! Old hand gets " + extraForOldHand
                + ", new hand gets " + c2 + " + " + extraForNewHand);
    }

    /**
     * let the player hit or stand for each of their hands in order
     */
    private void seatOrderHitStand(Player player, Dealer dealer) {
        for (int hIndex = 0; hIndex < player.getHands().size(); hIndex++) {
            Hand currentHand = player.getHands().get(hIndex);
            io.println("\nPlaying Hand #" + (hIndex+1) + ": " + currentHand + " (Score=" + currentHand.calculateScore() + ")");

            // keep drawing until bust or stand
            while (shouldHitThisHand(currentHand)) {
                Card card = deck.dealCard();

                // ask user if its an ace => 1 or 11
                if (card.getRank() == Rank.ACE) {
                    String input = io.readLine(player.getName() + ", you've drawn an Ace! Do you want it to count as '1' or '11'? ");
                    while (!input.equals("1") && !input.equals("11")) {
                        input = io.readLine("Invalid input. Please enter '1' or '11': ");
                    }
                    card.setOverrideValue(Integer.parseInt(input));
                }

                currentHand.addCard(card);

                int newScore = currentHand.calculateScore();
                io.println(player.getName() + " draws: " + card + " => New score: " + newScore);

                if (newScore > 21) {
                    io.println("Bust! Score = " + newScore);
                    break;
                }
            }
        }
    }

    /**
     * Asks the player to Hit or Stand
     */
    private boolean shouldHitThisHand(Hand hand) {
        if (hand.calculateScore() > 21) {
            return false; // already bust
        }

        io.print("Do you want to 'hit' or 'stand'? ");
        String choice = io.readLine("").trim().toLowerCase();
        while (!choice.equals("hit") && !choice.equals("stand")) {
            choice = io.readLine("Invalid input. Please enter 'hit' or 'stand': ").trim().toLowerCase();
        }
        return choice.equals("hit");
    }

    private void handleDealerTurn(Dealer dealer) {
        io.println("\nDealer's turn:");
        io.println(dealer.toString());

        dealer.autoPlay(deck);

        io.println(dealer.toString());
    }

    private void determineWinners(List<Player> players, Dealer dealer) {
        int dealerScore = dealer.getScore();
        io.println("\nDealer's final score is " + dealerScore);

        for (Player player : players) {
            for (int hIndex = 0; hIndex < player.getHands().size(); hIndex++) {
                Hand hand = player.getHands().get(hIndex);
                int playerScore = hand.calculateScore();

                String label = player.getName() + " (Hand #" + (hIndex+1) + ")";
                if (playerScore > 21) {
                    io.println(label + " busts with " + playerScore + ".");
                    player.recordLoss();  // <--- record loss
                }
                else if (dealerScore > 21) {
                    io.println(label + " wins with " + playerScore + "!");
                    player.recordWin();   // <--- record win
                }
                else {
                    if (playerScore > dealerScore) {
                        io.println(label + " wins with " + playerScore + "!");
                        player.recordWin();  // <--- record win
                    } else if (playerScore < dealerScore) {
                        io.println(label + " loses with " + playerScore + ".");
                        player.recordLoss(); // <--- record loss
                    } else {
                        io.println(label + " pushes with " + playerScore + " (tie).");
                        player.recordPush(); // <--- record push
                    }
                }
            }
        }
    }
}
