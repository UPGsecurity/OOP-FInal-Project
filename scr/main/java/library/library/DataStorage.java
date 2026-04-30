package library.library;

import library.models.BookItem;
import library.models.Author;
import library.enums.BookFormat;
import library.enums.BookStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private static final String BOOKS_FILE = "books.txt";
    private static final String USERS_FILE = "users.txt";

    public static class UserData {
        public String name, email, phone, password, memberId;
        
        public UserData(String name, String email, String phone,
                        String password, String memberId) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.memberId = memberId;
        }
    }

    public static void saveBooks(List<BookItem> books) {
        try (PrintWriter w = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (BookItem b : books) {
                String authorName = (b.getAuthor() != null) ? b.getAuthor().getName() : "";
                w.println(
                        esc(b.getISBN()) + "|" +
                        esc(b.getTitle()) + "|" +
                        esc(b.getSubject()) + "|" +
                        esc(b.getPublisher()) + "|" +
                        esc(b.getLanguage()) + "|" +
                        b.getNumberOfPages() + "|" +
                        esc(authorName) + "|" +
                        esc(b.getBarcode()) + "|" +
                        b.getFormat().name() + "|" +
                        b.getPrice() + "|" +
                        b.getStatus().name()
                );
            }
            System.out.println("[OK] " + books.size() + " books saved to " + BOOKS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    public static List<BookItem> loadBooks() {
        File f = new File(BOOKS_FILE);
        if (!f.exists()) {
            System.out.println("[INFO] " + BOOKS_FILE + " not found, using sample data.");
            return null;
        }
        
        List<BookItem> books = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            int lineNum = 0;
            while ((line = r.readLine()) != null) {
                lineNum++;
                if (line.isBlank()) continue;
                
                String[] p = line.split("\\|", -1);
                if (p.length < 11) {
                    System.out.println("[WARN] Line " + lineNum + " has " + p.length + " fields, skipping");
                    continue;
                }
                
                try {
                    Author author = new Author(p[6], "");
                    BookItem book = new BookItem(
                            p[0], p[1], p[2], p[3], p[4],
                            Integer.parseInt(p[5]), author, p[7],
                            BookFormat.valueOf(p[8]), Double.parseDouble(p[9])
                    );
                    book.setStatus(BookStatus.valueOf(p[10]));
                    books.add(book);
                } catch (Exception e) {
                    System.out.println("[WARN] Failed to parse line " + lineNum + ": " + e.getMessage());
                }
            }
            System.out.println("[OK] Loaded " + books.size() + " books from " + BOOKS_FILE);
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    public static void saveUsers(List<UserData> users) {
        try (PrintWriter w = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (UserData u : users) {
                w.println(
                        esc(u.name) + "|" +
                        esc(u.email) + "|" +
                        esc(u.phone) + "|" +
                        esc(u.password) + "|" +
                        esc(u.memberId)
                );
            }
            System.out.println("[OK] " + users.size() + " users saved to " + USERS_FILE);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public static List<UserData> loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) {
            System.out.println("[INFO] " + USERS_FILE + " not found, using sample users.");
            return null;
        }
        
        List<UserData> users = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            int lineNum = 0;
            while ((line = r.readLine()) != null) {
                lineNum++;
                if (line.isBlank()) continue;
                
                String[] p = line.split("\\|", -1);
                if (p.length < 5) {
                    System.out.println("[WARN] Line " + lineNum + " has " + p.length + " fields, skipping");
                    continue;
                }
                
                users.add(new UserData(p[0], p[1], p[2], p[3], p[4]));
            }
            System.out.println("[OK] Loaded " + users.size() + " users from " + USERS_FILE);
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("|", "\\pipe");
    }
}
