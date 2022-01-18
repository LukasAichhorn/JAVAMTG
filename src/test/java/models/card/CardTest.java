package models.card;

import junit.framework.TestCase;
import models.DAOs.CardDAO;

public class CardTest extends TestCase {
    CardDAO DAO_WATERSPELL = new CardDAO("testWaterSpell","WaterSpell",10,false);
    CardDAO DAO_REGULARSPELL = new CardDAO("testRegularSpell","RegularSpell",10,false);
    CardDAO DAO_FIRESPELL = new CardDAO("testFireSpell","FireSpell",10,false);
    CardDAO DAO_MONSTER = new CardDAO("testMonster","WaterGoblin",10,false);

    Card waterSpell = new SpellCard(DAO_WATERSPELL);
    Card regularSpell = new SpellCard(DAO_REGULARSPELL);
    Card fireSpell = new SpellCard(DAO_FIRESPELL);
    Card monster = new MonsterCard(DAO_MONSTER);


    public void testDecideConflictType() {
        assertEquals(ConflictType.vsMonster,monster.decideConflictType(monster,monster));
        assertEquals(ConflictType.vsSpell,monster.decideConflictType(monster,waterSpell));
    }

    public void testHandleEffectivness() {

        assertEquals(10.0, waterSpell.handleEffectivness(waterSpell));
        assertEquals(10.0 * 2, waterSpell.handleEffectivness(fireSpell));
        assertEquals(10.0 / 2, waterSpell.handleEffectivness(regularSpell));

        assertEquals(10.0, fireSpell.handleEffectivness(fireSpell));
        assertEquals(10.0 * 2, fireSpell.handleEffectivness(regularSpell));
        assertEquals(10.0 / 2, fireSpell.handleEffectivness(waterSpell));

        assertEquals(10.0, regularSpell.handleEffectivness(regularSpell));
        assertEquals(10.0 * 2, regularSpell.handleEffectivness(waterSpell));
        assertEquals(10.0 / 2, regularSpell.handleEffectivness(fireSpell));
    }
}