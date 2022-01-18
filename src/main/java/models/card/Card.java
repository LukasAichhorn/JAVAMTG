package models.card;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public abstract class Card implements ConflictHandler {
    private String id;
    private String name;
    private double damage;
    private ElementType elementType;

    public ConflictType decideConflictType(Card cpC, Card opC) {
        if (cpC instanceof MonsterCard && opC instanceof MonsterCard) {
            return ConflictType.vsMonster;
        }
        return ConflictType.vsSpell;
    }

    public final double handleEffectivness(Card opC){
        ElementType element = opC.getElementType();


        if(elementType == ElementType.Water){
            if(element == ElementType.Water ) return damage;
            if(element == ElementType.Fire) return damage * 2;
            if(element == ElementType.Regular) return damage /2;
        }
        if(elementType == ElementType.Fire){
            if(element == ElementType.Fire ) return damage;
            if(element == ElementType.Regular ) return damage * 2;
            if(element == ElementType.Water) return damage /2;
        }
        if(elementType == ElementType.Regular){
            if(element == ElementType.Regular) return damage;
            if(element == ElementType.Water) return damage * 2;
            if(element == ElementType.Fire) return damage /2;
        }
        return damage;
    }

}
