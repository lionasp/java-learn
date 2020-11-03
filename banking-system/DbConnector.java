package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DbConnector {
    private static String dbName;
    private static SQLiteDataSource getDataSource() {
        String url = "jdbc:sqlite:" + dbName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    public static void initDb(String name) {
        dbName = name;
        try (Connection con = getDataSource().getConnection()) {
            if (con.isValid(5)) {
                createCardTable(con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAccount(Account account) {
        try (Connection con = getDataSource().getConnection()) {
            if (con.isValid(5)) {
                String sql = "INSERT INTO card" +
                        "(number, pin)" +
                        " VALUES " +
                        "(?, ?)";

                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setString(1, account.getCardNumber());
                    statement.setString(2, account.getPinCode());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateIncome(Account account) {
        try (Connection con = getDataSource().getConnection()) {
            if (con.isValid(5)) {
                String sql = "UPDATE card" +
                        " SET balance = ?" +
                        " WHERE " +
                        " number = ?";

                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setInt(1, account.getBalance());
                    statement.setString(2, account.getCardNumber());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeAccount(Account account) {
        try (Connection con = getDataSource().getConnection()) {
            if (con.isValid(5)) {
                String sql = "DELETE FROM card WHERE number = ?";
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setString(1, account.getCardNumber());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Account getAccount(String cardNumber, String pinCode) {
        try (Connection con = getDataSource().getConnection()) {
            if (con.isValid(5)) {
                String sql = "SELECT number, pin, balance FROM card WHERE number = ?";
                if (pinCode != null) {
                    sql += " AND pin = ?";
                }
                try (PreparedStatement statement = con.prepareStatement(sql)) {
                    statement.setString(1, cardNumber);
                    if (pinCode != null) {
                        statement.setString(2, pinCode);
                    }
                    ResultSet rs = statement.executeQuery();
                    if (rs.next()) {
                        return new Account(
                                rs.getString("number"),
                                rs.getString("pin"),
                                rs.getInt("balance")
                        );
                    }
                    return null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void createCardTable(Connection con) {
        try (Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
