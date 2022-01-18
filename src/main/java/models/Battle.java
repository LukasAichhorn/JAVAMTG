package models;

import database.Query;
import helper.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import models.card.*;
import org.mockito.internal.matchers.InstanceOf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Battle {
    private Player currentPlayer;
    private  Player opponentPlayer;
    private final Logger battleLogger = new Logger();
    public boolean started = false;
    public boolean finished = false;


    public void fight(List<BufferedWriter> writer) {
        finished = false;
        started = true;
        //create Decks
        Deck currentPlayerDeck = new Deck(currentPlayer, battleLogger);
        Deck opponentPlayerDeck = new Deck(opponentPlayer, battleLogger);
        System.out.println(currentPlayerDeck.hashCode());
        System.out.println(opponentPlayerDeck.hashCode());

        Player winner = null;
        battleLogger.addLog("--- BATTLE is Starting");
        for (int i = 0; i < 100; i++) {

            if ((winner = checkForWin(currentPlayerDeck, opponentPlayerDeck)) != null) break;
            battleLogger.addLog("\n");
            battleLogger.addLog("----- START ROUND " + i+ "/100");
            RoundResult roundResult = evaluateWinner(currentPlayerDeck.shuffleAndDraw(), opponentPlayerDeck.shuffleAndDraw());
            if (roundResult == null) continue;
            swapCard(currentPlayerDeck, opponentPlayerDeck, roundResult);
            battleLogger.addLog("--- END ROUND ");
        }
        if(winner == null) battleLogger.addLog("this battle resulted in a Draw");
        if(winner == currentPlayer) Query.addWin(currentPlayer,battleLogger);
        if(winner == opponentPlayer) Query.addLoss(currentPlayer,battleLogger);
        battleLogger.addLog("--- BATTLE has been completed");
        for(var msg : battleLogger.getLogs()) {
            System.out.println(msg);
        }
        for(var w : writer){
            battleLogger.printLogs(w);
        }
        started = false;
        finished = true;
    }

   public  RoundResult evaluateWinner(Card cpC, Card opC) {
        battleLogger.addLog("-------------------------------");
        battleLogger.addLog("---- " + cpC.getName() +" | "+ cpC.getElementType() +" | "+ cpC.getDamage());
        battleLogger.addLog("---- vs.  ");
        battleLogger.addLog("---- " + opC.getName() +" | "+ opC.getElementType() +" | "+ opC.getDamage());
        battleLogger.addLog("-------------------------------");
        double cpCDmg = cpC.handleConflict(opC);
        double opCDmg = opC.handleConflict(cpC);
        battleLogger.addLog("---- my dmg -> " + cpCDmg);
        battleLogger.addLog("---- op dmg -> " + opCDmg);
        if (cpCDmg == opCDmg) {
            battleLogger.addLog("---- evaluate winner -> draw");
            return null;
        }
        if (cpCDmg > opCDmg) {
            battleLogger.addLog("---- evaluate winner -> " + currentPlayer.getUsername());
            return new RoundResult(currentPlayer, opponentPlayer, opC);
        }
        battleLogger.addLog("---- evaluate winner -> " + opponentPlayer.getUsername());
        return new RoundResult(opponentPlayer, currentPlayer, cpC);
    }

    public void swapCard(Deck currentPlayerDeck, Deck opponentPlayerDeck, RoundResult roundResult) {
        Deck winnerDeck = (roundResult.getWinner() == currentPlayer)
                ? currentPlayerDeck
                : opponentPlayerDeck;
        Deck loserDeck = (roundResult.getLoser() == currentPlayer)
                ? currentPlayerDeck
                : opponentPlayerDeck;
        battleLogger.addLog("---- winner deck size: " + winnerDeck.getDeck().size());
        battleLogger.addLog("---- loser deck size: " + loserDeck.getDeck().size());
        winnerDeck.getDeck().add(roundResult.getCard());
        loserDeck.getDeck().remove(roundResult.getCard());
        battleLogger.addLog("---- we are swapping cards");
        battleLogger.addLog("---- winner deck size: " + winnerDeck.getDeck().size());
        battleLogger.addLog("---- loser deck size: " + loserDeck.getDeck().size());
    }

    public Player checkForWin(Deck currentPlayerDeck, Deck opponentPlayerDeck) {
        int cl = currentPlayerDeck.getDeck().size();
        int ol = opponentPlayerDeck.getDeck().size();
        if (cl == 0) return opponentPlayer;
        if (ol == 0) return currentPlayer;
        return null;
    }
}