package blackjack;

import java.util.ArrayList;
import java.util.List;

public class BlackjackGame {
    private ConsoleIO io;
    private Deck deck;
    private List<Player> players;
    private Dealer dealer;

    public BlackjackGame() {
        // instantiate ConsoleIO to manage inputs and outputs
        this.io = new ConsoleIO();
    }

    public void play() {
        boolean keepPlaying = true;
        io.println("Welcome to Blackjack!");

        while (keepPlaying) {
            initialiseGame();
            RoundManager roundManager = new RoundManager(deck, io);
            roundManager.playRound(players, dealer);
            keepPlaying = askToPlayAgain();
        }

        io.println("Thanks for playing Blackjack!");
    }

    void initialiseGame() {
        deck = new Deck(); // builds a standard 52 card deck
        players = new ArrayList<>();
        dealer = new Dealer();

        // Assign the dealer strategy
        dealer.setHitStrategy(new DealerHitStrategy());

        int numPlayers = -1;
        while (numPlayers < 1 || numPlayers > 7) {
            numPlayers = io.readInt("Enter the number of players (1-7): ");
            if (numPlayers < 1 || numPlayers > 7) {
                io.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }

        for (int i = 1; i <= numPlayers; i++) {
            String name = "";
            while (name.trim().isEmpty() || "dealer".equalsIgnoreCase(name.trim())) {
                name = io.readLine("Enter name for Player " + i + ": ");
                if (name.trim().isEmpty() || "dealer".equalsIgnoreCase(name.trim())) {
                    io.println("A valid (non-'Dealer') name is required. Please try again.");
                }
            }
            Player player = new Player(name);
            // Each human player uses a UserHitStrategy
            player.setHitStrategy(new UserHitStrategy(io));
            players.add(player);
        }
    }

    private boolean askToPlayAgain() {
        String choice = io.readLine("Do you want to play again? (yes/no): ").toLowerCase();
        return choice.equals("yes");
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
