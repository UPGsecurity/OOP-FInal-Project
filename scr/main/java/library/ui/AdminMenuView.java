package library.ui;

import library.LibraryFX;
import library.accounts.Librarian;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AdminMenuView {
    private final LibraryFX app;
    private final Librarian librarian;

    public AdminMenuView(LibraryFX app, Librarian librarian) {
        this.app = app;
        this.librarian = librarian;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#ecf0f1;");

        VBox top = new VBox(5);
        top.setStyle("-fx-background-color:#2c3e50;");
        top.setPadding(new Insets(15));
        Label wl = new Label("Admin panel: " + librarian.getPerson().getName());
        wl.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:white;");
        top.getChildren().add(wl);

        VBox menu = new VBox(15);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(30));

        Button b1 = mkBtn("Add new book", "#2ecc71");
        Button b2 = mkBtn("Delete book", "#e74c3c");
        Button b3 = mkBtn("Edit book details", "#3498db");
        Button b4 = mkBtn("Manage users", "#9b59b6");
        Button b5 = mkBtn("All books", "#f39c12");
        Button b6 = mkBtn("All users", "#1abc9c");
        Button b7 = mkBtn("Logout", "#7f8c8d");

        b1.setOnAction(e -> app.showAddBookView());
        b2.setOnAction(e -> app.showDeleteBookView());
        b3.setOnAction(e -> app.showEditBookView());
        b4.setOnAction(e -> app.showManageUsersView());
        b5.setOnAction(e -> app.showAllBooksView());
        b6.setOnAction(e -> app.showAllUsersView());
        b7.setOnAction(e -> app.showLoginScreen());

        menu.getChildren().addAll(b1, b2, b3, b4, b5, b6, b7);
        root.setTop(top);
        root.setCenter(menu);
        return new Scene(root, 550, 600);
    }

    private Button mkBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;" +
                "-fx-font-size:14px;-fx-padding:12px 20px;");
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }
}
