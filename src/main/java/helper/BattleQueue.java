package helper;

import lombok.Getter;
import lombok.Setter;
import models.Battle;
import models.Player;

import java.io.BufferedWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class BattleQueue {
    private static final List<Player> queueList = new ArrayList<>();
    private static final BattleQueue battleQueue = new BattleQueue();
    private List<BufferedWriter> writers = new ArrayList<>();



    int accessCounter;
    private static final Battle battle = new Battle();

    public static BattleQueue getInstance(){
        return battleQueue;
    }
    public  synchronized void addPlayerToQueue(Player player){
        if(queueIsOpen()) queueList.add(player);
        System.out.println("added player to queue");
    }
    public synchronized  int getQueueSize(){
        return queueList.size();
    }
    public boolean queueIsFull(){
        if(queueList.size() == 2) return true;
        return false;
    }
    public boolean queueIsOpen(){
        if(queueList.size()!= 2 ) return true;
        //queueList.clear();
        return false;
    }
    public synchronized void clearQueue(){
        accessCounter = 0;
        queueList.clear();
    }
    public boolean battleIsStarted(){
        return battle.started;
    }
    public boolean battleIsFinised(){
        return battle.finished;
    }
    public synchronized void startBattle(){
        battle.setCurrentPlayer(queueList.get(0));
        battle.setOpponentPlayer(queueList.get(1));
        battle.fight(writers);
        clearQueue();

    }
    public synchronized  void addWriter(BufferedWriter playerWriter){
        writers.add(playerWriter);
    }

}
