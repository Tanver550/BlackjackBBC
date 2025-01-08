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
        // 3) Add Ace sp We'll pick 1 from userInput so final total = 21
        player.addCard(new Card(Rank.ACE, Suit.SPADES));

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

        // King is 10
        player.addCard(new Card(Rank.KING, Suit.HEARTS));

        // Queen is 10
        player.addCard(new Card(Rank.QUEEN, Suit.HEARTS));


        // So total now 20, Ace, user picks 1 so 21
        player.addCard(new Card(Rank.ACE, Suit.HEARTS));

        assertEquals(21, player.getScore(), "King (10) + Queen (10) + Ace (1) should be 21");
    }

    @Test
    void scenario8_testNineAndTwoAcesEquals21() {
        Player player = new Player("DoubleAce");

        player.addCard(new Card(Rank.NINE,Suit.CLUBS));
        assertEquals(9, player.getScore());

        // first Ace pick 11  total 20
        player.addCard(new Card(Rank.ACE, Suit.CLUBS));
        assertEquals(20, player.getScore());

        // second Ace pick 1  total 21
        player.addCard(new Card(Rank.ACE, Suit.DIAMONDS));

        assertEquals(21, player.getScore(), "9 + Ace(11) + Ace(1) should result in 21");
    }

}
