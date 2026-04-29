package library.exceptions;

import library.enums.BookStatus;

public class BookNotAvailableException extends Exception {

    private BookStatus currentStatus;

    public BookNotAvailableException(String title, BookStatus status) {
        super("Book is not available: '" + title + "'. Current status: " + status);
        this.currentStatus = status;
    }

    public BookStatus getCurrentStatus() {
        return currentStatus;
    }
}
