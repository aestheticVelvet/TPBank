package anhthu.tpbank;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {

    public static ResultSet getAllAccounts() throws SQLException {
        PreparedStatement statement = TPBank.connection.prepareStatement("SELECT * FROM bank_withdrawl.clients");
        return statement.executeQuery();
    }

    public static void insertAnAccount(Account account) throws SQLException {
        PreparedStatement statement =
                TPBank.connection.prepareStatement("INSERT INTO bank_withdrawl.clients VALUES (?,?,?,?,100000)");
        statement.setString(1,account.getAccountId());
        statement.setString(2,account.getName());
        statement.setString(3,account.getUsername());
        statement.setString(4,account.getPassword());
        statement.executeUpdate();
    }

    public static void getCash(String accountNumber, double amount) throws SQLException {
        PreparedStatement statement =
                TPBank.connection.prepareStatement("UPDATE clients SET account_balance = account_balance - ? WHERE account_number = ?");
        statement.setDouble(1,amount);
        statement.setString(2,accountNumber);
        statement.executeUpdate();
    }

    public static void transfer(String destinationAccountNumber, String accountNumber, double amount) throws SQLException {
        PreparedStatement decrease =
                TPBank.connection.prepareStatement("UPDATE clients SET account_balance = account_balance - ? WHERE account_number = ?");
        PreparedStatement increase =
                TPBank.connection.prepareStatement("UPDATE clients SET account_balance = account_balance + ? WHERE account_number = ?");
        decrease.setDouble(1,amount);
        decrease.setString(2,accountNumber);
        increase.setDouble(1,amount);
        increase.setString(2,destinationAccountNumber);
        decrease.executeUpdate();
        increase.executeUpdate();
    }
}


