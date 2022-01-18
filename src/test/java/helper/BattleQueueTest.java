package helper;

import junit.framework.TestCase;
import models.Player;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class BattleQueueTest extends TestCase {
    BattleQueue bq = BattleQueue.getInstance();
    Player testPlayer1 = new Player();
    Player testPlayer2 = new Player();

    @BeforeEach
    public void initEach(){
        bq.clearQueue();
    }
    public void testAddPlayerToQueue() {
        bq.clearQueue();
        testPlayer1.setUsername("testName");
        testPlayer1.setPassword("testPW");
    assertEquals(0,bq.getQueueSize());
    bq.addPlayerToQueue(testPlayer1);
    assertEquals(1,bq.getQueueSize());
    }

    public void testQueueIsFull() {
        assertFalse(bq.queueIsFull());
        bq.addPlayerToQueue(testPlayer1);
        assertFalse(bq.queueIsFull());
        bq.addPlayerToQueue(testPlayer1);
        assertTrue(bq.queueIsFull());
    }

    public void testQueueIsOpen() {
        bq.clearQueue();
        assertTrue(bq.queueIsOpen());
        bq.addPlayerToQueue(testPlayer1);
        assertTrue(bq.queueIsOpen());
        bq.addPlayerToQueue(testPlayer1);
        assertFalse(bq.queueIsOpen());
    }

    public void testBattleIsStarted() {


    }

    public void testBattleIsFinised() {

    }

    public void testStartBattle() {
    }

    public void testAddWriter() {
    }
}