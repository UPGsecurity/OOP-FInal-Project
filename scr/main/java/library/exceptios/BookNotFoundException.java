package library.exceptions;

public class BookNotFoundException extends Exception {

    public BookNotFoundException(String title) {
        super("Book not found: '" + title + "'");
    }

    public BookNotFoundException(String title, Throwable cause) {
        super("Book not found: '" + title + "'", cause);
    }
}
