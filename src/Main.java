import root.*;
import root.card.Card;
import root.card.MonsterCard;
import root.card.SpellCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {


        List<Card> cards = new ArrayList<Card>();

        //create some Monster
        for (MonsterType type: MonsterType.values()) {
            cards.add(new MonsterCard("M-"+type.toString(),5,ElementType.FIRE,type));
        }
        for (ElementType type: ElementType.values()) {
            cards.add(new SpellCard("S-"+type.toString(),5,type));
        }
        cards.toString();


        //create 2 Decks
        Random rand = new Random();
        Deck deck_01 = new Deck("Deck01");
        Deck deck_02 = new Deck("Deck02");

        for (int i = 0; i <= Deck.getMaxSize(); i++) {
            deck_01.getCards().add(cards.get(rand.nextInt(cards.size())));
            deck_02.getCards().add(cards.get(rand.nextInt(cards.size())));
        }

        deck_01.printContent();
        deck_02.printContent();
        Battle battle = new Battle(deck_01,deck_02);
        battle.handleFight();
        

    }
}
