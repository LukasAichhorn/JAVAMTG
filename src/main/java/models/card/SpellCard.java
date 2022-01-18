package models.card;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import models.DAOs.CardDAO;

@Getter
@Setter
@ToString
public class SpellCard extends Card {

    public SpellCard(CardDAO cardData) {
        var s = cardData.getName().split("(?<!^)(?=[A-Z])");
        this.setId(cardData.getId());
        this.setName(cardData.getName());
        this.setElementType(ElementType.valueOf(s[0]));
        this.setDamage(cardData.getDamage());
    }

    @Override
    public double handleConflict(Card opC) {
        ConflictType conflictType = super.decideConflictType(this, opC);
        double dmg = 0;
        switch (conflictType) {
            case vsMonster -> dmg =   handleMonsterConflict( (MonsterCard) opC);
                    case vsSpell -> dmg = handleEffectivness(opC);
        }
        return dmg;
    }
    private double handleMonsterConflict(MonsterCard opC){
        if(opC.getMonsterType() == MonsterType.Kraken) return 0;
        return handleEffectivness(opC);
    }
}



