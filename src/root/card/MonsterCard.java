package root.card;

import root.ElementType;
import root.MonsterType;

import javax.swing.text.Element;

public class MonsterCard extends Card {
    private MonsterType monsterType;

    public MonsterCard(String name, int damage, ElementType elementType, MonsterType monsterType) {
        super(name, damage, elementType);
        this.monsterType = monsterType;
    }


    public MonsterType getMonsterType() {
        return monsterType;
    }
    public int handleMonsterConflict(MonsterType enemyType){
        if(monsterType == MonsterType.GOBLIN){
            if(enemyType == MonsterType.DRAGON) return 0;
        }
        if(monsterType == MonsterType.DRAGON){
        if(enemyType == MonsterType.ELVE) return 0;
    }
        if(monsterType == MonsterType.ORK){
            if(enemyType == MonsterType.WIZARD) return 0;
        }

        return super.getDamage();
}
    @Override
    public String toString() {
        return  super.toString() + " => " + "MonsterCard{" +
                ", monsterType=" + monsterType +
                '}';
    }


    @Override
    public int resolveEnemyCard(Card enemy) {
            //monster vs spell
            if(enemy instanceof SpellCard) return handleEffectivness(enemy.getElementType());
            //monster vs Monster
            if(enemy instanceof MonsterCard){
                return handleMonsterConflict(((MonsterCard) enemy).getMonsterType());
            }
            return super.getDamage();
        }
}

