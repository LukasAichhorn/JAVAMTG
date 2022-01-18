package models.DAOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.card.Card;
import models.card.MonsterCard;
import models.card.SpellCard;

import javax.management.monitor.MonitorSettingException;

@AllArgsConstructor
@Getter
@Setter

@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class CardDAO {
    private String id;
    private String name;
    private double damage;
    private boolean cardIsInDeck;

    public CardDAO() {
        super();
    }

    public Card importCard(CardDAO cardData) {
        Card currentCard;
        currentCard = (cardData.getName().contains("Spell"))
                ? new SpellCard(cardData)
                : new MonsterCard(cardData);
        return currentCard;
    }
}

