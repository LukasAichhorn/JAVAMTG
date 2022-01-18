package helper;

import junit.framework.TestCase;
import models.DAOs.CardDAO;
import models.Deck;
import models.card.Card;
import models.card.MonsterCard;
import models.card.SpellCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsTest {
    Deck testDeck = new Deck();
    CardDAO DAO_Goblin = new CardDAO("testGoblin", "Goblin", 20, false);
    CardDAO DAO_Dragon = new CardDAO("testDragon", "Dragon", 10, false);
    CardDAO DAO_WATERSPELL = new CardDAO("testWaterSpell", "WaterSpell", 10, false);
    CardDAO DAO_REGULARSPELL = new CardDAO("testRegularSpell", "RegularSpell", 10, false);
    CardDAO DAO_FIRESPELL = new CardDAO("testFireSpell", "FireSpell", 10, false);

    Card goblin = new MonsterCard(DAO_Goblin);
    Card dragon = new MonsterCard(DAO_Dragon);

    Card waterSpell = new SpellCard(DAO_WATERSPELL);
    Card regularSpell = new SpellCard(DAO_REGULARSPELL);
    Card fireSpell = new SpellCard(DAO_FIRESPELL);
    Logger testLogger = new Logger();
    Statistics testStatistics = new Statistics(testDeck,testLogger);
    @BeforeEach
    public void initEach(){
        testDeck.getDeck().clear();
        testDeck.getDeck().add(waterSpell);
        testDeck.getDeck().add(fireSpell);
        testDeck.getDeck().add(regularSpell);

        testDeck.getDeck().add(goblin);
        testDeck.getDeck().add(dragon);
    }
@Test
    public void testPrintStats() {
        testStatistics.printStats();
        assertEquals(10,testLogger.getLogs().size());
    }
    @Test
    public void testMonsterCount() {
        assertEquals(2,testStatistics.monsterCount());
    }
    @Test
    public void testSpellCount() {
        assertEquals(3,testStatistics.spellCount());
    }
    @Test
    public void testFireCount() {
        assertEquals(1,testStatistics.fireCount());
    }
    @Test
    public void testWaterCount() {
        assertEquals(1,testStatistics.waterCount());
    }
    @Test
    public void testRegularCount() {
        assertEquals(1,testStatistics.regularCount());
    }
    @Test
    public void testMeanDmg() {
        System.out.println(testStatistics.monsterCount());
        assertEquals(12,testStatistics.meanDmg());
    }
    @Test
    public void testMaxDmg() {
        assertEquals(20,testStatistics.maxDmg());
    }
    @Test
    public void testMinDmg() {
        assertEquals(10,testStatistics.minDmg());
    }
}