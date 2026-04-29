package library.library;

import library.models.*;
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
            this.name=name; this.email=email; this.phone=phone;
            this.password=password; this.memberId=memberId;
        }
    }


    public static void saveBooks(List<BookItem> books) {
        try (PrintWriter w = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (BookItem b : books) {
                w.println(
                        esc(b.getISBN())        + "|" +
                                esc(b.getTitle())        + "|" +
                                esc(b.getSubject())      + "|" +
                                esc(b.getPublisher())    + "|" +
                                esc(b.getLanguage())     + "|" +
                                b.getNumberOfPages()     + "|" +
                                esc(b.getAuthor()!=null ? b.getAuthor().getName() : "") + "|" +
                                esc(b.getBarcode())      + "|" +
                                b.getFormat().name()     + "|" +
                                b.getPrice()             + "|" +
                                b.getStatus().name()
                );
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public static List<BookItem> loadBooks() {
        File f = new File(BOOKS_FILE);
        if (!f.exists()) return null;
        List<BookItem> books = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 11) continue;
                try {
                    Author author = new Author(p[6], "");
                    BookItem book = new BookItem(p[0],p[1],p[2],p[3],p[4],
                            Integer.parseInt(p[5]), author, p[7],
                            BookFormat.valueOf(p[8]), Double.parseDouble(p[9]));
                    book.setStatus(BookStatus.valueOf(p[10]));
                    books.add(book);
                } catch (Exception e) {
                    System.out.println("Failed to read line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }


    public static void saveUsers(List<UserData> users) {
        try (PrintWriter w = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (UserData u : users) {
                w.println(esc(u.name)+"|"+esc(u.email)+"|"+
                        esc(u.phone)+"|"+esc(u.password)+"|"+esc(u.memberId));
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public static List<UserData> loadUsers() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return null;
        List<UserData> users = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 5) continue;
                users.add(new UserData(p[0],p[1],p[2],p[3],p[4]));
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace("|", "\\pipe");
    }
}
