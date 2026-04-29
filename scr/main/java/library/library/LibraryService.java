package library.library;

import library.accounts.Librarian;
import library.accounts.Member;
import library.enums.BookStatus;
import library.models.*;
import library.notifications.EmailNotification;
import library.search.Catalog;
import library.transactions.*;

import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private final Catalog           catalog;
    private final List<BookLending> allLendings = new ArrayList<>();

    public LibraryService(Library library) {
        this.catalog = new Catalog();
    }

    public boolean checkoutBook(Member member, BookItem bookItem, BarcodeReader reader) {
        System.out.println("\n===== CHECKOUT BOOK PROCESS =====");

        if (member.getLibraryCard() == null || !member.getLibraryCard().isActive()) {
            System.out.println("[ERROR] No active library card.");
            System.out.println("================================\n");
            return false;
        }

        reader.scan(bookItem.getBarcode());
        boolean success = member.checkoutBookItem(bookItem);

        if (success) {

            BookLending lending = member.getActiveLendings().stream()
                    .filter(l -> l.getBookItem().equals(bookItem) && l.getReturnDate() == null)
                    .findFirst().orElse(null);

            if (lending != null && !allLendings.contains(lending)) {
                allLendings.add(lending);
            }
        }

        System.out.println("================================\n");
        return success;
    }

    public boolean returnBook(Member member, BookItem bookItem,
                              BarcodeReader reader, Librarian librarian) {
        System.out.println("\n===== RETURN BOOK PROCESS =====");
        reader.scan(bookItem.getBarcode());

        // FIXED: Previously it only searched in allLendings.
        // When the program restarts, allLendings may be empty,
        // so we also search from member.getActiveLendings().
        BookLending lending = allLendings.stream()
                .filter(l -> l.getBookItem().equals(bookItem)
                        && l.getMember().equals(member)
                        && l.getReturnDate() == null)
                .findFirst().orElse(null);

        if (lending == null) {
            lending = member.getActiveLendings().stream()
                    .filter(l -> l.getBookItem().equals(bookItem)
                            && l.getReturnDate() == null)
                    .findFirst().orElse(null);
        }

        if (lending == null) {
            System.out.println("[ERROR] This book was not checked out to you.");
            System.out.println("====================================\n");
            return false;
        }

        if (lending.isOverdue()) {
            Fine fine = new Fine(lending);
            System.out.println("[Fine] You need to pay: $" + fine.getAmount());
            CashTransaction payment = new CashTransaction(fine.getAmount(), fine.getAmount());
            payment.initiateTransaction();
        }

        lending.returnBook();
        member.returnBookItem(bookItem);
        allLendings.remove(lending);
        bookItem.setStatus(BookStatus.AVAILABLE);

        System.out.println("====================================\n");
        return true;
    }

    public void renewBook(Member member, BookItem bookItem, BarcodeReader reader) {
        System.out.println("\n===== RENEW BOOK =====");
        reader.scan(bookItem.getBarcode());

        BookLending lending = allLendings.stream()
                .filter(l -> l.getBookItem().equals(bookItem)
                        && l.getMember().equals(member)
                        && l.getReturnDate() == null)
                .findFirst().orElse(null);

        if (lending == null) {
            lending = member.getActiveLendings().stream()
                    .filter(l -> l.getBookItem().equals(bookItem)
                            && l.getReturnDate() == null)
                    .findFirst().orElse(null);
        }

        if (lending == null) {
            System.out.println("[ERROR] Active lending not found.");
            return;
        }
        if (lending.renewBook(false)) System.out.println("[OK] Book renewed!");
        else                           System.out.println("[ERROR] Renewal failed!");
        System.out.println("==============================\n");
    }

    public void sendOverdueNotifications() {
        allLendings.stream().filter(BookLending::isOverdue).forEach(l -> {
            String content = "Your book \"" + l.getBookItem().getTitle()
                    + "\" is " + l.getOverdueDays() + " days late.";
            new EmailNotification((int)(Math.random()*9999),
                    content, l.getMember().getPerson().getEmail()).sendNotification();
        });
    }

    public Catalog           getCatalog()    { return catalog; }
    public List<BookLending> getAllLendings() { return allLendings; }
}
