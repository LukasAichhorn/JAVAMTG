package helper;

import lombok.AllArgsConstructor;
import lombok.Setter;
import models.Deck;
import models.card.Card;
import models.card.ElementType;
import models.card.MonsterCard;
import models.card.SpellCard;

import java.util.stream.Collectors;

@Setter
@AllArgsConstructor
public class Statistics {
    final Deck userDeck;
    final Logger logger;

    public void printStats(){
        logger.addLog("CardCount    -> " + userDeck.getDeck().size());
        logger.addLog("Monsters     -> " + monsterCount());
        logger.addLog("- Max dmg    -> " + maxDmg());
        logger.addLog("- Min dmg    -> " + minDmg());
        logger.addLog("- mean dmg    -> " + meanDmg());
        logger.addLog("--------------------------------");
        logger.addLog("Spells       -> " + spellCount());
        logger.addLog("- Regular    -> " + regularCount());
        logger.addLog("- Fire       -> " + fireCount());
        logger.addLog("- Water      -> " + waterCount());



    }
    public int monsterCount(){
        int count = 0;
       for(var card : userDeck.getDeck()){
           if(card instanceof MonsterCard) count +=1;
       }
       return count;
    }
    public int spellCount(){
        int count = 0;
        for(var card : userDeck.getDeck()){
            if(card instanceof SpellCard) count +=1;
        }
        return count;
    }
    public int fireCount(){
        int count = 0;
        for(var card : userDeck.getDeck()){
            if(card instanceof  SpellCard && card.getElementType() == ElementType.Fire) count +=1;
        }
        return count;
    }
    public int waterCount(){
        int count = 0;
        for(var card : userDeck.getDeck()){
            if(card instanceof  SpellCard && card.getElementType() == ElementType.Water) count +=1;
        }
        return count;    }
    public int regularCount(){
        int count = 0;
        for(var card : userDeck.getDeck()){
            if(card instanceof  SpellCard && card.getElementType() == ElementType.Regular) count +=1;
        }
        return count;    }
    public double meanDmg(){
        return (userDeck.getDeck().stream().map(Card::getDamage).reduce(0.0, Double::sum)/ userDeck.getDeck().size());
    }
    public double maxDmg(){
        return userDeck.getDeck().stream().map(Card::getDamage).max(Double::compare).get();
    }
    public double minDmg(){
        return userDeck.getDeck().stream().map(Card::getDamage).min(Double::compare).get();
    }


}

