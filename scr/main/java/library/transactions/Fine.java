package library.transactions;

public class Fine {

    private double      amount;
    private BookLending lending;

    private static final double FINE_PER_DAY = 1.0;

    public Fine(BookLending lending) {
        this.lending = lending;
        this.amount  = calculateAmount();
    }

    private double calculateAmount() {
        long overdueDays = lending.getOverdueDays();
        if (overdueDays <= 0) return 0.0;
        double calc = overdueDays * FINE_PER_DAY;
        System.out.println("[FINE] " + overdueDays + " days overdue. Fine: $" + calc);
        return calc;
    }

    public double getAmount() { return amount; }

    @Override
    public String toString() {
        return "Fine{amount=$" + amount + ", book='" + lending.getBookItem().getTitle() + "'}";
    }
}
