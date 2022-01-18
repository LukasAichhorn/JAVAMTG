package routes.GET;

import com.fasterxml.jackson.core.JsonProcessingException;
import database.Query;
import helper.Logger;
import helper.Statistics;
import helper.Trade;
import models.DAOs.CardDAO;
import models.DAOs.TradeDAO;
import models.Deck;
import models.Player;
import models.RequestBuilder;
import routes.HttpCodes;

import java.lang.annotation.Retention;
import java.util.List;
import java.util.stream.Collectors;

import static server.MainServer.mapper;

public final class GetRoutes {

    public static HttpCodes getUserCardLibary(RequestBuilder req, Logger logger) {
        //decode Json
        final String token = req.getAuthToken();
        Player currentPlayer;
        List<CardDAO> userLibrary;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        if ((userLibrary = Query.getCardLibraryForUser(currentPlayer, logger)) == null || userLibrary.isEmpty()) {
            logger.addLog("user has no cards in his library");
            System.out.println("user has no Cards in his library");
            return HttpCodes.BAD_REQUEST;
        }

        //list all cards
        logger.addLog("listinglibrary");
        for (CardDAO card : userLibrary) {
            String s = " Card-ID: " + card.getId() + " |  Card-Name: " + card.getName() + " |  Card-Dmg: " + card.getDamage() + " |  Card in Deck: " + card.isCardIsInDeck();
            logger.addLog(s);
            System.out.println(s);
        }
        logger.addLog("-----------------");
        System.out.println("-----------------");
        return HttpCodes.SUCCESS;
    }


    public static HttpCodes getUserDeck(RequestBuilder req, Logger logger) {
        final String token = req.getAuthToken();
        Player currentPlayer;
        List<CardDAO> userDeck;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        if ((userDeck = Query.getDeckForUser(currentPlayer, logger)) == null || userDeck.isEmpty()) {
            logger.addLog("user has no cards in his Deck");
            System.out.println("user has no Cards in his Deck");
            return HttpCodes.SUCCESS;
        }

        //list all cards
        logger.addLog("listingDeck");
        for (CardDAO card : userDeck) {
            String s = " Card-ID: " + card.getId() + " |  Card-Name: " + card.getName() + " |  Card-Dmg: " + card.getDamage() + " |  Card in Deck: " + card.isCardIsInDeck();
            logger.addLog(s);
            System.out.println(s);
        }
        logger.addLog("-----------------");
        System.out.println("-----------------");
        return HttpCodes.SUCCESS;
    }

    public static HttpCodes getUserInfoRoute(Player loggedInUser, Logger logger) {
        logger.addLog(loggedInUser.toString());
        System.out.println(loggedInUser);
        return HttpCodes.SUCCESS;
    }

    public static HttpCodes getStatRoute(RequestBuilder req, Logger logger) {
        //decode Json
        final String token = req.getAuthToken();
        Player currentPlayer;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        if (currentPlayer.getUserId() == 0) {
            logger.addLog("User does not exist, or maybe logged out");
            System.out.println("User does not exit, or maybe logged out");
            return HttpCodes.BAD_REQUEST;
        }
        String message = "-> Elo: " + currentPlayer.getElo() + " | Wins: " + currentPlayer.getGamesWon() + " | Loses: " + currentPlayer.getGamesLost();
        logger.addLog(message);
        System.out.println(message);
        return HttpCodes.SUCCESS;


    }

    public static HttpCodes getScoreBoard(RequestBuilder req, Logger logger) {

        final String token = req.getAuthToken();
        Player currentPlayer;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        if (currentPlayer.getUserId() == 0) {
            logger.addLog("User does not exist, or maybe logged out");
            System.out.println("User does not exit, or maybe logged out");
            return HttpCodes.BAD_REQUEST;
        }
        logger.addLog("---- SCOREBOARD ----");
        Query.getScoreBoard(logger);
        return HttpCodes.SUCCESS;
    }

    public static HttpCodes calculateDeckStatistics(RequestBuilder req, Logger logger) {
        final String token = req.getAuthToken();
        Player currentPlayer;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        if (!Query.checkIfUserHasCompleteDeck(currentPlayer, logger)) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.BAD_REQUEST;
        }
        var userDeck = new Deck(currentPlayer, logger);


        Statistics statistics = new Statistics(userDeck, logger);
        statistics.printStats();
        return HttpCodes.SUCCESS;


    }

    public static HttpCodes getOpenTrades(RequestBuilder req, Logger logger) {
        System.out.println("getOpenTrades");
        final String token = req.getAuthToken();
        Player currentPlayer;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        List<Trade> openTrade;
        if ((openTrade = Query.getOpenTrades(logger)) == null || openTrade.isEmpty()) {
            logger.addLog("no trades are open");
            System.out.println("no trades are open");
            return HttpCodes.BAD_REQUEST;
        }
        for (var trade : openTrade) {
            logger.addLog("printing Trade");
            logger.addLog(trade.toString());
        }
        return HttpCodes.SUCCESS;
    }


}

