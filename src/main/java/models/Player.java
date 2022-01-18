package models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import models.DAOs.CardDAO;
import models.card.Card;
import models.card.MonsterCard;
import models.card.SpellCard;

@Getter
@Setter
@ToString
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Player {
    private int userId;
    private String username;
    private String password;
    private String token;
    private int coins;
    private String personalName;
    private int elo;
    private String personalBio;
    private String personalImg;
    private int gamesWon;
    private int gamesLost;


public Player(){
    super();
}


public boolean canAffordPackage(){
    if(coins / 5 > 0 ){
        return true;
    }
    return false;
}
}
