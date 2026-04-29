package library.transactions;

import library.accounts.Member;
import library.enums.ReservationStatus;
import library.models.BookItem;

import java.util.Date;

public class BookReservation {

    private Date              creationDate;
    private ReservationStatus status;
    private BookItem          bookItem;
    private Member            member;

    public BookReservation(BookItem bookItem, Member member) {
        this.bookItem     = bookItem;
        this.member       = member;
        this.creationDate = new Date();
        this.status       = ReservationStatus.WAITING;
    }

    public BookReservation fetchReservationDetails() {
        System.out.println("[INFO] Reservation: " + bookItem.getTitle()
                           + " | Member: " + member.getPerson().getName()
                           + " | Status: " + status);
        return this;
    }

    public ReservationStatus getStatus()      { return status; }
    public BookItem          getBookItem()    { return bookItem; }
    public Member            getMember()      { return member; }
    public Date              getCreationDate(){ return creationDate; }

    public void setStatus(ReservationStatus status) {
        this.status = status;
        System.out.println("[INFO] Reservation status changed: " + status);
    }

    @Override
    public String toString() {
        return "BookReservation{book='" + bookItem.getTitle()
               + "', member='" + member.getPerson().getName()
               + "', status=" + status + "}";
    }
}
