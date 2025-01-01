package blackjack;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerAddCard() {
        Player player = new Player("John");
        Card card = new Card("Ace", "Spades", 11);
        Scanner scanner = new Scanner(new ByteArrayInputStream("11\n".getBytes()));

        player.addCard(card, false, scanner);

        assertEquals(11, player.getScore());
        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().get(0));
    }

    @Test
    void testDealerAddCard() {
        Player dealer = new Player("Dealer");
        Card card = new Card("Ace", "Hearts", 11);

        // Automated dealer logic
        dealer.addCard(card, true, new Scanner(System.in));

        assertEquals(11, dealer.getScore());
        assertEquals(1, dealer.getHand().size());
        assertEquals(card, dealer.getHand().get(0));
    }

    @Test
    void scenario4_testValidHandUnderOrEqual21() {
        Player player = new Player("ValidHandPlayer");

        String userInput = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(in);

        // 1) Add King (10)
        player.addCard(new Card("King", "Hearts", 10), false, scanner);
        // 2) Add Queen (10)
        player.addCard(new Card("Queen", "Diamonds", 10), false, scanner);
        // Now total is 20
        // 3) Add Ace => We'll pick "1" from userInput => final total = 21
        player.addCard(new Card("Ace", "Clubs", 11), false, scanner);

        assertTrue(player.getScore() <= 21, "Score should be 21 or less => valid hand");
        assertEquals(21, player.getScore(), "Expected final score to be 21");
    }


    @Test
    void scenario6_testKingAndAceEquals21() {
        Player player = new Player("AceKing");
        String userInput = "11\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(in);

        // King => 10
        player.addCard(new Card("King", "Hearts", 10), false, scanner);

        // Ace => user picks 11 => total becomes 21
        player.addCard(new Card("Ace", "Spades", 11), false, scanner);

        assertEquals(21, player.getScore(),
                "King (10) + Ace (11) should be 21");
    }

    @Test
    void scenario7_testKingQueenAceEquals21() {
        Player player = new Player("AceKQ");
        // We'll feed "1" so the Ace is counted as 1
        String userInput = "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(in);

        // King => 10
        player.addCard(new Card("King", "Hearts", 10), false, scanner);
        // Queen => 10
        player.addCard(new Card("Queen", "Diamonds", 10), false, scanner);

        // So total now 20; Ace => user picks 1 => 21
        player.addCard(new Card("Ace", "Clubs", 11), false, scanner);

        assertEquals(21, player.getScore(),
                "King (10) + Queen (10) + Ace (1) should be 21");
    }

    @Test
    void scenario8_testNineAndTwoAcesEquals21() {
        Player player = new Player("DoubleAce");
        // We'll feed "11" for the first Ace, "1" for the second Ace => total 21
        String userInput = "11\n1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(in);

        // 9 => 9
        player.addCard(new Card("9", "Hearts", 9), false, scanner);
        assertEquals(9, player.getScore());

        // First Ace => pick 11 => total 20
        player.addCard(new Card("Ace", "Clubs", 11), false, scanner);
        assertEquals(20, player.getScore());

        // Second Ace => pick 1 => total 21
        player.addCard(new Card("Ace", "Diamonds", 11), false, scanner);
        assertEquals(21, player.getScore(),
                "9 + Ace(11) + Ace(1) should result in 21");
    }


}
