package server;

import database.Query;
import helper.Logger;
import models.Player;
import models.RequestBuilder;
import routes.GET.GetRoutes;
import routes.HttpCodes;
import routes.POST.PostRoutes;
import routes.PUT.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ServerThread extends Thread {
    private Socket s;
    private final int name;
    private final Logger logger = new Logger();




    public ServerThread(Socket s, int name) {
        this.s = s;
        this.name = name;
    }

    public void run() {
        try {

            String threadName = Integer.toString(name);
            logger.setName(threadName);
            logger.addLog("init logger");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            System.out.println("+++++++ t-" + name + " is starting");
            writer.write("This request is handled by thread t-" + name);
            writer.newLine();
            writer.flush();
            StringBuffer sb = new StringBuffer(500);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Vector<String> request = new Vector<>();
            HttpCodes finalResponse = HttpCodes.BAD_REQUEST;


            int c;
            while ((c = reader.read()) != -1) {
                // space
                if (c == '\r') {
                    //absatz
                } else if (c == '\n') {
                    if (sb.length() <= 0) {
                        break;
                    } else {
                        request.add(sb.toString());
                        System.out.println("-----------------------> " + sb.toString());
                        sb = new StringBuffer(500);
                    }
                } else {
                    sb.append((char) c);
                }
            }

            if (request.lastElement().startsWith("Content-Length:")) {
                int cl = Integer.parseInt(request.lastElement().substring(request.lastElement().indexOf(" ") + 1));
                sb = new StringBuffer(cl);
                for (int i = 0; i < cl; i++) {
                    sb.append((char) reader.read());
                }
                request.add(sb.toString());

            }

// build new Request
            RequestBuilder decodedRequest = new RequestBuilder(request);
            System.out.println("decoded request:");
            System.out.println(decodedRequest);
            Player loggedInUser;
            loggedInUser = Query.authByToken(decodedRequest.getAuthToken(),logger);

            switch (decodedRequest.getType()) {
                case POST:
                    if (decodedRequest.getRoute().equals("/users")) finalResponse = PostRoutes.createUserRoute(decodedRequest,logger);
                    if (decodedRequest.getRoute().equals("/sessions")) finalResponse = PostRoutes.loginUserRoute(decodedRequest,logger);
                    if (decodedRequest.getRoute().equals("/packages")) finalResponse = PostRoutes.createPackagesRoute(decodedRequest,logger);
                    if (decodedRequest.getRoute().equals("/transactions/packages")) finalResponse = PostRoutes.acquirePackageRoute(decodedRequest,logger);
                    if (decodedRequest.getRoute().equals("/battles")) finalResponse = PostRoutes.handleBattleRoute(decodedRequest,logger,writer);
                    if(decodedRequest.getRoute().equals("/tradings")) finalResponse = PostRoutes.insertTrade(decodedRequest,logger);


                    break;
                case GET:
                    if(decodedRequest.getRoute().equals("/cards")) finalResponse = GetRoutes.getUserCardLibary(decodedRequest,logger);
                    if(decodedRequest.getRoute().equals("/deck")) finalResponse = GetRoutes.getUserDeck(decodedRequest,logger);
                    if(decodedRequest.getRoute().equals("/users/" + loggedInUser.getUsername())) finalResponse = GetRoutes.getUserInfoRoute(loggedInUser,logger);
                    if(decodedRequest.getRoute().equals("/stats")) finalResponse = GetRoutes.getStatRoute(decodedRequest,logger);
                    if(decodedRequest.getRoute().equals("/score")) finalResponse = GetRoutes.getScoreBoard(decodedRequest,logger);
                    if(decodedRequest.getRoute().equals("/deckStatistics")) finalResponse = GetRoutes.calculateDeckStatistics(decodedRequest,logger);
                    if(decodedRequest.getRoute().equals("/tradings")) finalResponse = GetRoutes.getOpenTrades(decodedRequest,logger);
                    break;
                case DELETE:
                    break;
                case PUT:
                    if(decodedRequest.getRoute().equals("/deck")) finalResponse = PutRoutes.manageDeckRoute(decodedRequest,logger);
                    if(decodedRequest.getRoute().equals("/users/" + loggedInUser.getUsername())) finalResponse = PutRoutes.editUserInfoRoute(loggedInUser,decodedRequest,logger);

                    break;
            }


            //writer.write("server response with " + finalResponse.toString());
            writer.newLine();
            writer.flush();
            logger.addLog(finalResponse.toString());
            logger.printLogs(writer);
            System.out.println("end of stream");
            System.out.println("+++++++ t-" + name + " is closing");
            s.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
