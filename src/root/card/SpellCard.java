package root.card;

import root.ElementType;
import root.MonsterType;

public class SpellCard extends Card {
    public SpellCard(String name, int damage, ElementType elementType) {
        super(name, damage, elementType);
    }

    public int resolveEnemyCard(Card enemy){
        //spell vs Spell
        if(enemy instanceof SpellCard) return handleEffectivness(enemy.getElementType());
        //spell vs Monster
        if(enemy instanceof MonsterCard){
            if(((MonsterCard) enemy).getMonsterType() == MonsterType.KRAKEN) return -1;
            if(((MonsterCard) enemy).getMonsterType() == MonsterType.KNIGHT && super.getElementType() == ElementType.WATER) return Integer.MAX_VALUE;
        }
        return super.getDamage();
    }

    @Override
    public String toString() {
        return super.toString() + " => " + "SpellCard{" +

                '}';
    }
}
