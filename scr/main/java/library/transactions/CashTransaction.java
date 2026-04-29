package library.transactions;

public class CashTransaction extends FineTransaction {
    private double cashTendered;

    public CashTransaction(double amount, double cashTendered) {
        super(amount);
        this.cashTendered = cashTendered;
    }

    @Override
    public boolean initiateTransaction() {
        if (cashTendered < amount) {
            System.out.println("[ERROR] Insufficient cash. Need: $" + amount + " | Given: $" + cashTendered);
            return false;
        }
        double change = cashTendered - amount;
        System.out.println("[PAYMENT] Cash: $" + amount + " | Change: $" + change);
        return true;
    }

    public double getCashTendered() { return cashTendered; }
}
