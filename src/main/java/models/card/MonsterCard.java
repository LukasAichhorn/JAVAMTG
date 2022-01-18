package models.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import models.DAOs.CardDAO;

@Getter
@Setter
@ToString
public class MonsterCard extends Card {

   private MonsterType monsterType;



   public MonsterCard(CardDAO cardData) {
      var s = cardData.getName().split("(?<!^)(?=[A-Z])");
      this.setId(cardData.getId());
      this.setName(cardData.getName());
      this.setElementType((s.length == 2) ? ElementType.valueOf(s[0]) : null);
      this.monsterType = (s.length == 2) ? MonsterType.valueOf(s[1]) : MonsterType.valueOf(s[0]);
      this.setDamage(cardData.getDamage());
   }

    public MonsterCard() {

    }


   @Override
   public double handleConflict(Card opC) {
      ConflictType conflictType = super.decideConflictType(this,opC);
      double dmg=0;
      switch (conflictType){
         case vsMonster -> dmg = handleMonsterConflict( (MonsterCard) opC);
         case vsSpell -> dmg = handleSpell((SpellCard) opC);
      }
      return dmg;
   }

   private double handleMonsterConflict(MonsterCard opC) {
      if (MonsterType.Goblin == monsterType) {
         if (opC.getMonsterType() == MonsterType.Dragon) return 0;
      }
      if (MonsterType.Dragon == monsterType) {
         if (opC.getMonsterType() == MonsterType.Elve && opC.getElementType() == ElementType.Fire) return 0;
      }
      if (MonsterType.Ork == monsterType) {
         if (opC.getMonsterType() == MonsterType.Wizard) return 0;
      }

      return super.getDamage();
   }
   private double handleSpell(SpellCard opC){
      if(monsterType == MonsterType.Knight && opC.getElementType() == ElementType.Water)return 0;
      return handleEffectivness(opC);
   }


}
