package helper;

import lombok.*;


import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Logger {
    private String name;
    private final List<String> logs = new ArrayList<>();

    public Logger(){};


    public void addLog(String message){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        logs.add("thread: "+ name + " Ts:"+ timestamp + " -- " + message);

    }
    public void printLogs(BufferedWriter writer){
        try {
            for (String log : logs) {
                writer.newLine();
                writer.write(log);
                writer.flush();
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
