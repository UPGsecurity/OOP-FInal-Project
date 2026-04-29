package library.transactions;

public class CheckTransaction extends FineTransaction {
    private String bankName;
    private String checkNumber;

    public CheckTransaction(double amount, String bankName, String checkNumber) {
        super(amount);
        this.bankName = bankName;
        this.checkNumber = checkNumber;
    }

    @Override
    public boolean initiateTransaction() {
        System.out.println("[PAYMENT] Check: " + bankName + " | Check number: " + checkNumber + " | Amount: $" + amount);
        return true;
    }

    public String getBankName() { return bankName; }
    public String getCheckNumber() { return checkNumber; }
}
