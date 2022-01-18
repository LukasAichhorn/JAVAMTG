package database;

import helper.Logger;
import helper.Trade;
import models.DAOs.CardDAO;
import models.DAOs.TradeDAO;
import models.DAOs.UserInfoDAO;
import models.Player;
import models.RequestBuilder;
import models.card.Card;
import routes.HttpCodes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static server.MainServer.db;


public final class Query {
    public static HttpCodes addUser(Player player, Logger logger, Connection conn) {
        String SQL = "INSERT INTO USERS (USERNAME, PASSWORD) VALUES (?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getPassword());
            int row = preparedStatement.executeUpdate();
            // rows affected
            System.out.println(row); //1
            System.out.println("User has been created");
            logger.addLog("User has been created");
            conn.close();
            return HttpCodes.SUCCESS;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());

            return HttpCodes.BAD_REQUEST;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return HttpCodes.BAD_REQUEST;
        }
    }

    public static boolean authenticateUser(Player player, Logger logger) {
        String SQL = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setString(1, player.getUsername());
            preparedStatement.setString(2, player.getPassword());
            ResultSet queryResult = preparedStatement.executeQuery();
            //System.out.println("User exists in DB");
            while (queryResult.next()) {
                int user_id = queryResult.getInt("user_id");
                player.setUserId(user_id);
                System.out.println("we fetched user id: " + user_id);
                int coins = queryResult.getInt("coins");
                System.out.println("we fetched user coins: " + coins);
                String token = queryResult.getString("token");
                System.out.println("we fetched user token: " + token);
                player.setToken(token);
                player.setCoins(coins);
            }
            preparedStatement.close();
            queryResult.close();
            conn.close();
            logger.addLog("user is logged in");
            return true;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String setTokenForUser(Player player, Logger logger) {


        String SQL = "UPDATE users SET TOKEN = ? WHERE user_id = ?;";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            String token = "Basic " + player.getUsername() + "-mtcgToken";
            preparedStatement.setString(1, token);
            preparedStatement.setInt(2, player.getUserId());

            int row = preparedStatement.executeUpdate();
            // rows affected
            System.out.println("executeUpdate response : " + row); //1
            conn.close();
            return token;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());

            return null;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addCardsToLibary(CardDAO[] listOfCards, Logger logger) {
        String SQL = "INSERT INTO CARDLIBRARY (CARD_ID, CARD_NAME,CARD_DAMAGE) VALUES (?,?,?) ";


        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            for (CardDAO card : listOfCards) {
                preparedStatement.clearParameters();
                preparedStatement.setString(1, card.getId());
                preparedStatement.setString(2, card.getName());
                preparedStatement.setDouble(3, card.getDamage());
                preparedStatement.addBatch();
            }
            int[] row = preparedStatement.executeBatch();
            System.out.println("executeUpdate response : " + row); //1
            logger.addLog("adding card to library");


            // rows affected
            System.out.println("executeUpdate response : " + row); //1
            conn.close();

        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    public static int createNewPackageId(Logger logger) {
        int packageId = -1;
        String SQL = "INSERT INTO AVAILABLEPACKAGES DEFAULT VALUES returning package_id";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                packageId = res.getInt(1);
                logger.addLog("adding new Package id");
                System.out.println("new package id = " + packageId);
            }
            preparedStatement.close();
            res.close();
            conn.close();
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
        }

        return packageId;
    }

    public static void mapContentToPackage(int currentPackageId, CardDAO[] listOfCards, Logger logger) {
        String SQL = "INSERT INTO PACKAGESCONTENT (PACKAGE_ID, CARD_ID) VALUES (?,?) ";

        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            for (CardDAO card : listOfCards) {
                preparedStatement.clearParameters();
                preparedStatement.setInt(1, currentPackageId);
                preparedStatement.setString(2, card.getId());
                preparedStatement.addBatch();

            }
            int[] row = preparedStatement.executeBatch();
            System.out.println("executeUpdate response : " + row); //1
            logger.addLog("linking Card ID to Package");
            conn.close();

        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
        }


    }

    public static Player authByToken(String receivedToken, Logger logger) {
        String SQL = "SELECT * FROM users WHERE token = ?";
        Player player = new Player();
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setString(1, receivedToken);
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next()) {
                player.setUserId(queryResult.getInt("user_id"));
                player.setUsername(queryResult.getString("username"));
                player.setToken(queryResult.getString("token"));
                player.setCoins(queryResult.getInt("coins"));
                player.setPersonalBio(queryResult.getString("personal_bio"));
                player.setPersonalImg(queryResult.getString("personal_img"));
                player.setPersonalName(queryResult.getString("personal_name"));
                player.setElo(queryResult.getInt("elo"));
                player.setGamesWon(queryResult.getInt("games_won"));
                player.setGamesLost(queryResult.getInt("games_lost"));
            }
            System.out.println(player);
            preparedStatement.close();
            queryResult.close();
            conn.close();
            return player;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return null;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

    public static boolean checkForAvailablePackages(Logger logger) {
        int count = 0;
        String SQL = "SELECT COUNT(*) FROM availablepackages";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {

            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                count = queryResult.getInt(1);
            }
            preparedStatement.close();
            queryResult.close();
            conn.close();
            logger.addLog("user is logged in");
            return count > 0;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;

        }
    }

    public static int findNextPackageId(Player currentPlayer, Logger logger) {
        int nextPackageId = -1;
        String SQL = "SELECT MIN(package_id) FROM availablepackages";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {

            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                nextPackageId = queryResult.getInt(1);
            }
            preparedStatement.close();
            queryResult.close();
            conn.close();
            logger.addLog("user is logged in");
            return nextPackageId;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return -1;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return -1;

        }

    }

    public static void acquirePackage(int packageId, Player currentPlayer, Logger logger) {
        List<String> packageContent;
        if (!Query.subtractCoins(currentPlayer, logger)) {
            logger.addLog("probems with subtracting coins");
            return;
        }
        if ((packageContent = Query.getPackageContent(packageId, logger)).size() == 0) {
            logger.addLog("package is empty");
            return;
        }

        if (!Query.addCardsToUserLibrary(packageContent, currentPlayer, logger)) {
            logger.addLog("problem with adding cards to user library");
            return;
        }
        Query.deletePackage(packageId, logger);
    }

    private static boolean subtractCoins(Player player, Logger logger) {
        String SQL = "UPDATE users SET  coins= coins - 5 WHERE username = ?";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setString(1, player.getUsername());

            int row = preparedStatement.executeUpdate();
            if (row == 1) logger.addLog("Coins have been updated");
            if (row == 0) logger.addLog("there was an error updating your Elo");

            preparedStatement.close();
            conn.close();

        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void deletePackage(int packageId, Logger logger) {
        String SQL = " DELETE FROM availablepackages WHERE package_id = ?";
        List<String> packageContent = new ArrayList<>();
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setInt(1, packageId);
            int row = preparedStatement.executeUpdate();

            preparedStatement.close();
            conn.close();
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();

        }
    }

    private static boolean addCardsToUserLibrary(List<String> packageContent, Player currentPlayer, Logger logger) {
        String SQL = "INSERT INTO personal_libary (USER_ID, CARD_ID) VALUES (?,?) ";

        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            for (String cardId : packageContent) {
                preparedStatement.clearParameters();
                preparedStatement.setInt(1, currentPlayer.getUserId());
                preparedStatement.setString(2, cardId);
                preparedStatement.addBatch();

            }
            int[] row = preparedStatement.executeBatch();
            System.out.println("executeUpdate response : " + row); //1
            logger.addLog("linking Card ID to User Library");
            conn.close();
            return true;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;
        }


    }

    private static List<String> getPackageContent(int packageId, Logger logger) {
        String SQL = "SELECT * FROM packagescontent WHERE package_id = ?";
        List<String> packageContent = new ArrayList<>();
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setInt(1, packageId);
            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                String cardId = queryResult.getString("card_id");
                packageContent.add(cardId);
            }

            preparedStatement.close();
            queryResult.close();
            conn.close();
            return packageContent;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return packageContent;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return packageContent;

        }
    }

    public static List<CardDAO> getCardLibraryForUser(Player currentPlayer, Logger logger) {
        String SQL = "SELECT \n" +
                "cardlibrary.card_id,\n" +
                "cardlibrary.card_name,\n" +
                "cardlibrary.card_damage,\n" +
                "personal_libary.card_is_in_deck\n" +
                "FROM cardlibrary \n" +
                "INNER JOIN\n" +
                "\tpersonal_libary\n" +
                "ON\n" +
                "personal_libary.card_id = cardlibrary.card_id\n" +
                "WHERE personal_libary.user_id = ?";

        List<CardDAO> cardList = new ArrayList<>();
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setInt(1, currentPlayer.getUserId());
            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                String cardId = queryResult.getString("card_id");
                String cardName = queryResult.getString("card_name");
                double cardDamage = queryResult.getDouble("card_damage");
                boolean inDeck = queryResult.getBoolean("card_is_in_deck");
                cardList.add(new CardDAO(cardId, cardName, cardDamage, inDeck));
            }

            preparedStatement.close();
            queryResult.close();
            conn.close();
            return cardList;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return null;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

    public static ArrayList<CardDAO> getDeckForUser(Player currentPlayer, Logger logger) {
        String SQL = "SELECT \n" +
                "cardlibrary.card_id,\n" +
                "cardlibrary.card_name,\n" +
                "cardlibrary.card_damage,\n" +
                "personal_libary.card_is_in_deck\n" +
                "FROM cardlibrary \n" +
                "INNER JOIN\n" +
                "\tpersonal_libary\n" +
                "ON\n" +
                "personal_libary.card_id = cardlibrary.card_id\n" +
                "WHERE personal_libary.user_id = ? and personal_libary.card_is_in_deck = ?";

        ArrayList<CardDAO> cardList = new ArrayList<>();
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setInt(1, currentPlayer.getUserId());
            preparedStatement.setBoolean(2, true);
            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                String cardId = queryResult.getString("card_id");
                String cardName = queryResult.getString("card_name");
                double cardDamage = queryResult.getDouble("card_damage");
                boolean inDeck = queryResult.getBoolean("card_is_in_deck");
                cardList.add(new CardDAO(cardId, cardName, cardDamage, inDeck));
            }

            preparedStatement.close();
            queryResult.close();
            conn.close();
            return cardList;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return null;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

    public static boolean addCardToDeck(String cardID, Player player, Logger logger) {

        String SQL = "UPDATE personal_libary SET card_is_in_deck = ? WHERE user_id = ? AND card_id = ?";

        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, player.getUserId());
            preparedStatement.setString(3, cardID);

            int row = preparedStatement.executeUpdate();
            if (row == 1) logger.addLog("cardID: " + cardID + "-> added to users Deck");
            if (row == 0) logger.addLog("cardID: " + cardID + "-> is not in users libary");

            preparedStatement.close();
            conn.close();
            return true;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;

        }
    }

    public static boolean checkIfUserHasCompleteDeck(Player currentPlayer, Logger logger) {

        int cardCount = 0;
        String SQL = "SELECT COUNT(*) FROM personal_libary WHERE user_id = ? AND card_is_in_deck = ?";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setInt(1, currentPlayer.getUserId());
            preparedStatement.setBoolean(2, true);

            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                cardCount = queryResult.getInt(1);
            }
            preparedStatement.close();
            queryResult.close();
            conn.close();
            return cardCount == 4;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;

        }
    }

    public static boolean editUserInfo(Player loggedInUser, UserInfoDAO userInfo, Logger logger) {
        String SQL = "UPDATE users SET personal_name = ?, personal_bio = ?,personal_img=? WHERE user_id = ?";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setString(1, userInfo.getName());
            preparedStatement.setString(2, userInfo.getBio());
            preparedStatement.setString(3, userInfo.getImage());
            preparedStatement.setInt(4, loggedInUser.getUserId());
            int row = preparedStatement.executeUpdate();
            if (row == 1) logger.addLog("Personal information has been updates");
            if (row == 0) logger.addLog("there was an error in updating your personal information");

            preparedStatement.close();
            conn.close();
            return true;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;

        }
    }

    public static void getScoreBoard(Logger logger) {
        String SQL = "SELECT * FROM users ORDER BY elo DESC";
        int place = 1;
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            ResultSet queryResult = preparedStatement.executeQuery();
            while (queryResult.next()) {
                String username = queryResult.getString("username");
                int elo = queryResult.getInt("elo");
                int wins = queryResult.getInt("games_won");
                int losses = queryResult.getInt("games_lost");
                String message = place + ". -> " + username + " elo: " + elo + " Wins: " + wins + " Losses: " + losses;
                logger.addLog(message);
                System.out.println(message);
                place = place + 1;
            }
            preparedStatement.close();
            queryResult.close();
            conn.close();
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();

        }
    }

    public static void addWin(Player player, Logger logger) {
        String SQL = "UPDATE users SET  elo= elo + 10 ?, games_won= games_won +1, WHERE username = ?";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setString(1, player.getUsername());

            int row = preparedStatement.executeUpdate();
            if (row == 1) logger.addLog("Elo has been updated");
            if (row == 0) logger.addLog("there was an error updating your Elo");

            preparedStatement.close();
            conn.close();
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();

        }
    }

    public static void addLoss(Player player, Logger logger) {
        String SQL = "UPDATE users SET  elo= elo - 10 ?, games_lost= games_lost + 1, WHERE username = ?";
        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            preparedStatement.setString(1, player.getUsername());

            int row = preparedStatement.executeUpdate();
            if (row == 1) logger.addLog("Elo has been updated");
            if (row == 0) logger.addLog("there was an error updating your Elo");

            preparedStatement.close();
            conn.close();
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();

        }
    }

    public static List<Trade> getOpenTrades(Logger logger) {
        String SQL = "SELECT * FROM open_trades";

        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
            ResultSet queryResult = preparedStatement.executeQuery();
            List<Trade> temp = new ArrayList<>();
            while (queryResult.next()) {
                String tradeId = queryResult.getString("trade_id");
                int ownerId = queryResult.getInt("user_id");
                String cardId = queryResult.getString("card_to_trade");
                Card cardToTrade = Query.getSingleCard(cardId, logger);
                String type = queryResult.getString("card_type");
                int dmg = queryResult.getInt("minimum_dmg");

                temp.add(new Trade(tradeId, ownerId, cardToTrade, type, dmg));

            }
            System.out.println("temp: "+ temp);
            preparedStatement.close();
            queryResult.close();
            conn.close();
            return temp;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return null;

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static Card getSingleCard(String id, Logger logger) {
        String SQL = "SELECT * From cardlibrary Where card_id = ?";

        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {

            preparedStatement.setString(1, id);
            ResultSet queryResult = preparedStatement.executeQuery();
            List<CardDAO> cardList = new ArrayList<>();
            while (queryResult.next()) {
                String cardId = queryResult.getString("card_id");
                String cardName = queryResult.getString("card_name");
                double cardDamage = queryResult.getDouble("card_damage");
                cardList.add(new CardDAO(cardId, cardName, cardDamage, false));
            }

            preparedStatement.close();
            queryResult.close();
            conn.close();
            return cardList.get(0).importCard(cardList.get(0));
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return null;
        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return null;

        }
    }

    public static boolean insertTrade(Player currentPlayer,TradeDAO tradeDAO, Logger logger) {
        String SQL = "INSERT INTO open_trades (trade_id, card_to_trade,card_type,minimum_dmg,user_id) VALUES (?,?,?,?,?)";

        try (Connection conn = db.connect(); PreparedStatement preparedStatement = conn.prepareStatement(SQL)) {
                preparedStatement.setString(1,tradeDAO.getId() );
                preparedStatement.setString(2, tradeDAO.getCardToTrade());
                preparedStatement.setString(3, tradeDAO.getType());
                preparedStatement.setInt(4, tradeDAO.getMinimumDamage());
                preparedStatement.setInt(5, currentPlayer.getUserId());

            int row = preparedStatement.executeUpdate();
            System.out.println("executeUpdate response : " + row); //1
            logger.addLog("Added trade to available trades");
            conn.close();
            return true;
        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            logger.addLog(e.getMessage());
            return false;

        } catch (Exception e) {
            logger.addLog(e.getMessage());
            e.printStackTrace();
            return false;
        }


    }
}

