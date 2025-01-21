package blackjack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerAddCard() {
        Player player = new Player("John");

        Card card = new Card(Rank.ACE, Suit.SPADES);
        // Add the card to the player first hand
        player.addCard(card);

        // Check that the score is 11, ACE is 11 default
        assertEquals(11, player.getScore());

        // check the first hand cards
        assertEquals(1, player.getHands().get(0).getCards().size(), "First hand should have 1 card.");
        assertEquals(card, player.getHands().get(0).getCards().get(0), "Card should match the one added.");
    }

    @Test
    void scenario4_testValidHandUnderOrEqual21() {
        Player player = new Player("ValidHandPlayer");

        // 1) Add King (10)
        player.addCard(new Card(Rank.KING, Suit.HEARTS));
        // 2) Add Queen (10)
        player.addCard(new Card(Rank.QUEEN, Suit.HEARTS));
        // total: 20

        // 3) Add Ace as 1 => total 21
        Card ace = new Card(Rank.ACE, Suit.SPADES);
        ace.setOverrideValue(1);  // simulate user choosing 1
        player.addCard(ace);

        assertTrue(player.getScore() <= 21, "Score should be 21 or less => valid hand");
        assertEquals(21, player.getScore(), "Expected final score to be 21");
    }

    @Test
    void scenario6_testKingAndAceEquals21() {
        Player player = new Player("AceKing");

        // King=10
        player.addCard(new Card(Rank.KING, Suit.HEARTS));

        // Ace => default or user picks 11 => total=21
        player.addCard(new Card(Rank.ACE, Suit.SPADES));

        assertEquals(21, player.getScore(), "King (10) + Ace (11) should be 21");
    }

    @Test
    void scenario7_testKingQueenAceEquals21() {
        Player player = new Player("AceKQ");

        // King=10
        player.addCard(new Card(Rank.KING, Suit.HEARTS));
        // Queen=10 => total=20
        player.addCard(new Card(Rank.QUEEN, Suit.HEARTS));

        // Ace => override as 1 => total=21
        Card ace = new Card(Rank.ACE, Suit.HEARTS);
        ace.setOverrideValue(1);
        player.addCard(ace);

        assertEquals(21, player.getScore(), "King(10)+Queen(10)+Ace(1) => 21");
    }

    @Test
    void scenario8_testNineAndTwoAcesEquals21() {
        Player player = new Player("DoubleAce");

        player.addCard(new Card(Rank.NINE, Suit.CLUBS));
        assertEquals(9, player.getScore());

        // 1st Ace => override as 11 => total=20
        Card ace1 = new Card(Rank.ACE, Suit.CLUBS);
        ace1.setOverrideValue(11);
        player.addCard(ace1);
        assertEquals(20, player.getScore(), "9 + Ace(11) => 20");

        // 2nd Ace => override as 1 => total=21
        Card ace2 = new Card(Rank.ACE, Suit.DIAMONDS);
        ace2.setOverrideValue(1);
        player.addCard(ace2);

        assertEquals(21, player.getScore(), "9 + Ace(11) + Ace(1) => 21");
    }
}
