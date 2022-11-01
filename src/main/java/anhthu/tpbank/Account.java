package anhthu.tpbank;

public class Account {
    private String accountId;
    private String name;
    private String username;
    private String password;
    private double accountBallance;
    private StringBuilder formattedAccountID;
    private StringBuilder formattedAccountBalance;
    public Account() {

    }
    public Account(String accountId, String name, String username, String password, double accountBalance) {
        this.accountBallance = accountBalance;
        this.accountId = accountId;
        this.name = name;
        this.password = password;
        this.username = username;
        formatAccountID();
        formatAccountBalance();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getAccountBallance() {
        return accountBallance;
    }

    public void setAccountBallance(double accountBallance) {
        this.accountBallance = accountBallance;
    }

    private void formatAccountID() {
        formattedAccountID = new StringBuilder(accountId);
        for (int i=4; i<formattedAccountID.length(); i+=6) {
            formattedAccountID.insert(i,"  ");
        }
    }

    private void formatAccountBalance() {
        formattedAccountBalance = new StringBuilder(String.valueOf(accountBallance));
        int index = 0;
        for (int i=formattedAccountBalance.length()-1; i>=0; i--) {
            if (formattedAccountBalance.charAt(i)=='.') {
                index = i;
                break;
            }
        }
        for (int i=index-4; i>0; i-=4) {
            formattedAccountID.insert(i," ");
        }
    }

    public String getFormatAccountID() {
        return formattedAccountID.toString();
    }
    public String getFormatAccountBalance() {
        return formattedAccountBalance.toString();
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountBallance=" + accountBallance +
                ", formattedAccountID=" + formattedAccountID +
                '}';
    }
}
