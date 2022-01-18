package models.DAOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class TradeDAO {
    String id;
    String cardToTrade;
    String type;
    int minimumDamage;

    public TradeDAO() {
        super();
    }
}
