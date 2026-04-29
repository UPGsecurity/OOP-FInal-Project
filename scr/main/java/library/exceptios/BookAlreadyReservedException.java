package library.exceptions;

public class BookAlreadyReservedException extends Exception {

    private String reservedBy;

    public BookAlreadyReservedException(String title, String reservedBy) {
        super("Book is already reserved: '" + title + "' - Reserved by: " + reservedBy);
        this.reservedBy = reservedBy;
    }

    public String getReservedBy() { return reservedBy; }
}
