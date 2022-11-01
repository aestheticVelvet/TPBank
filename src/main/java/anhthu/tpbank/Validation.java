package anhthu.tpbank;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static HashMap<String,Account> accountHashMap;

    public static boolean validateLogin(String username, String password){
        for (Account account:accountHashMap.values()) {
            if (username.equals(account.getUsername())) {
                if (password.equals(account.getPassword()))
                    return true;
            }
        }
        return false;
    }

    public static Account getAnAccount(String username){
        refreshAccountHashmap();
        for (Account account:accountHashMap.values()) {
            if (username.equals(account.getUsername())) return account;
        }
        return null;
    }

    public static void refreshAccountHashmap() {
        accountHashMap = TPBank.getAccountHashMap();
    }

    public static boolean isValidConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public static boolean isUsernameValid(String username) {
        if (username.isBlank()) return false;
        for (Account account:accountHashMap.values()) {
            if (account.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidCapcha(String capcha, String inputCapcha) {
        return capcha.equals(inputCapcha);
    }

    public static boolean isValidName(String name) {
        Pattern pattern = Pattern.compile("^[A-Za-z ]++$");
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }

    public static boolean isValidAccountID(String id) {
        for (Account account:accountHashMap.values()) {
            if (account.getAccountId().equals(id)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumber(String input) {
        Pattern pattern = Pattern.compile("^[0-9]++$");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static boolean isValidTransfer(Account a, String destinationID, String amount) {
        if (isNumber(destinationID) && isNumber(amount)) {
            return isAccountExist(destinationID) && a.getAccountBallance() - Double.parseDouble(amount) >= 0;
        }
        return false;
    }

    public static boolean isAccountExist(String accountID) {
        refreshAccountHashmap();
        return accountHashMap.containsKey(accountID);
    }
}
