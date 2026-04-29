package library.accounts;

import library.enums.AccountStatus;
import library.enums.BookStatus;
import library.enums.ReservationStatus;
import library.models.*;
import library.transactions.BookLending;
import library.transactions.BookReservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Member extends Account {

    private final Date dateOfMembership;
    private int totalBooksCheckedout;
    private LibraryCard libraryCard;
    private static final int MAX_BOOKS_LIMIT = 5;
    private final List<BookLending>     activeLendings     = new ArrayList<>();
    private final List<BookReservation> activeReservations = new ArrayList<>();

    public Member(String id, String password, Person person) {
        super(id, password, person);
        this.dateOfMembership     = new Date();
        this.totalBooksCheckedout = 0;
    }

    public boolean checkoutBookItem(BookItem bookItem) {
        if (getStatus() != AccountStatus.ACTIVE) {
            System.out.println("[ERROR] Your account is not active.");
            return false;
        }
        if (totalBooksCheckedout >= MAX_BOOKS_LIMIT) {
            System.out.println("[ERROR] You have reached the limit of " + MAX_BOOKS_LIMIT + " books.");
            return false;
        }
        if (bookItem.getStatus() != BookStatus.AVAILABLE) {
            System.out.println("[ERROR] Book is not available. Status: " + bookItem.getStatus());
            return false;
        }

        if (bookItem.checkout()) {
            BookLending lending = new BookLending(bookItem, this);
            activeLendings.add(lending);
            totalBooksCheckedout++;

            activeReservations.stream()
                    .filter(r -> r.getBookItem().equals(bookItem))
                    .forEach(r -> r.setStatus(ReservationStatus.COMPLETED));

            System.out.println("[OK] Book successfully checked out: " + bookItem.getTitle());
            System.out.println("     Due date: " + bookItem.getDueDate());
            return true;
        }
        return false;
    }

    public boolean returnBookItem(BookItem bookItem) {
        BookLending lending = activeLendings.stream()
                .filter(l -> l.getBookItem().equals(bookItem))
                .findFirst()
                .orElse(null);

        if (lending == null) {
            System.out.println("[ERROR] You do not have this book: " + bookItem.getTitle());
            return false;
        }

        lending.returnBook();
        activeLendings.remove(lending);
        totalBooksCheckedout--;
        System.out.println("[OK] Book returned: " + bookItem.getTitle());
        return true;
    }

    public void reserveBookItem(BookItem bookItem) {
        if (bookItem.getStatus() == BookStatus.AVAILABLE) {
            System.out.println("[INFO] Book is available, you can take it directly.");
            return;
        }

        boolean alreadyReserved = activeReservations.stream()
                .anyMatch(r -> r.getBookItem().equals(bookItem)
                        && r.getStatus() == ReservationStatus.WAITING);

        if (alreadyReserved) {
            System.out.println("[ERROR] You have already reserved this book!");
            return;
        }

        BookReservation reservation = new BookReservation(bookItem, this);
        activeReservations.add(reservation);
        bookItem.setStatus(BookStatus.RESERVED);
        System.out.println("[OK] Book reserved: " + bookItem.getTitle());
    }

    public void removeReservation(BookReservation reservation) {
        if (!activeReservations.contains(reservation)) {
            System.out.println("[ERROR] You do not have this reservation.");
            return;
        }
        reservation.setStatus(ReservationStatus.CANCELED);
        activeReservations.remove(reservation);
        System.out.println("[OK] Reservation canceled.");
    }

    public List<BookItem> getBorrowedBooks() {
        List<BookItem> books = new ArrayList<>();
        for (BookLending lending : activeLendings) {
            books.add(lending.getBookItem());
        }
        return books;
    }

    public int                   getTotalBooksCheckedout() { return totalBooksCheckedout; }
    public Date                  getDateOfMembership()     { return dateOfMembership; }
    public LibraryCard           getLibraryCard()          { return libraryCard; }
    public List<BookLending>     getActiveLendings()       { return activeLendings; }
    public List<BookReservation> getActiveReservations()   { return activeReservations; }

    public void setLibraryCard(LibraryCard lc)        { this.libraryCard = lc; }
    // FIXED: Needed to restore count when loading from DataStorage
    public void setTotalBooksCheckedout(int count)    { this.totalBooksCheckedout = count; }

    @Override
    public String toString() {
        return "Member{id='" + id + "', name='" + person.getName() +
                "', booksCheckedout=" + totalBooksCheckedout + "}";
    }
}
