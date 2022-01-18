package routes.POST;

import com.fasterxml.jackson.core.JsonProcessingException;
import database.Query;
import helper.Logger;
import models.Battle;
import models.DAOs.CardDAO;
import models.DAOs.TradeDAO;
import models.Player;
import models.RequestBuilder;
import routes.HttpCodes;

import java.io.BufferedWriter;
import java.sql.Connection;

import static server.MainServer.*;

public final class PostRoutes {

    public static HttpCodes createUserRoute(RequestBuilder req, Logger logger) throws JsonProcessingException {
        //decode Json
        final String JSON = req.getData();
        Player player;
        try {
            player = mapper.readValue(JSON, Player.class);
            Connection conn = db.connect();
            return Query.addUser(player, logger,conn);
        } catch (JsonProcessingException e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return HttpCodes.BAD_REQUEST;
        }

    }

    public static HttpCodes loginUserRoute(RequestBuilder req, Logger logger) throws JsonProcessingException {
        final String JSON = req.getData();
        Player player;
        String assignedToken;
        try {
            player = mapper.readValue(JSON, Player.class);
            if (Query.authenticateUser(player, logger)) {
                if (player.getToken() == null) {
                    logger.addLog("user has no token, so we give him one");
                    if ((assignedToken = Query.setTokenForUser(player, logger)) != null) {
                        player.setToken(assignedToken);
                    }
                }

            }
        } catch (JsonProcessingException e) {
            logger.addLog(e.getMessage());

        }
        return HttpCodes.SUCCESS;
    }

    public static HttpCodes createPackagesRoute(RequestBuilder req, Logger logger) throws JsonProcessingException {

        if (!req.getAuthToken().equals("Basic admin-mtcgToken")) {
            logger.addLog("Authorisation token not allowed");
            return HttpCodes.NOT_ALLOWED;
        }
        final String JSON = req.getData();
        try {
            CardDAO[] listOfCards = mapper.readValue(JSON, CardDAO[].class);
            Query.addCardsToLibary(listOfCards, logger);
            int newPackageId = Query.createNewPackageId(logger);
            if (newPackageId == -1) return HttpCodes.BAD_REQUEST;
            Query.mapContentToPackage(newPackageId, listOfCards, logger);


        } catch (JsonProcessingException e) {
            logger.addLog(e.getMessage());
        }

        return HttpCodes.SUCCESS;
    }

    public static HttpCodes acquirePackageRoute(RequestBuilder req, Logger logger) {
        String receivedToken = req.getAuthToken();
        Player currentPlayer;
        int packageId;
        if (receivedToken.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(receivedToken, logger)) == null) {
            logger.addLog("token not valid, please login");
            return HttpCodes.NOT_ALLOWED;
        }
        if (!currentPlayer.canAffordPackage()) {
            logger.addLog("not enough COINS!!!!!");
            return HttpCodes.BAD_REQUEST;
        }
        if ((packageId = Query.findNextPackageId(currentPlayer, logger)) == -1) {
            logger.addLog("no package is currently available");
            System.out.println("no package is currently available");
            return HttpCodes.BAD_REQUEST;
        }
        Query.acquirePackage(packageId,currentPlayer,logger);


        return HttpCodes.SUCCESS;
    }

    public static HttpCodes handleBattleRoute(RequestBuilder req, Logger logger, BufferedWriter writer) throws InterruptedException {
        final String token = req.getAuthToken();
        Player currentPlayer;
        if(token == null) return HttpCodes.NOT_ALLOWED;
        if(token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if((currentPlayer = Query.authByToken(token,logger)) == null){
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        if(currentPlayer.getUserId() == 0){
            logger.addLog("User does not exist, or maybe logged out");
            System.out.println("User does not exit, or maybe logged out");
            return  HttpCodes.BAD_REQUEST;
        }
        while(battleQueue.queueIsFull()){
            Thread.sleep(300);
            System.out.println("Player " + currentPlayer.getUsername() + " is waiting for because Queue is full ... ");
        }
         battleQueue.addPlayerToQueue(currentPlayer);
        battleQueue.addWriter(writer);
        System.out.println("Player " + currentPlayer.getUsername() + "got added to Queue");
        while(battleQueue.queueIsOpen()){
        Thread.sleep(300);
               System.out.println("Player " + currentPlayer.getUsername() + " is waiting for opponent ... ");
        }
        System.out.println("Player " + currentPlayer.getUsername() + "found oponnent");
        if(!battleQueue.battleIsStarted()){
            System.out.println("Player " + currentPlayer.getUsername() + "starts the battle");
            battleQueue.startBattle();
        }
        while(!battleQueue.battleIsFinised()){
            Thread.sleep(600);
            System.out.println("Player " + currentPlayer.getUsername() + "waits till battle is done");
        }
        return HttpCodes.SUCCESS;
    }

    public static HttpCodes insertTrade(RequestBuilder req, Logger logger) {
        final String token = req.getAuthToken();
        Player currentPlayer;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        try{
            TradeDAO tradeDAO=  mapper.readValue(req.getData(),TradeDAO.class);
            if(!Query.insertTrade(currentPlayer,tradeDAO,logger)){
                logger.addLog("error while inserting trade");
                System.out.println("error while inserting trade");
                return HttpCodes.BAD_REQUEST;
            };
        }
        catch (JsonProcessingException e) {
            logger.addLog(e.getMessage());
            return HttpCodes.BAD_REQUEST;
        }
    return HttpCodes.SUCCESS;
    }
}

