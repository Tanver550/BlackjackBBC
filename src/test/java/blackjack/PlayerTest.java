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
}
