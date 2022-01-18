package helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import models.card.Card;
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Trade {
    String id;
    int ownerId;
    Card offeredCard;
    String wantedType;
    int minDamage;

}
