package models;

import database.Query;
import helper.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.DAOs.CardDAO;
import models.card.Card;
import models.card.SpellCard;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter

public class Deck {
    private ArrayList<Card> deck;

    public Deck(){
        this.deck = new ArrayList<>();
    }

    public Deck(Player player, Logger logger) {
        //get cards of User
        try{
            this.deck = new ArrayList<>(Query.getDeckForUser(player, logger).stream().map(cardDAO -> cardDAO.importCard(cardDAO)).collect(Collectors.toList()));
        }
        catch(NullPointerException e) {
            logger.addLog("error in constructing Deck object " + e.getMessage());
            System.out.println("error in constructing Deck object ");
        }
    }
    public Card shuffleAndDraw(){
        Collections.shuffle(deck);
        return deck.get(0);
    }




    }

