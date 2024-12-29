package blackjack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
this is for testing everything player related. this is anyone who participates in the game.
ensures correct initialisation of their hands, scores and what not

 */

public class PlayerTest {
    @Test
    void testPlayerInitialState() {
        Player player = new Player("John");
        assertEquals("John", player.getName());
        assertEquals(0, player.getScore());
        assertTrue(player.getHand().isEmpty());
    }

    @Test
    void testPlayerAddCard() {
        Player player = new Player("John");
        Card card = new Card("Ace", "Spades", 11);
        player.addCard(card);
        assertEquals(11, player.getScore());
        assertEquals(1, player.getHand().size());
        assertEquals(card, player.getHand().get(0));
    }

    @Test
    void testPlayerBustWithAceAdjustment() {
        Player player = new Player("John");
        player.addCard(new Card("Ace", "Spades", 11));
        player.addCard(new Card("King", "Hearts", 10));
        player.addCard(new Card("Queen", "Diamonds", 10));
        assertEquals(21, player.getScore());
    }
}
