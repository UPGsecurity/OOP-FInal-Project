package library.accounts;

import library.enums.AccountStatus;
import library.enums.BookStatus;
import library.enums.ReservationStatus;
import library.models.*;
import library.notifications.EmailNotification;
import library.search.Catalog;
import library.transactions.BookLending;
import library.transactions.BookReservation;

import java.util.List;

public class Librarian extends Account {

    public Librarian(String id, String password, Person person) {
        super(id, password, person);
    }

    public void addBookItem(BookItem bookItem, Catalog catalog) {
        catalog.updateCatalog(bookItem);
        System.out.println("[OK] Book added to catalog: " + bookItem.getTitle());
    }

    public void removeBookItem(BookItem bookItem, Catalog catalog) {
        catalog.removeFromCatalog(bookItem);
        System.out.println("[OK] Book removed from catalog: " + bookItem.getTitle());
    }

    public void blockMember(Member member) {
        member.setStatus(AccountStatus.BLACKLISTED);
        System.out.println("[OK] Member blocked: " + member.getId());
    }

    public void unblockMember(Member member) {
        member.setStatus(AccountStatus.ACTIVE);
        System.out.println("[OK] Member unblocked: " + member.getId());
    }

    public void issueBook(BookItem bookItem, Member member) {
        if (member.checkoutBookItem(bookItem)) {
            System.out.println("[OK] Librarian issued book: " + bookItem.getTitle()
                    + " -> " + member.getPerson().getName());
        } else {
            System.out.println("[ERROR] Could not issue book.");
        }
    }

    public LibraryCard registerNewMember(Member member) {
        String cardNumber = "LIB-" + System.currentTimeMillis();
        String barcode = "BAR-" + member.getId();
        LibraryCard card = new LibraryCard(cardNumber, barcode);
        member.setLibraryCard(card);
        System.out.println("[OK] New member registered: " + member.getPerson().getName());
        System.out.println("     Library card: " + cardNumber);
        return card;
    }

    public void cancelMembership(Member member) {
        if (!member.getActiveLendings().isEmpty()) {
            System.out.println("[ERROR] Member has unreturned books.");
            return;
        }
        member.setStatus(AccountStatus.CANCELED);
        System.out.println("[OK] Membership canceled: " + member.getId());
    }

    public void sendOverdueNotifications(List<BookLending> overdueList) {
        for (BookLending lending : overdueList) {
            String memberEmail = lending.getMember().getPerson().getEmail();
            String content = "Dear " + lending.getMember().getPerson().getName()
                    + ", the book \"" + lending.getBookItem().getTitle()
                    + "\" is overdue!";
            EmailNotification notif = new EmailNotification(
                    (int)(Math.random() * 9999), content, memberEmail
            );
            notif.sendNotification();
        }
    }

    public void notifyReservationAvailable(BookReservation reservation) {
        String email = reservation.getMember().getPerson().getEmail();
        String content = "Your book \"" + reservation.getBookItem().getTitle()
                + "\" is now available! Please come and pick it up.";
        EmailNotification notif = new EmailNotification(
                (int)(Math.random() * 9999), content, email
        );
        notif.sendNotification();

        reservation.getBookItem().setStatus(BookStatus.RESERVED);
        reservation.setStatus(ReservationStatus.WAITING);
    }

    @Override
    public String toString() {
        return "Librarian{id='" + id + "', name='" + person.getName() + "'}";
    }
}
