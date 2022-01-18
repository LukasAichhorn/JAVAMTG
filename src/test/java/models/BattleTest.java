package models;

import junit.framework.TestCase;
import models.DAOs.CardDAO;
import models.card.Card;
import models.card.MonsterCard;
import models.card.RoundResult;
import models.card.SpellCard;

public class BattleTest extends TestCase {

    CardDAO DAO_Goblin = new CardDAO("testGoblin", "Goblin", 10, false);
    CardDAO DAO_Dragon = new CardDAO("testDragon", "Dragon", 10, false);
    CardDAO DAO_Ork = new CardDAO("testOrk", "Ork", 10, false);
    CardDAO DAO_Elve = new CardDAO("testelve", "Elve", 10, false);
    CardDAO DAO_Wizard = new CardDAO("testelve", "Wizard", 10, false);
    CardDAO DAO_WATERSPELL = new CardDAO("testWaterSpell", "WaterSpell", 10, false);
    CardDAO DAO_REGULARSPELL = new CardDAO("testRegularSpell", "RegularSpell", 10, false);
    CardDAO DAO_FIRESPELL = new CardDAO("testFireSpell", "FireSpell", 10, false);
    CardDAO DAO_MONSTER = new CardDAO("testMonster", "WaterGoblin", 10, false);

    Card goblin = new MonsterCard(DAO_Goblin);
    Card dragon = new MonsterCard(DAO_Dragon);
    Card ork = new MonsterCard(DAO_Ork);
    Card elve = new MonsterCard(DAO_Elve);
    Card wizard = new MonsterCard(DAO_Wizard);

    Card waterSpell = new SpellCard(DAO_WATERSPELL);
    Card regularSpell = new SpellCard(DAO_REGULARSPELL);
    Card fireSpell = new SpellCard(DAO_FIRESPELL);
    Card monster = new MonsterCard(DAO_MONSTER);

    Player p1 = new Player();
    Player p2 = new Player();
    Deck deckP1 = new Deck();
    Deck deckP2 = new Deck();
    Battle testBattle = new Battle();

    public void testFight() {

    }

    public void testEvaluateWinner() {
        //draw
        assertNull(testBattle.evaluateWinner(dragon,dragon));
        assertNull(testBattle.evaluateWinner(waterSpell,waterSpell));

    }

    public void testSwapCard() {
        testBattle.setCurrentPlayer(p1);
        testBattle.setOpponentPlayer(p2);
        deckP1.getDeck().clear();
        deckP2.getDeck().clear();
        deckP1.getDeck().add(dragon);
        deckP2.getDeck().add(goblin);
        RoundResult r1 = new RoundResult(p1, p2, goblin);
        testBattle.swapCard(deckP1, deckP2, r1);
        assertEquals(0, deckP2.getDeck().size());
        assertEquals(2, deckP1.getDeck().size());


    }

    public void testCheckForWin() {
        testBattle.setCurrentPlayer(p1);
        testBattle.setOpponentPlayer(p2);
        deckP1.getDeck().add(goblin);
        assertEquals(p1, testBattle.checkForWin(deckP1, deckP2));
        deckP2.getDeck().add(goblin);
        assertNull(testBattle.checkForWin(deckP1, deckP2));
        deckP1.getDeck().clear();
        assertEquals(p2, testBattle.checkForWin(deckP1, deckP2));


    }
}