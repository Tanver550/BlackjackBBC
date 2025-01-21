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

        io.println("\nFinal Scoreboard:");
        for (Player p : players) {
            io.println(p.getName() + " => Wins: " + p.getWins()
                    + ", Losses: " + p.getLosses()
                    + ", Pushes: " + p.getPushes());
        }
    }

    void initialiseGame() {

        // build a standard 52 card deck
        if (this.deck == null) {
            deck = new Deck(6);
        }

        players = new ArrayList<>();
        dealer = new Dealer();

        // Assign the dealer strategy
        dealer.setHitStrategy(new DealerHitStrategy());

        // enter number of players
        int numPlayers = -1;
        while (numPlayers < 1 || numPlayers > 7) {
            numPlayers = io.readInt("Enter the number of players (1-7): ");
            if (numPlayers < 1 || numPlayers > 7) {
                io.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }

        // assigning players names
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

    // used to create a rigged deck for testing. or else the game used a standard 52 card deck
    // this allows the test code to know exactly which cards are in the deck, instead of the random ones from shuffle
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    private boolean askToPlayAgain() {
        while (true) {
            String choice = io.readLine("Do you want to play again? (yes/no): ").trim().toLowerCase();

            if (choice.equals("yes")) {
                return true;
            }
            else if (choice.equals("no")) {
                return false;
            }
            else {
                io.println("Invalid input. Please type 'yes' or 'no'.");
            }
        }
    }


    public List<Player> getPlayers() {
        return players;
    }

    public Dealer getDealer() {
        return dealer;
    }
}
