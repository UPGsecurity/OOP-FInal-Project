package library;

import library.accounts.Librarian;
import library.accounts.Member;
import library.enums.AccountStatus;
import library.enums.BookFormat;
import library.library.DataStorage;
import library.library.LibraryService;
import library.models.*;
import library.search.Catalog;
import library.ui.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LibraryFX extends Application {

    private static LibraryService service;
    private static Catalog catalog;
    private static Librarian librarian;
    private static Stage primaryStage;
    private static Member currentMember;

    public static class UserAccount {
        public String name, email, phone, password;
        public Member member;
        public UserAccount(String name, String email, String phone, String password, Member member) {
            this.name = name; this.email = email; this.phone = phone;
            this.password = password; this.member = member;
        }
    }

    private final List<UserAccount> registeredUsers = new ArrayList<>();
    private String resetCode = "";
    private UserAccount resetUser = null;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        initializeSystem();
        showLoginScreen();
        stage.setTitle("Library Management System");
        stage.setMinWidth(450);
        stage.setMinHeight(550);
        stage.show();
    }

    @Override
    public void stop() { saveAllData(); }

    // ------------------- Initialization -------------------
    private void initializeSystem() {
        Address libAddr = new Address("Mustaqillik 1","Tashkent","Tashkent","100000","Uzbekistan");
        Library library = Library.getInstance("Alisher Navoiy Library", libAddr);
        service = new LibraryService(library);
        catalog = service.getCatalog();

        Person lp = new Person("Zulfiya Kholmatova",
                new Address("Chilanzar 5","Tashkent","Tashkent","100100","Uzbekistan"),
                "zulfiya@library.uz","+998901234567"){};
        librarian = new Librarian("LIB-001","lib123",lp);

        List<BookItem> savedBooks = DataStorage.loadBooks();
        if (savedBooks != null && !savedBooks.isEmpty()) {
            for (BookItem b : savedBooks) catalog.updateCatalog(b);
        } else {
            addSampleBooks();
        }

        List<DataStorage.UserData> savedUsers = DataStorage.loadUsers();
        if (savedUsers != null && !savedUsers.isEmpty()) {
            for (DataStorage.UserData ud : savedUsers) {
                Address a = new Address("","Tashkent","Tashkent","100000","Uzbekistan");
                Person p = new Person(ud.name, a, ud.email, ud.phone){};
                Member m = new Member(ud.memberId, ud.password, p);
                librarian.registerNewMember(m);
                registeredUsers.add(new UserAccount(ud.name,ud.email,ud.phone,ud.password,m));
            }
        } else {
            Person ap = new Person("Admin",
                    new Address("","Tashkent","Tashkent","100000","Uzbekistan"),
                    "admin@library.com","+998901234567"){};
            Member am = new Member("ADMIN-001","1234",ap);
            librarian.registerNewMember(am);
            registeredUsers.add(new UserAccount("Admin","admin@library.com",
                    "+998901234567","1234",am));
        }
    }

    private void addSampleBooks() {
        Author a1 = new Author("Robert C. Martin","Software expert");
        Author a2 = new Author("Abdulla Qodiriy","Uzbek writer");
        Author a3 = new Author("Ernest Hemingway","American writer");
        librarian.addBookItem(new BookItem("1","Clean Code","Programming",
                "Prentice Hall","English",431,a1,"BAR-1001",BookFormat.PAPERBACK,45.0),catalog);
        librarian.addBookItem(new BookItem("2","Days Gone By","Novel",
                "Sharq","Uzbek",320,a2,"BAR-1002",BookFormat.HARDCOVER,25.0),catalog);
        librarian.addBookItem(new BookItem("3","The C Programming Language","Programming",
                "Prentice Hall","English",272,a1,"BAR-1003",BookFormat.PAPERBACK,35.0),catalog);
        librarian.addBookItem(new BookItem("4","The Old Man and the Sea","Novel",
                "Scribner","English",127,a3,"BAR-1004",BookFormat.PAPERBACK,15.0),catalog);
    }
    public void saveAllData() {
        DataStorage.saveBooks(catalog.getAllBooks());
        List<DataStorage.UserData> ud = new ArrayList<>();
        for (UserAccount ua : registeredUsers) {
            String mid = ua.member != null ? ua.member.getId() : "UNKNOWN";
            ud.add(new DataStorage.UserData(ua.name,ua.email,ua.phone,ua.password,mid));
        }
        DataStorage.saveUsers(ud);
    }

    public Object authenticate(String email, String password) {

        if (librarian.getPerson().getEmail().equalsIgnoreCase(email) && librarian.getPassword().equals(password)) {
            return librarian;
        }
      
        for (UserAccount u : registeredUsers) {
            if (u.email.equalsIgnoreCase(email)) {
                if (u.member != null && u.member.getStatus() != AccountStatus.ACTIVE) return null;
                if (u.password.equals(password)) return u.member;
                else return null;
            }
        }
        return null;
    }

    public boolean registerUser(String name, String email, String phone, String password) {
        for (UserAccount u : registeredUsers) {
            if (u.email.equalsIgnoreCase(email)) return false;
        }
        String mid = "MEM-" + System.currentTimeMillis();
        Address adr = new Address("","Tashkent","Tashkent","100000","Uzbekistan");
        Person per = new Person(name, adr, email, phone){};
        Member mem = new Member(mid, password, per);
        librarian.registerNewMember(mem);
        registeredUsers.add(new UserAccount(name, email, phone, password, mem));
        saveAllData();
        return true;
    }

    public UserAccount findUserByEmail(String email) {
        for (UserAccount u : registeredUsers) {
            if (u.email.equalsIgnoreCase(email)) return u;
        }
        return null;
    }

    public void showLoginScreen() {
        LoginView view = new LoginView(this);
        primaryStage.setScene(view.getScene());
    }

    public void showRegisterScreen() {
        RegisterView view = new RegisterView(this);
        primaryStage.setScene(view.getScene());
    }

    public void showForgotPasswordScreen() {
        ForgotPasswordView view = new ForgotPasswordView(this);
        primaryStage.setScene(view.getScene());
    }

    public void showMainMenu(Member member) {
        currentMember = member;
        MainMenuView view = new MainMenuView(this, member);
        primaryStage.setScene(view.getScene());
    }

    public void showAdminMenu(Librarian librarian) {
        AdminMenuView view = new AdminMenuView(this, librarian);
        primaryStage.setScene(view.getScene());
    }

    public void showSearchScreen() {
        SearchView view = new SearchView(this, catalog);
        primaryStage.setScene(view.getScene());
    }

    public void showCheckoutScreen() {
        CheckoutView view = new CheckoutView(this, catalog, currentMember);
        primaryStage.setScene(view.getScene());
    }

    public void showReturnScreen() {
        ReturnView view = new ReturnView(this, currentMember, librarian);
        primaryStage.setScene(view.getScene());
    }

    public void showMyBooksScreen() {
        MyBooksView view = new MyBooksView(this, currentMember);
        primaryStage.setScene(view.getScene());
    }

    public void showAddBookView() {
        AddBookView view = new AddBookView(this);
        primaryStage.setScene(view.getScene());
    }

    public void showDeleteBookView() {
        DeleteBookView view = new DeleteBookView(this);
        primaryStage.setScene(view.getScene());
    }

    public void showEditBookView() {
        EditBookView view = new EditBookView(this);
        primaryStage.setScene(view.getScene());
    }
    public void logout() {
        currentMember = null;
    }

    public void showManageUsersView() {
        ManageUsersView view = new ManageUsersView(this);
        primaryStage.setScene(view.getScene());
    }

    public void showAllBooksView() {
        AllBooksView view = new AllBooksView(this);
        primaryStage.setScene(view.getScene());
    }

    public void showAllUsersView() {
        AllUsersView view = new AllUsersView(this);
        primaryStage.setScene(view.getScene());
    }

    public LibraryService getService() { return service; }
    public Member getCurrentMember() { return currentMember; }
    public Catalog getCatalog() { return catalog; }
    public Librarian getLibrarian() { return librarian; }
    public List<UserAccount> getRegisteredUsers() { return registeredUsers; }
    public String getResetCode() { return resetCode; }
    public void setResetCode(String code) { this.resetCode = code; }
    public UserAccount getResetUser() { return resetUser; }
    public void setResetUser(UserAccount user) { this.resetUser = user; }
}
