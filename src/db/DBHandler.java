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

    public int getAverageSum(String tableName, String columnName) {
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

    // Refactor this method for use with user-specified tables and columns as input
    public String[] getMinimumBalance() {
        int min = 99999;
        String sql = "SELECT saldo, fornavn, etternavn, personnr, kontonr FROM konto";
        String[] data = new String[5];

        try (Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sql)) {
            ResultSetMetaData rsmd = res.getMetaData();

            while (res.next()) {
                int tempBalance = res.getInt(1);
                if (tempBalance < min) {
                       min = tempBalance;
                    for (int i = 1; i <= 2; i++) {
                        String columnType = rsmd.getColumnTypeName(i + 3);

                        // Check if columndata is String or int, parse to String if int
                        switch (columnType) {
                            case "CHAR":
                                data[i + 2] = res.getString(i + 3);
                                break;
                            case "INT":
                                data[i + 3] = String.valueOf(res.getInt(i + 3));
                                break;
                        }
                    }
                    data[0] = String.valueOf(min);
                    data[1] = res.getString(2);
                    data[2] = res.getString(3);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public ResultSet getAll(String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName;

        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(sql);
    }

}
