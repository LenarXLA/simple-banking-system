package banking;

import java.sql.*;

public class CardJDBC {
    private final String url;

    public CardJDBC(String url) {
        this.url = "jdbc:sqlite:" + url;
    }

    public void createNewTable(String url) {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	number TEXT,\n"
                + "	pin TEXT,\n"
                + "	balance INTEGER DEFAULT 0"
                + ");";

        try (Connection connection = DriverManager.getConnection(url);
            Statement stmt = connection.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewDatabase() {
        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                connection.getMetaData();
                createNewTable(url);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String number, String pin) {
        String sql = "INSERT INTO card(number, pin) VALUES(?,?)";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean findCurrentData(String number, String pin){
        boolean result = false;
        String sql = "SELECT number, pin FROM card";

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String num = rs.getString("number");
                String pinCode = rs.getString("pin");
                if (number.equals(num) && pin.equals(pinCode)) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public String getBalance(String number) {
        String balance = null;
        String sql = "SELECT balance FROM card WHERE number=" + number;

        try (Connection connection = DriverManager.getConnection(url);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                balance = rs.getString("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    public void addBalance(String number, int money) {
        String updateOrigin = "UPDATE card SET balance = ? WHERE number = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = con.prepareStatement(updateOrigin)) {

            preparedStatement.setInt(1, Integer.parseInt(getBalance(number)) + money);
            preparedStatement.setString(2, number);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transfer(String curCardNum, String curCardNumBalance, String transferCard, String transferCardBalance, int money) {

        String upd = "UPDATE card SET balance = ? WHERE number = ?";

        try (Connection con = DriverManager.getConnection(url)) {

            // Disable auto-commit mode
            con.setAutoCommit(false);

            try (PreparedStatement takeMoney = con.prepareStatement(upd)) {

                takeMoney.setInt(1, Integer.parseInt(curCardNumBalance) - money);
                takeMoney.setString(2, curCardNum);

                takeMoney.executeUpdate();

                PreparedStatement addMoney = con.prepareStatement(upd);
                addMoney.setInt(1, Integer.parseInt(transferCardBalance) + money);
                addMoney.setString(2, transferCard);

                addMoney.executeUpdate();

                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAccount(String curCardNum) {
        String del = "DELETE FROM card WHERE number = ?";

        try (Connection con = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = con.prepareStatement(del)) {

            preparedStatement.setString(1, curCardNum);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
