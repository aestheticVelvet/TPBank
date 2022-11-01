package anhthu.tpbank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TPBank extends Application {
    public static Connection connection;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        connectToDb();
        launch();
    }

    private static void connectToDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_withdrawl","root","140801");

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Cannot connect to the database");
            ex.printStackTrace();
        }
    }

    public static HashMap<String,Account> getAccountHashMap() {
        HashMap<String,Account> accountHashMap = new HashMap<>();
        try {
            ResultSet rs = Queries.getAllAccounts();
            while (rs.next()) {
                Account account = createAccount(rs);
                accountHashMap.put(account.getAccountId(),account);
            }
        } catch (SQLException ex) {
            System.out.println("Can not find the account");
            ex.printStackTrace();
        }
        return accountHashMap;
    }

    private static Account createAccount(ResultSet dataFromDb) {
        Account account = new Account();
        try {
//            dataFromDb.next();
            account = new Account(dataFromDb.getString("account_number"), dataFromDb.getString("name"), dataFromDb.getString("username"),dataFromDb.getString("password"),Double.parseDouble(dataFromDb.getString("account_balance")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }
}