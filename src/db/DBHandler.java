package db;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Lars on 17.09.15.
 */
public class DBHandler {
    private ConnectToDB db;
    private Connection con;

    public DBHandler() {
        try {
            db = new ConnectToDB("localhost", "pg3100", "root", "root");
            con = db.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws SQLException {
        db.close();
    }

    public int update(String sql) {
        try (Statement stmt = con.createStatement()) {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<String> getTable(String tableName) {
        ArrayList<String> data = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;
        String temp = "";

        try (Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sql)) {
            ResultSetMetaData rsmd = res.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                temp += String.format("%-25s", rsmd.getColumnName(i).toUpperCase());
            }
            data.add(temp);

            while (res.next()) {
                temp = "";
                for (int i = 1; i <= numberOfColumns; i++) {
                    String columnTypeName = rsmd.getColumnTypeName(i);

                    switch (columnTypeName) {
                        case "CHAR":
                            temp += String.format("%-25s", res.getString(i));
                            break;
                        case "INT":
                            temp += String.format("%-25d", res.getInt(i));
                            break;
                    }
                }
                data.add(temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public int averageSum(String tableName, String columnName) {
        ArrayList<Integer> data = new ArrayList<>();
        String sql = "SELECT " + columnName + " FROM " + tableName;
        int count = 0;
        int average = 0;

        try (Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sql)) {
            ResultSetMetaData rsmd = res.getMetaData();
            String columnTypeName = rsmd.getColumnTypeName(1);

            while (res.next()) {
                switch (columnTypeName) {
                    case "CHAR":
                        data.add(Integer.parseInt(res.getString(1)));
                        count++;
                        break;
                    case "INT":
                        data.add(res.getInt(1));
                        count++;
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int n : data) {
            average += n;
        }

        return average / count;
    }

    public String minimumSum() {
        String data = "";
        String test1;
        String test2;

        return data;
    }

    public ResultSet getAll(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }

}
