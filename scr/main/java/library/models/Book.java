package library.models;

public class Book {
    private String ISBN;
    private String title;
    private String subject;
    private String publisher;
    private String language;
    private int    numberOfPages;
    private Author author;

    public Book(String ISBN, String title, String subject,
                String publisher, String language,
                int numberOfPages, Author author) {
        this.ISBN          = ISBN;
        this.title         = title;
        this.subject       = subject;
        this.publisher     = publisher;
        this.language      = language;
        this.numberOfPages = numberOfPages;
        this.author        = author;
    }

    public String getISBN()          { return ISBN; }
    public String getTitle()         { return title; }
    public String getSubject()       { return subject; }
    public String getPublisher()     { return publisher; }
    public String getLanguage()      { return language; }
    public int    getNumberOfPages() { return numberOfPages; }
    public Author getAuthor()        { return author; }

    public void setTitle(String title)     { this.title = title; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setAuthor(Author author)   { this.author = author; }

    @Override
    public String toString() {
        return "Book{ISBN='" + ISBN + "', title='" + title +
               "', author=" + (author != null ? author.getName() : "null") + "}";
    }
}
