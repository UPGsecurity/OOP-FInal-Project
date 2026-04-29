package library.models;

import library.enums.BookFormat;
import library.enums.BookStatus;

import java.util.Date;

public class BookItem extends Book {

    private String     barcode;
    private boolean    isReferenceOnly;  
    private Date       borrowed;
    private Date       dueDate;
    private double     price;
    private BookFormat format;
    private BookStatus status;
    private Date       dateOfPurchase;
    private Date       publicationDate;
    private Rack       placedAt;

    private static final int LOAN_DAYS = 14; 

    public BookItem(String ISBN, String title, String subject,
                    String publisher, String language, int numberOfPages,
                    Author author, String barcode, BookFormat format, double price) {
        super(ISBN, title, subject, publisher, language, numberOfPages, author);
        this.barcode         = barcode;
        this.format          = format;
        this.price           = price;
        this.status          = BookStatus.AVAILABLE;
        this.isReferenceOnly = false;
        this.dateOfPurchase  = new Date();
    }

    public boolean checkout() {
        if (isReferenceOnly) {
            System.out.println("[ERROR] This book is for in-library use only.");
            return false;
        }
        if (status != BookStatus.AVAILABLE) {
            System.out.println("[ERROR] Book is not currently available. Status: " + status);
            return false;
        }
        this.status   = BookStatus.LOANED;
        this.borrowed = new Date();
        
        long dueMs = System.currentTimeMillis() + (long) LOAN_DAYS * 24 * 60 * 60 * 1000;
        this.dueDate = new Date(dueMs);
        
        return true;
    }
    
    public String     getBarcode()         { return barcode; }
    public boolean    isReferenceOnly()    { return isReferenceOnly; }
    public Date       getBorrowed()        { return borrowed; }
    public Date       getDueDate()         { return dueDate; }
    public double     getPrice()           { return price; }
    public BookFormat getFormat()          { return format; }
    public BookStatus getStatus()          { return status; }
    public Date       getDateOfPurchase()  { return dateOfPurchase; }
    public Date       getPublicationDate() { return publicationDate; }
    public Rack       getPlacedAt()        { return placedAt; }

    public void setStatus(BookStatus status)             { this.status = status; }
    public void setDueDate(Date dueDate)                 { this.dueDate = dueDate; }
    public void setReferenceOnly(boolean referenceOnly)  { isReferenceOnly = referenceOnly; }
    public void setPlacedAt(Rack placedAt)               { this.placedAt = placedAt; }
    public void setPublicationDate(Date publicationDate) { this.publicationDate = publicationDate; }
    public void setBorrowed(Date borrowed)               { this.borrowed = borrowed; }

    @Override
    public String toString() {
        return "BookItem{barcode='" + barcode + "', title='" + getTitle() +
               "', status=" + status + "}";
    }
}
