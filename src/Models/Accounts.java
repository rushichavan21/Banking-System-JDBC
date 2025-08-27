package Models;

public class Accounts {
    private int account_id;
    private long account_number;
    private int balance;
    private int user_id;
    Accounts(int account_id, long account_number, int balance, int user_id) {
        this.account_id = account_id;
        this.account_number = account_number;
        this.balance = balance;
        this.user_id = user_id;
    }
    public void Account_info(){
        System.out.println("Account number : "+account_number);
        System.out.println("Account balance : "+balance);
        System.out.println("Account user_id : "+user_id);
    }
}
