package anhthu.tpbank;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private ChoiceBox<String> optionChoiceBox;
    @FXML
    private Label accountNumber;
    @FXML
    private Label cardHolderName;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label cardNumberLabel;
    @FXML
    private TextField cardNumberTextField;
    @FXML
    private TextField amountTextField;
    private Account currentAccount;
    private String option;
    @FXML
    private Button sendRequestButton;
    @FXML
    private Button cancelButton;

    public void switchToLoginScene(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Views/LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setupChoiceBox() {
        optionChoiceBox.getItems().add("Get Cash");
        optionChoiceBox.getItems().add("Transfer");
        optionChoiceBox.setValue("Get Cash");
        cardNumberLabel.setVisible(false);
        cardNumberTextField.setVisible(false);
        optionChoiceBox.setOnAction(this::setOption);
        option = "get cash";
    }



    private void setCurrentAccount(){
        String username;
        username =  LoginFormController.getUsername();
        if (username == null) {
            username = SignupFormController.getUsername();
        }
        System.out.println(username);
        currentAccount = Validation.getAnAccount(username);
    }

    private void setUpUserInfo() throws SQLException {
        setCurrentAccount();
        accountNumber.setText(currentAccount.getFormatAccountID());
        System.out.println(currentAccount.getFormatAccountID());
        cardHolderName.setText(currentAccount.getName());
        balanceLabel.setText(currentAccount.getFormatAccountBalance());
    }

    public void sendRequest() throws SQLException {
        if (option.equalsIgnoreCase("get cash")) {
            getCashRequest();
            setUpUserInfo();
        } else if ((option.equalsIgnoreCase("transfer"))) {
            getTransferRequest();
            setUpUserInfo();
        }
    }

    private void getCashRequest() throws SQLException {
        if (Validation.isNumber(amountTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure");
            alert.setHeaderText("Do you want to get "+amountTextField.getText()+" VND");
            if(alert.showAndWait().get() == ButtonType.OK) {
                Queries.getCash(currentAccount.getAccountId(), Double.parseDouble(amountTextField.getText()));
            }
        } else {
            amountError();
        }
    }

    public void amountError() {
        amountTextField.requestFocus();
        amountTextField.setStyle(Styles.errorTextBox());
        amountTextField.setText("");
        amountTextField.setStyle(Styles.errorTextBox());
        amountTextField.setText("");
        amountTextField.setPromptText("Amount is not valid!!!");
        amountTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> amountTextField.setPromptText(""));
    }

    public void accountNumberError() {
        cardNumberTextField.requestFocus();
        cardNumberTextField.setStyle(Styles.errorTextBox());
        cardNumberTextField.setText("");
        cardNumberTextField.setStyle(Styles.errorTextBox());
        cardNumberTextField.setText("");
        cardNumberTextField.setPromptText("Not valid!!!");
        cardNumberTextField.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> cardNumberTextField.setPromptText(""));
    }

    private void getTransferRequest() throws SQLException {
        if (Validation.isValidTransfer(currentAccount,cardNumberTextField.getText(),amountTextField.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Are you sure");
            String amount = amountTextField.getText();
            String cardNumber = cardNumberTextField.getText();
            alert.setContentText("Do you want to transfer "+amount+" VND to "+cardNumber);
            if(alert.showAndWait().get() == ButtonType.OK) {
                Queries.transfer(cardNumber, currentAccount.getAccountId(), Double.parseDouble(amount));
            }
            } else if (Validation.isNumber(cardNumberTextField.getText()) ) {
            accountNumberError();
            } else {
            amountError();
            }

    }

    private void setOption(ActionEvent e) {
        option = optionChoiceBox.getValue();
        if (option.equalsIgnoreCase("get cash")) {
            getcash();
        } else if (option.equalsIgnoreCase("transfer")) {
            transfer();
        }
    }

    private void transfer() {
        cardNumberLabel.setVisible(true);
        cardNumberTextField.setVisible(true);
    }

    private void getcash() {
        cardNumberLabel.setVisible(false);
        cardNumberTextField.setVisible(false);
    }

    private void amountTextFiedKeyEvent(ActionEvent e) {
        amountTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER")) {
                sendRequestButton.requestFocus();
            } else if ( keyEvent.getCode().toString().equalsIgnoreCase("Down")) {
                sendRequestButton.requestFocus();
            }
            if (option.equalsIgnoreCase("transfer")) {
                if (keyEvent.getCode().toString().equalsIgnoreCase("Up"))
                    cardNumberTextField.requestFocus();
            }
        });
    }

    private void cardNumberTextFiedKeyEvent(ActionEvent e) {
        cardNumberTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().toString().equalsIgnoreCase("ENTER") || keyEvent.getCode().toString().equalsIgnoreCase("Down"))
                amountTextField.requestFocus();
        });
    }

    private void setupKeyEvent(ActionEvent e) {
        amountTextFiedKeyEvent(e);
        cardNumberTextFiedKeyEvent(e);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ActionEvent event = new ActionEvent();
        setupKeyEvent(event);
        setupChoiceBox();
        try {
            setUpUserInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
