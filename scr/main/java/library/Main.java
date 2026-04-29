package library;

import library.accounts.Librarian;
import library.accounts.Member;
import library.enums.AccountStatus;
import library.enums.BookFormat;
import library.enums.BookStatus;
import library.library.LibraryService;
import library.models.*;
import library.search.Catalog;

import java.util.*;

public class Main {

    private static final Scanner      scanner    = new Scanner(System.in);
    private static LibraryService     service;
    private static Catalog            catalog;
    private static Librarian          librarian;
    private static Member             currentMember;
    private static final List<Member> allMembers = new ArrayList<>();

    private static final String RESET  = "\u001B[0m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE   = "\u001B[34m";
    private static final String CYAN   = "\u001B[36m";
    private static final String BOLD   = "\u001B[1m";

    public static void main(String[] args) {
        clearScreen();
        printHeader();
        initializeSystem();
        while (true) {
            printMainMenu();
            System.out.print(BOLD+CYAN+"Choose (1-5): "+RESET);
            String c = scanner.nextLine().trim();
            switch(c) {
                case "1": loginMenu();    break;
                case "2": registerMenu(); break;
                case "3": aboutSystem();  break;
                case "4": viewAllBooks(); break;
                case "5":
                    System.out.println(GREEN+"Goodbye!"+RESET); return;
                default:
                    System.out.println(RED+"Invalid choice!"+RESET);
            }
        }
    }

    private static void initializeSystem() {
        Address la = new Address("Mustaqillik 1","Tashkent","Tashkent","100000","Uzbekistan");
        Library lib = Library.getInstance("Alisher Navoiy Library", la);
        service = new LibraryService(lib);
        catalog = service.getCatalog();

        Person lp = new Person("Zulfiya Kholmatova",
                new Address("Chilanzar 5","Tashkent","Tashkent","100100","Uzbekistan"),
                "zulfiya@library.uz","+998901234567"){};
        librarian = new Librarian("LIB-001","lib123",lp);

        addSampleBooks();

        Person dp = new Person("Jasur Toshmatov",
                new Address("Yunusobod 12","Tashkent","Tashkent","100200","Uzbekistan"),
                "jasur@email.uz","+998907654321"){};
        Member dm = new Member("MEM-001","1234",dp);
        librarian.registerNewMember(dm);
        allMembers.add(dm);

        System.out.println(GREEN+"System ready! "+catalog.getTotalBooks()+" books."+RESET);
        System.out.println("Demo: jasur@email.uz / 1234\n");
    }

    private static void addSampleBooks() {
        Author a1=new Author("Robert C. Martin","");
        Author a2=new Author("Abdulla Qodiriy","");
        Author a3=new Author("Ernest Hemingway","");
        librarian.addBookItem(new BookItem("1","Clean Code","Programming",
                "Prentice Hall","English",431,a1,"BAR-1001",BookFormat.PAPERBACK,45.0),catalog);
        librarian.addBookItem(new BookItem("2","Days Gone By","Novel",
                "Sharq","Uzbek",320,a2,"BAR-1002",BookFormat.HARDCOVER,25.0),catalog);
        librarian.addBookItem(new BookItem("3","The C Programming Language","Programming",
                "Prentice Hall","English",272,a1,"BAR-1003",BookFormat.PAPERBACK,35.0),catalog);
        librarian.addBookItem(new BookItem("4","The Old Man and the Sea","Novel",
                "Scribner","English",127,a3,"BAR-1004",BookFormat.PAPERBACK,15.0),catalog);
    }

    private static void printHeader() {
        System.out.println(CYAN+BOLD);
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      LIBRARY MANAGEMENT SYSTEM      ║");
        System.out.println("╚══════════════════════════════════════╝"+RESET);
    }

    private static void printMainMenu() {
        System.out.println("\n"+YELLOW+BOLD+"[ MAIN MENU ]"+RESET);
        System.out.println(GREEN+"1."+RESET+" Login");
        System.out.println(GREEN+"2."+RESET+" Register");
        System.out.println(GREEN+"3."+RESET+" About");
        System.out.println(GREEN+"4."+RESET+" All books");
        System.out.println(GREEN+"5."+RESET+" Exit");
    }

    private static void loginMenu() {
        System.out.print(BLUE+"Email: "+RESET);
        String email = scanner.nextLine().trim();
        System.out.print(BLUE+"Password: "+RESET);
        String pass = scanner.nextLine().trim();

        for (Member m : allMembers) {
            if (m.getPerson().getEmail().equalsIgnoreCase(email)) {
                if (m.getStatus() != AccountStatus.ACTIVE) {
                    System.out.println(RED+"Your account is not active: "+m.getStatus()+RESET);
                    waitForEnter(); return;
                }
                if (m.login(pass)) {
                    currentMember = m;
                    System.out.println(GREEN+"Welcome, "+m.getPerson().getName()+"!"+RESET);
                    waitForEnter(); userMenu(); return;
                } else {
                    System.out.println(RED+"Wrong password!"+RESET);
                    waitForEnter(); return;
                }
            }
        }
        System.out.println(RED+"Email not found!"+RESET);
        waitForEnter();
    }

    private static void registerMenu() {
        System.out.print(BLUE+"Name: "+RESET);    String name=scanner.nextLine().trim();
        System.out.print(BLUE+"Email: "+RESET);   String email=scanner.nextLine().trim();
        for (Member m : allMembers) {
            if (m.getPerson().getEmail().equalsIgnoreCase(email)) {
                System.out.println(RED+"This email is already registered!"+RESET);
                waitForEnter(); return; } }
        System.out.print(BLUE+"Phone: "+RESET); String phone=scanner.nextLine().trim();
        System.out.print(BLUE+"Password (min 4): "+RESET); String pass=scanner.nextLine().trim();
        if (pass.length()<4) {
            System.out.println(RED+"Password must be at least 4 characters!"+RESET); waitForEnter(); return; }
        String mid="MEM-"+System.currentTimeMillis();
        Address a=new Address("","Tashkent","Tashkent","100000","Uzbekistan");
        Person p=new Person(name,a,email,phone){};
        Member m=new Member(mid,pass,p);
        librarian.registerNewMember(m);
        allMembers.add(m);
        System.out.println(GREEN+"Registration successful!"+RESET);
        waitForEnter();
    }

    private static void userMenu() {
        while (true) {
            clearScreen();
            System.out.println(CYAN+currentMember.getPerson().getName()
                    +" | "+currentMember.getTotalBooksCheckedout()+"/5"+RESET);
            System.out.println(YELLOW+"[ USER MENU ]"+RESET);
            System.out.println(GREEN+"1."+RESET+" Search books");
            System.out.println(GREEN+"2."+RESET+" Checkout book");
            System.out.println(GREEN+"3."+RESET+" Return book");
            System.out.println(GREEN+"4."+RESET+" Reserve book");
            System.out.println(GREEN+"5."+RESET+" My books");
            System.out.println(GREEN+"6."+RESET+" All books");
            System.out.println(GREEN+"7."+RESET+" My profile");
            System.out.println(GREEN+"8."+RESET+" Logout");
            System.out.print(BOLD+CYAN+"Choose (1-8): "+RESET);
            String c = scanner.nextLine().trim();
            switch(c) {
                case "1": searchBook();   break;
                case "2": checkoutBook(); break;
                case "3": returnBook();   break;
                case "4": reserveBook();  break;
                case "5": myBooks();      break;
                case "6": viewAllBooks(); break;
                case "7": showProfile();  break;
                case "8": currentMember=null; return;
                default:  System.out.println(RED+"Invalid!"+RESET);
            }
        }
    }

    private static void searchBook() {
        System.out.print(BLUE+"Search (title/author): "+RESET);
        String q = scanner.nextLine().trim();
        // FIXED: Partial search — "clean" → "Clean Code" found
        List<BookItem> res = new ArrayList<>(catalog.searchByTitle(q));
        for (BookItem b : catalog.searchByAuthor(q))
            if (!res.contains(b)) res.add(b);
        if (res.isEmpty()) {
            System.out.println(RED+"Not found."+RESET);
        } else {
            System.out.println(GREEN+res.size()+" found:"+RESET);
            for (BookItem b : res) {
                String s = b.getStatus()==BookStatus.AVAILABLE
                        ? GREEN+"[Available]" : RED+"["+b.getStatus()+"]";
                System.out.println("  "+s+" "+b.getTitle()+" — "+b.getAuthor().getName()+RESET);
            }
        }
        waitForEnter();
    }

    private static void checkoutBook() {
        System.out.print(BLUE+"Book title: "+RESET);
        String t = scanner.nextLine().trim();
        List<BookItem> books = catalog.searchByTitle(t);
        if (books.isEmpty()) {
            System.out.println(RED+"Not found!"+RESET); waitForEnter(); return; }
        BookItem book = books.stream()
                .filter(b -> b.getStatus()==BookStatus.AVAILABLE)
                .findFirst().orElse(null);
        if (book==null) {
            System.out.println(YELLOW+"Book not available: "+books.get(0).getStatus()+RESET);
            waitForEnter(); return; }
        if (service.checkoutBook(currentMember, book, new BarcodeReader("R-01")))
            System.out.println(GREEN+"Checked out: "+book.getTitle()+" | Due: "+book.getDueDate()+RESET);
        else
            System.out.println(RED+"Error! Possibly reached limit."+RESET);
        waitForEnter();
    }

    private static void returnBook() {
        List<BookItem> borrowed = currentMember.getBorrowedBooks();
        if (borrowed.isEmpty()) {
            System.out.println(YELLOW+"No borrowed books."+RESET); waitForEnter(); return; }
        System.out.println(BLUE+"Borrowed books:"+RESET);
        borrowed.forEach(b -> System.out.println("  - "+b.getTitle()));
        System.out.print(BLUE+"Title to return: "+RESET);
        String t = scanner.nextLine().trim();
        // FIXED: Searches only within borrowed books
        BookItem book = borrowed.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(t.toLowerCase()))
                .findFirst().orElse(null);
        if (book==null) {
            System.out.println(RED+"You did not borrow this book!"+RESET); waitForEnter(); return; }
        if (service.returnBook(currentMember, book, new BarcodeReader("R-01"), librarian))
            System.out.println(GREEN+"Returned: "+book.getTitle()+RESET);
        else
            System.out.println(RED+"Error!"+RESET);
        waitForEnter();
    }

