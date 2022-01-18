package routes.PUT;

import com.fasterxml.jackson.core.JsonProcessingException;
import database.Query;
import helper.Logger;
import models.DAOs.CardDAO;
import models.DAOs.UserInfoDAO;
import models.Player;
import models.RequestBuilder;
import routes.GET.GetRoutes;
import routes.HttpCodes;

import java.util.List;

import static server.MainServer.mapper;

public final class PutRoutes {

    public static HttpCodes manageDeckRoute(RequestBuilder req, Logger logger) throws JsonProcessingException {
        //decode Json
        String JSON = req.getData();
        final String token = req.getAuthToken();

        Player currentPlayer;
        String[] selectedCards;
        if (JSON == null) return HttpCodes.BAD_REQUEST;
        if (token == null) return HttpCodes.NOT_ALLOWED;
        if (token.equals("Basic admin-mtcgToken")) return HttpCodes.NOT_ALLOWED;
        if ((currentPlayer = Query.authByToken(token, logger)) == null) {
            logger.addLog("authentification token not correct");
            System.out.println("authentification token not correct");
            return HttpCodes.NOT_ALLOWED;
        }
        try {
            selectedCards = mapper.readValue(JSON, String[].class);
            if (selectedCards.length < 4) {
                logger.addLog("less than 4 cards selected");
                System.out.println("less than 4 cards selected");
                return HttpCodes.BAD_REQUEST;

            }
            if (Query.checkIfUserHasCompleteDeck(currentPlayer, logger)) {
                logger.addLog("User has already a complete Deck");
                System.out.println("User has already a complete Deck");
                return HttpCodes.BAD_REQUEST;
            }
            for (String cardID : selectedCards) {
                if (!Query.addCardToDeck(cardID, currentPlayer, logger)) return HttpCodes.BAD_REQUEST;
            }

        } catch (JsonProcessingException e) {
            logger.addLog(e.getMessage());
            System.out.println("errors encoding JSON in ManageDeckRoute");
            return HttpCodes.BAD_REQUEST;
        }


        return HttpCodes.SUCCESS;
    }

    public static HttpCodes editUserInfoRoute(Player loggedInUser, RequestBuilder req, Logger logger) throws JsonProcessingException {
        try {
            UserInfoDAO userInfo;
            String JSON = req.getData();
            userInfo = mapper.readValue(JSON, UserInfoDAO.class);
            if(!Query.editUserInfo(loggedInUser, userInfo, logger)){
                logger.addLog("Route error editUserInfoRoute");
                System.out.println("Route error editUserInfoRoute");
                return HttpCodes.BAD_REQUEST;
            }
            return HttpCodes.SUCCESS;

        } catch (JsonProcessingException e) {
            logger.addLog(e.getMessage());
            System.out.println("errors encoding JSON in editUserInfoRoute");
            return HttpCodes.BAD_REQUEST;
        }
    }


}
