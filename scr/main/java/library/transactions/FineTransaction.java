package library.transactions;

import java.util.Date;

public abstract class FineTransaction {
    protected Date creationDate;
    protected double amount;

    public FineTransaction(double amount) {
        this.amount = amount;
        this.creationDate = new Date();
    }

    public abstract boolean initiateTransaction();

    public double getAmount() { return amount; }
    public Date getCreationDate() { return creationDate; }
}