    private static void reserveBook() {
        System.out.print(BLUE+"Book title: "+RESET);
        String t = scanner.nextLine().trim();
        List<BookItem> books = catalog.searchByTitle(t);
        if (books.isEmpty()) {
            System.out.println(RED+"Not found!"+RESET); waitForEnter(); return; }
        currentMember.reserveBookItem(books.get(0));
        waitForEnter();
    }

    private static void myBooks() {
        System.out.println(CYAN+"My books ("+currentMember.getTotalBooksCheckedout()+"/5):"+RESET);
        List<BookItem> bk = currentMember.getBorrowedBooks();
        if (bk.isEmpty()) { System.out.println("  Nothing."); }
        else bk.forEach(b -> {
            boolean ov = b.getDueDate()!=null && new Date().after(b.getDueDate());
            System.out.println("  "+(ov?RED+"[!]":GREEN+"[ok]")+" "
                    +b.getTitle()+" | "+b.getDueDate()+(ov?" OVERDUE!":"")+RESET);
        });
        waitForEnter();
    }

    private static void viewAllBooks() {
        System.out.println(CYAN+"All books ("+catalog.getTotalBooks()+"):"+RESET);
        for (BookItem b : catalog.getAllBooks()) {
            String s = b.getStatus()==BookStatus.AVAILABLE
                    ? GREEN+"[Available]" : RED+"["+b.getStatus()+"]";
            System.out.println("  "+s+" "+b.getTitle()+" — "+b.getAuthor().getName()+RESET);
        }
        waitForEnter();
    }

    private static void showProfile() {
        System.out.println(CYAN+"PROFILE"+RESET);
        System.out.println("  Name:    "+currentMember.getPerson().getName());
        System.out.println("  Email:   "+currentMember.getPerson().getEmail());
        System.out.println("  Phone:   "+currentMember.getPerson().getPhone());
        System.out.println("  ID:      "+currentMember.getId());
        System.out.println("  Books:   "+currentMember.getTotalBooksCheckedout()+"/5");
        waitForEnter();
    }

    private static void aboutSystem() {
        System.out.println(CYAN+"Library Management System — OOP Java project."+RESET);
        waitForEnter();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J"); System.out.flush(); }

    private static void waitForEnter() {
        System.out.print(YELLOW+"[Press Enter...]"+RESET); scanner.nextLine(); }
}
