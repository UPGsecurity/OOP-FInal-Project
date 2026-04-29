package library.transactions;

import library.accounts.Member;
import library.models.BookItem;

import java.util.Date;

public class BookLending {

    private Date     creationDate;
    private Date     dueDate;
    private Date     returnDate;
    private BookItem bookItem;
    private Member   member;

    private static final int LOAN_DAYS = 14;

    public BookLending(BookItem bookItem, Member member) {
        this.bookItem     = bookItem;
        this.member       = member;
        this.creationDate = new Date();

        long dueMs = System.currentTimeMillis() + (long) LOAN_DAYS * 24 * 60 * 60 * 1000;
        this.dueDate = new Date(dueMs);
        bookItem.setDueDate(this.dueDate);
    }

    public boolean returnBook() {
        this.returnDate = new Date();
        System.out.println("[INFO] Book returned: " + bookItem.getTitle());
        System.out.println("       Return date: " + returnDate);
        return true;
    }

    public boolean renewBook(boolean isReservedByOther) {
        if (isReservedByOther) {
            System.out.println("[ERROR] Book is reserved by another member. Cannot renew.");
            return false;
        }
        long newDueMs = System.currentTimeMillis() + (long) LOAN_DAYS * 24 * 60 * 60 * 1000;
        this.dueDate = new Date(newDueMs);
        bookItem.setDueDate(this.dueDate);
        System.out.println("[OK] Book renewed. New due date: " + dueDate);
        return true;
    }

    public boolean isOverdue() {
        if (returnDate != null) return false; 
        return new Date().after(dueDate);
    }

    public long getOverdueDays() {
        if (!isOverdue()) return 0;
        long diffMs = System.currentTimeMillis() - dueDate.getTime();
        return diffMs / (24L * 60 * 60 * 1000);
    }

    public Date     getCreationDate() { return creationDate; }
    public Date     getDueDate()      { return dueDate; }
    public Date     getReturnDate()   { return returnDate; }
    public BookItem getBookItem()     { return bookItem; }
    public Member   getMember()       { return member; }

    @Override
    public String toString() {
        return "BookLending{book='" + bookItem.getTitle()
               + "', member='" + member.getPerson().getName()
               + "', dueDate=" + dueDate
               + ", overdue=" + isOverdue() + "}";
    }
}
