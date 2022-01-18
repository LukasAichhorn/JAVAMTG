package database;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public  class Database {
    private static final Database database = new Database();
    private final String url = "jdbc:postgresql://localhost:5432/battleDB";
    private final String user = "postgres";
    private final String password = "admin";

    private Database(){};
    public static Database getInstance(){
        return database;
    }
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
}
