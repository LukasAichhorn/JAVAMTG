package models.card;

import junit.framework.TestCase;
import models.DAOs.CardDAO;

public class MonsterCardTest extends TestCase {

    CardDAO DAO_Goblin = new CardDAO("testGoblin","Goblin",10,false);
    CardDAO DAO_Dragon = new CardDAO("testDragon","Dragon",10,false);
    CardDAO DAO_Ork = new CardDAO("testOrk","Ork",10,false);
    CardDAO DAO_Elve = new CardDAO("testelve","Elve",10,false);
    CardDAO DAO_FireElve = new CardDAO("FireElve","FireElve",10,false);
    CardDAO DAO_Wizard = new CardDAO("testelve","Wizard",10,false);
    Card goblin = new MonsterCard(DAO_Goblin);
    Card dragon = new MonsterCard(DAO_Dragon);
    Card ork = new MonsterCard(DAO_Ork);
    Card elve = new MonsterCard(DAO_Elve);
    Card fireElve = new MonsterCard(DAO_FireElve);
    Card wizard = new MonsterCard(DAO_Wizard);


    public void testHandleMonsterConflict(){
        assertEquals(0.0,goblin.handleConflict(dragon));
        assertEquals(10.0,dragon.handleConflict(goblin));
        //fire elve or not
        assertEquals(10.0,dragon.handleConflict(elve));
        assertEquals(0.0,dragon.handleConflict(fireElve));
        assertEquals(10.0,elve.handleConflict(dragon));
        assertEquals(0.0,ork.handleConflict(wizard));
        assertEquals(10.0,wizard.handleConflict(ork));




    }
}