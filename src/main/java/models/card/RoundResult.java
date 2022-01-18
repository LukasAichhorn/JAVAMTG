package models.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.Player;
@Getter
@Setter
@AllArgsConstructor
public class RoundResult
{
    private Player winner;
    private Player loser;
    private Card card;
}
