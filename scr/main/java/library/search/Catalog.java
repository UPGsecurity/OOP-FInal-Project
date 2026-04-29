package library.search;

import library.exceptions.BookNotFoundException;
import library.models.BookItem;

import java.util.*;
import java.util.stream.Collectors;

public class Catalog implements Search {
    private Date creationDate;
    private int  totalBooks;
    private final Map<String, List<BookItem>> bookTitles   = new HashMap<>();
    private final Map<String, List<BookItem>> bookAuthors  = new HashMap<>();
    private final Map<String, List<BookItem>> bookSubjects = new HashMap<>();
    private final List<BookItem>              allBooks     = new ArrayList<>();

    public Catalog() {
        this.creationDate = new Date();
        this.totalBooks   = 0;
    }

    @Override
    public List<BookItem> searchByTitle(String title) {
        if (title == null || title.isBlank()) return new ArrayList<>(allBooks);
        String q = title.toLowerCase().trim();
        List<BookItem> result = allBooks.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(q))
                .collect(Collectors.toList());
        System.out.println("[SEARCH] Title: '" + title + "' - " + result.size() + " found");
        return result;
    }

    @Override
    public List<BookItem> searchByAuthor(String author) {
        if (author == null || author.isBlank()) return new ArrayList<>(allBooks);
        String q = author.toLowerCase().trim();
        List<BookItem> result = allBooks.stream()
                .filter(b -> b.getAuthor() != null
                        && b.getAuthor().getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
        System.out.println("[SEARCH] Author: '" + author + "' - " + result.size() + " found");
        return result;
    }

    @Override
    public List<BookItem> searchBySubject(String subject) {
        if (subject == null || subject.isBlank()) return new ArrayList<>(allBooks);
        String q = subject.toLowerCase().trim();
        List<BookItem> result = allBooks.stream()
                .filter(b -> b.getSubject().toLowerCase().contains(q))
                .collect(Collectors.toList());
        System.out.println("[SEARCH] Subject: '" + subject + "' - " + result.size() + " found");
        return result;
    }

    @Override
    public List<BookItem> searchByPubDate(Date publishDate) {
        return new ArrayList<>();
    }

    public BookItem findBookByTitle(String title) throws BookNotFoundException {
        List<BookItem> results = searchByTitle(title);
        if (results.isEmpty()) throw new BookNotFoundException(title);
        return results.get(0);
    }

    public boolean updateCatalog(BookItem bookItem) {
        if (bookItem == null) return false;
        String tk = bookItem.getTitle().toLowerCase().trim();
        String ak = bookItem.getAuthor() != null
                ? bookItem.getAuthor().getName().toLowerCase().trim() : "unknown";
        String sk = bookItem.getSubject().toLowerCase().trim();
        bookTitles.computeIfAbsent(tk, k -> new ArrayList<>()).add(bookItem);
        bookAuthors.computeIfAbsent(ak, k -> new ArrayList<>()).add(bookItem);
        bookSubjects.computeIfAbsent(sk, k -> new ArrayList<>()).add(bookItem);
        allBooks.add(bookItem);
        totalBooks++;
        return true;
    }

    public boolean removeFromCatalog(BookItem bookItem) {
        if (bookItem == null || !allBooks.contains(bookItem)) return false;
        String tk = bookItem.getTitle().toLowerCase().trim();
        String ak = bookItem.getAuthor() != null
                ? bookItem.getAuthor().getName().toLowerCase().trim() : "unknown";
        String sk = bookItem.getSubject().toLowerCase().trim();
        if (bookTitles.get(tk) != null)   bookTitles.get(tk).remove(bookItem);
        if (bookAuthors.get(ak) != null)  bookAuthors.get(ak).remove(bookItem);
        if (bookSubjects.get(sk) != null) bookSubjects.get(sk).remove(bookItem);
        allBooks.remove(bookItem);
        totalBooks--;
        return true;
    }

    public List<BookItem> getAllBooks()    { return Collections.unmodifiableList(allBooks); }
    public int            getTotalBooks() { return totalBooks; }
    public Date           getCreationDate(){ return creationDate; }
}
