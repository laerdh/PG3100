package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Lars on 17.09.15.
 */
public class ConnectToDB implements AutoCloseable {
    private Connection con;

    public ConnectToDB(String host, String database, String user, String password) throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://" + host + "/" +
                database, user, password);
    }

    public void close() throws SQLException {
        con.close();
    }

    public Connection getConnection() {
        return con;
    }
}
