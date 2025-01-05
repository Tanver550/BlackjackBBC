package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerAddCard() {
        Player player = new Player("TestPlayer");
        Card card = new Card("Ace", "Spades", 11);

        player.addCard(card);

        assertEquals(11, player.getScore());
        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().get(0));
    }

    @Test
    void testDealerAddCard() {
        Player dealer = new Player("Dealer");
        Card card = new Card("Ace", "Hearts", 11);

        // Automated dealer logic
        dealer.addCard(card);

        assertEquals(11, dealer.getScore());
        assertEquals(1, dealer.getHand().size());
        assertEquals(card, dealer.getHand().get(0));
    }

    @Test
    void scenario4_testValidHandUnderOrEqual21() {
        Player player = new Player("ValidHandPlayer");

        // 1) Add King (10)
        player.addCard(new Card("King", "Hearts", 10));
        // 2) Add Queen (10)
        player.addCard(new Card("Queen", "Diamonds", 10));
        // Now total is 20
        // 3) Add Ace sp We'll pick "1" from userInput => final total = 21
        player.addCard(new Card("Ace", "Clubs", 11));

        assertTrue(player.getScore() <= 21, "Score should be 21 or less => valid hand");
        assertEquals(21, player.getScore(), "Expected final score to be 21");
    }


    @Test
    void scenario6_testKingAndAceEquals21() {
        Player player = new Player("AceKing");

        // King is 10
        player.addCard(new Card("King", "Hearts", 10));

        // user picks 11 so total becomes 21
        player.addCard(new Card("Ace", "Spades", 11));

        assertEquals(21, player.getScore(), "King (10) + Ace (11) should be 21");
    }

    @Test
    void scenario7_testKingQueenAceEquals21() {
        Player player = new Player("AceKQ");

        // King is 10
        player.addCard(new Card("King", "Hearts", 10));
        // Queen is 10
        player.addCard(new Card("Queen", "Diamonds", 10));

        // So total now 20, Ace, user picks 1 so 21
        player.addCard(new Card("Ace", "Clubs", 11));

        assertEquals(21, player.getScore(), "King (10) + Queen (10) + Ace (1) should be 21");
    }

    @Test
    void scenario8_testNineAndTwoAcesEquals21() {
        Player player = new Player("DoubleAce");

        player.addCard(new Card("9", "Hearts", 9));
        assertEquals(9, player.getScore());

        // first Ace pick 11  total 20
        player.addCard(new Card("Ace", "Clubs", 11));
        assertEquals(20, player.getScore());

        // second Ace pick 1  total 21
        player.addCard(new Card("Ace", "Diamonds", 11));
        assertEquals(21, player.getScore(), "9 + Ace(11) + Ace(1) should result in 21");
    }

}
