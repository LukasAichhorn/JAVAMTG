package root;

import root.card.Card;
import root.card.MonsterCard;
import root.card.SpellCard;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private Deck playerOneDeck;
    private  Deck playerTwoDeck;

    public Battle(Deck playerOneDeck, Deck playerTwoDeck) {
        this.playerOneDeck = playerOneDeck;
        this.playerTwoDeck = playerTwoDeck;
    }
public void handleFight(){
    System.out.println(" ");
    System.out.println("--------------- Battle Begins -------------------");
    System.out.println(" ");
        playerOneDeck.shuffle();
        playerTwoDeck.shuffle();
    for (int i = 0; i <= Deck.getMaxSize(); i++) {
        //take 2 cards and resolve conflict
        handleConflict(playerOneDeck.getCards().get(i),playerTwoDeck.getCards().get(i));
    }
    System.out.println(" ");
    System.out.println("Final score: P1: " + playerOneScore + "vs P2: " + playerTwoScore);
    System.out.println(" ");
}
private void handleConflict(Card p1c, Card p2c){
        int p1Atk = 0;
    int p2Atk = 0;
    System.out.println(p1c.toString() + " --> vs. <-- " + p2c.toString());
    p1Atk = p1c.resolveEnemyCard(p2c);
    p2Atk = p2c.resolveEnemyCard(p1c);
    System.out.println("P1:"+ p1Atk + " vs. P2: "+ p2Atk);
    if(p1Atk > p2Atk){
        playerOneScore += 1;
    }else{
        playerTwoScore += 1;
    }
}

    private void handleMonsterFight() {
    }
}

