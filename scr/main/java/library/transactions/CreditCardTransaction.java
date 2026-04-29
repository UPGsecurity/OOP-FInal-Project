package library.transactions;

public class CreditCardTransaction extends FineTransaction {
    private String nameOnCard;

    public CreditCardTransaction(double amount, String nameOnCard) {
        super(amount);
        this.nameOnCard = nameOnCard;
    }

    @Override
    public boolean initiateTransaction() {
        System.out.println("[PAYMENT] Credit card: " + nameOnCard + " | Amount: $" + amount);
        return true;
    }

    public String getNameOnCard() { return nameOnCard; }
}
