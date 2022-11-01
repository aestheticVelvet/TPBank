package anhthu.tpbank;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable{
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    private static String username;
    private static String password;
    @FXML
    private Label welcomeLabel;

    public void switchToSignUpScene(ActionEvent e){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/SignupForm.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMenuScene(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }


    public void login(ActionEvent e){
        username =  setUsername();
        password = setPassword();
        Validation.refreshAccountHashmap();
        if (Validation.validateLogin(username,password)) {
            try {
                switchToMenuScene(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else loginError();
    }

    private void loginError() {
        usernameTextField.requestFocus();
        usernameTextField.setStyle(Styles.errorTextBox());
        usernameTextField.setText("");
        passwordTextField.setStyle(Styles.errorTextBox());
        passwordTextField.setText("");
        usernameTextField.setPromptText("username or password is not valid!!!");
        usernameTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> usernameTextField.setPromptText(""));
    }


    private void userNameTextFieldKeyEvent(ActionEvent e) {
        usernameTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER") ||
            keyEvent.getCode().toString().equalsIgnoreCase("Down"))
                passwordTextField.requestFocus();
        });
    }

    private void passwordTextFieldKeyEvent(ActionEvent e) {
        passwordTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER")) {
               login(e);
            }
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                usernameTextField.requestFocus();
            else if (keyEvent.getCode().toString().equalsIgnoreCase("DOWN"))
                loginButton.requestFocus();
        });
    }

    private void loginButtonKeyEvent(ActionEvent e) {
        loginButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER"))
                login(e);
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                passwordTextField.requestFocus();
            else if (keyEvent.getCode().toString().equalsIgnoreCase("DOWN"))
                signupButton.requestFocus();
        });
    }

    private void signupButtonKeyEvent(ActionEvent e) {
        signupButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER"))
                switchToSignUpScene(e);
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                loginButton.requestFocus();
            else if (keyEvent.getCode().toString().equalsIgnoreCase("DOWN"))
                usernameTextField.requestFocus();
        });
    }

    private void setupKeyEvent(ActionEvent e) {
        userNameTextFieldKeyEvent(e);
        passwordTextFieldKeyEvent(e);
        loginButtonKeyEvent(e);
        signupButtonKeyEvent(e);
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    private String setUsername() {
        return username = usernameTextField.getText();
    }

    private String setPassword() {
        return password = passwordTextField.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ActionEvent event = new ActionEvent();
        setupKeyEvent(event);
    }

}
