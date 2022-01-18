package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.Database;
import helper.BattleQueue;
import models.Battle;
import models.Player;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class MainServer implements Runnable {

    private static ServerSocket _listener = null;
    public static Database db = Database.getInstance();
    public static ObjectMapper mapper = new ObjectMapper();
    public static BattleQueue battleQueue = BattleQueue.getInstance();



    public static void main(String[] args) {

        System.out.println("start server");
        int cnt = 0;


        try {
            _listener = new ServerSocket(10001);
            while (true) {
                Socket socket = _listener.accept();

                System.out.println("new incoming connection");
                (new ServerThread(socket, ++cnt)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //Runtime.getRuntime().addShutdownHook(new Thread(new MainServer()));

    }

    @Override
    public void run() {
        try {
            _listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _listener = null;
        System.out.println("close server");
    }

}
