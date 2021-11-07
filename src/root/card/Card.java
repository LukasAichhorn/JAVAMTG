package root.card;

import root.ElementType;

public abstract class Card implements Conflict {
    private String name;
    private int damage;
    private ElementType elementType;

    public Card(String name, int damage, ElementType elementType) {
        this.name = name;
        this.damage = damage;
        this.elementType = elementType;
    }

    public int getDamage() {
        return damage;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public String getName() {
        return name;
    }

    public int handleEffectivness(ElementType element){
        if(elementType == ElementType.WATER){
            if(element == ElementType.WATER ) return damage;
            if(element == ElementType.FIRE ) return damage * 2;
            if(element == ElementType.NORMAL ) return damage /2;
        }
        if(elementType == ElementType.FIRE){
            if(element == ElementType.FIRE ) return damage;
            if(element == ElementType.NORMAL ) return damage * 2;
            if(element == ElementType.WATER ) return damage /2;
        }
        if(elementType == ElementType.NORMAL){
            if(element == ElementType.NORMAL) return damage;
            if(element == ElementType.WATER) return damage * 2;
            if(element == ElementType.FIRE) return damage /2;
        }
        return damage;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", damage=" + damage +
                ", elementType=" + elementType +
                '}';
    }
}
