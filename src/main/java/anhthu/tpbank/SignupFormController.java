package anhthu.tpbank;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class SignupFormController implements Initializable {
    @FXML
    private Label capchaLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField confirmPasswordTextField;
    @FXML
    private TextField capchaTextField;
    @FXML
    private TextField fullNameTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Label welcomeLabel;

    private String capcha;
    private String inputCapcha;
    static String username;
    private String password;
    private String confirmPassword;
    private String fullName;

    public void switchToLoginScene(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMainMenuScene(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setCapchaLabel() {
        capcha = generateCapcha(6);
        capchaLabel.setText(capcha);
        capchaLabel.setFont(new Font("Arial",30));
    }

    private String generateCapcha(int n) {
        Random rand = new Random();

        String chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder captcha = new StringBuilder();
        while (n-->0){
            int index = rand.nextInt(0,chrs.length());
            captcha.append(chrs.charAt(index));
        }

        return captcha.toString();
    }

    private void getSignupDataFromUser() {
        fullName = fullNameTextField.getText();
        username = usernameTextField.getText();
        password = passwordTextField.getText();
        confirmPassword = confirmPasswordTextField.getText();
        inputCapcha = capchaTextField.getText();
    }

    private boolean returnSignupError() {
        if (!Validation.isValidName(fullName)) {
            fullNameError();
            return false;
        }else if(!Validation.isUsernameValid(username)) {
            usernameError();
            return false;
        } else if (!Validation.isValidConfirmPassword(password, confirmPassword)) {
            conFirmPasswordError();
            return false;
        } else if (!Validation.isValidCapcha(inputCapcha,capcha)) {
            capchaError();
            return false;
        }
        return true;
    }

    public void signup(ActionEvent e) throws IOException {
        Validation.refreshAccountHashmap();
        getSignupDataFromUser();
        if (returnSignupError()) {
            pushAccountToDb();
            switchToMainMenuScene(e);
        } else {
            setCapchaLabel();
        }
        }


    private void pushAccountToDb() {
        String accountId = generateAccountID();
        while (!Validation.isValidAccountID(accountId)) {
            accountId = generateAccountID();
        }
        Account a = new Account(accountId,fullName,username,password,0);
        try {
            Queries.insertAnAccount(a);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fullNameError() {
            fullNameTextField.setStyle(Styles.errorTextBox());
            fullNameTextField.setText("");
            fullNameTextField.setPromptText("⚠ Name is not valid!!!");
            fullNameTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> fullNameTextField.setPromptText(""));
    }

    public void usernameError() {
        usernameTextField.setStyle(Styles.errorTextBox());
        usernameTextField.setText("");
        usernameTextField.setPromptText("⚠ Username is not valid!!!");
        usernameTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> usernameTextField.setPromptText(""));
    }

    public void conFirmPasswordError() {
        confirmPasswordTextField.setStyle(Styles.errorTextBox());
        confirmPasswordTextField.setText("");
        confirmPasswordTextField.setPromptText("⚠ Confirm password is not matched!!!");
        confirmPasswordTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> confirmPasswordTextField.setPromptText(""));
    }

    public void capchaError() {
        capchaTextField.setStyle(Styles.errorTextBox());
        capchaTextField.setText("");
        capchaTextField.setPromptText("⚠ Capcha is not valid");
        capchaTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> capchaTextField.setPromptText(""));
    }

    private String generateAccountID() {
        StringBuilder res = new StringBuilder();
        Random random = new Random();
        for (int i=0; i<4; i++) {
            res.append(random.nextInt(1000,10000));
        }
        System.out.println(res);
        return res.toString();
    }

    public static String getUsername() {
        return username;
    }

    private void fullNameTextFieldKeyEvent(ActionEvent e) {
        fullNameTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER") ||
                    keyEvent.getCode().toString().equalsIgnoreCase("Down"))
                usernameTextField.requestFocus();
        });
    }


    private void userNameTextFieldKeyEvent(ActionEvent e) {
        usernameTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER") ||
                    keyEvent.getCode().toString().equalsIgnoreCase("Down"))
                passwordTextField.requestFocus();
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                fullNameTextField.requestFocus();
        });
    }

    private void passwordTextFieldKeyEvent(ActionEvent e) {
        passwordTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER") || keyEvent.getCode().toString().equalsIgnoreCase("DOWN") ) {
                confirmPasswordTextField.requestFocus();
            }
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                usernameTextField.requestFocus();
        });
    }

    private void confirmPasswordTextFieldKeyEvent(ActionEvent e) {
        confirmPasswordTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER") ||
                    keyEvent.getCode().toString().equalsIgnoreCase("Down"))
                capchaTextField.requestFocus();
            else if (keyEvent.getCode().toString().equalsIgnoreCase("Up")) passwordTextField.requestFocus();
        });
    }

    private void capchaTextFieldKeyEvent(ActionEvent e) {
        capchaTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER")) {
                try {
                    signup(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                confirmPasswordTextField.requestFocus();
            else if (keyEvent.getCode().toString().equalsIgnoreCase("DOWN"))
                signupButton.requestFocus();
        });
    }

    private void signupButtonKeyEvent(ActionEvent e) {
        signupButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER")) {
                try {
                    signup(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                capchaTextField.requestFocus();
            else if (keyEvent.getCode().toString().equalsIgnoreCase("DOWN"))
                loginButton.requestFocus();
        });
    }

    private void loginButtonKeyEvent(ActionEvent e) {
        loginButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER")) {
                try {
                    switchToLoginScene(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else if(keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                signupButton.requestFocus();
            else if (keyEvent.getCode().toString().equalsIgnoreCase("DOWN"))
                fullNameTextField.requestFocus();
        });
    }

    private void setupKeyEvent(ActionEvent e) {
        fullNameTextFieldKeyEvent(e);
        userNameTextFieldKeyEvent(e);
        confirmPasswordTextFieldKeyEvent(e);
        capchaTextFieldKeyEvent(e);
        passwordTextFieldKeyEvent(e);
        loginButtonKeyEvent(e);
        signupButtonKeyEvent(e);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCapchaLabel();
        ActionEvent e = new ActionEvent();
        setupKeyEvent(e);
    }
}
