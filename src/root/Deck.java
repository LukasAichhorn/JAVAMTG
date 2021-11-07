package root;

import root.card.Card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Deck {
    private String name;

    public Deck(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static int maxSize = 5;

    public static int getMaxSize() {
        return maxSize;
    }

    public List<Card> getCards() {
        return cards;
    }

    private List<Card> cards = new ArrayList<>();

    public void shuffle() {
        Collections.shuffle(cards);
        System.out.printf("decks has been shuffled");
    }
    public void printContent(){
        System.out.printf("\nDeck: " + name + "-------------------");
        for (Card card: cards) {
           card.toString();
        }

    }
}

