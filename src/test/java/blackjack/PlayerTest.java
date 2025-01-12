package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void testPlayerAddCard() {
        Player player = new Player("John");

        Card card = new Card(Rank.ACE, Suit.SPADES);

        // Add the card to the player hand
        player.addCard(card);

        // Check that the score is 11 and the hand has 1 card
        assertEquals(11, player.getScore());
        assertEquals(1, player.getHand().getCards().size());
        assertEquals(card, player.getHand().getCards().get(0));
    }

    @Test
    void testDealerAddCard() {
        Player dealer = new Player("Dealer");

        Card card = new Card(Rank.ACE, Suit.HEARTS);

        dealer.addCard(card);

        // Score should be 11 if an Ace was added as 11 by default
        assertEquals(11, dealer.getScore());
        assertEquals(1, dealer.getHand().getCards().size());
        assertEquals(card, dealer.getHand().getCards().get(0));
    }

    @Test
    void scenario4_testValidHandUnderOrEqual21() {
        Player player = new Player("ValidHandPlayer");

        // 1) Add King (10)
        player.addCard(new Card(Rank.KING, Suit.HEARTS));
        // 2) Add Queen (10)
        player.addCard(new Card(Rank.QUEEN, Suit.HEARTS));
        // Now total is 20

        // 3) Add Ace ,want it to be worth 1 so total is 21
        Card ace = new Card(Rank.ACE, Suit.SPADES);
        ace.setOverrideValue(1);  // Simulate the user choosing 1
        player.addCard(ace);

        assertTrue(player.getScore() <= 21, "Score should be 21 or less => valid hand");
        assertEquals(21, player.getScore(), "Expected final score to be 21");
    }


    @Test
    void scenario6_testKingAndAceEquals21() {
        Player player = new Player("AceKing");

        // King is 10
        player.addCard(new Card(Rank.KING, Suit.HEARTS));

        // user picks 11 so total becomes 21
        player.addCard(new Card(Rank.ACE, Suit.SPADES));

        assertEquals(21, player.getScore(), "King (10) + Ace (11) should be 21");
    }

    @Test
    void scenario7_testKingQueenAceEquals21() {
        Player player = new Player("AceKQ");

        // King = 10
        player.addCard(new Card(Rank.KING, Suit.HEARTS));

        // Queen = 10
        player.addCard(new Card(Rank.QUEEN, Suit.HEARTS));

        // So total now 20. The Ace is chosen as 1 so = 21
        Card ace = new Card(Rank.ACE, Suit.HEARTS);
        ace.setOverrideValue(1);
        player.addCard(ace);

        assertEquals(21, player.getScore(), "King (10) + Queen (10) + Ace (1) should be 21");
    }


    @Test
    void scenario8_testNineAndTwoAcesEquals21() {
        Player player = new Player("DoubleAce");

        player.addCard(new Card(Rank.NINE, Suit.CLUBS));
        assertEquals(9, player.getScore());

        // 1st Ace, user picks 11 so total should become 20
        Card ace1 = new Card(Rank.ACE, Suit.CLUBS);
        ace1.setOverrideValue(11);
        player.addCard(ace1);
        assertEquals(20, player.getScore(), "9 + Ace(11) => 20");

        // 2nd Ace, user picks 1 so total should become 21
        Card ace2 = new Card(Rank.ACE, Suit.DIAMONDS);
        ace2.setOverrideValue(1);
        player.addCard(ace2);

        assertEquals(21, player.getScore(), "9 + Ace(11) + Ace(1) => 21");
    }

}
