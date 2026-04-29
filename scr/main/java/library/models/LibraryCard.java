package library.models;

import java.util.Date;

public class LibraryCard {
    private String  cardNumber;
    private String  barcode;
    private Date    issuedAt;
    private boolean active;

    public LibraryCard(String cardNumber, String barcode) {
        this.cardNumber = cardNumber;
        this.barcode    = barcode;
        this.issuedAt   = new Date();
        this.active     = true;
    }

    public boolean isActive() { return active; }

    public void deactivate() {
        this.active = false;
        System.out.println("[INFO] Card deactivated: " + cardNumber);
    }

    public void activate() {
        this.active = true;
        System.out.println("[INFO] Card activated: " + cardNumber);
    }

    public String getCardNumber() { return cardNumber; }
    public String getBarcode()    { return barcode; }
    public Date   getIssuedAt()   { return issuedAt; }

    @Override
    public String toString() {
        return "LibraryCard{cardNumber='" + cardNumber + "', active=" + active + "}";
    }
}
