package library.ui;

import library.LibraryFX;
import library.accounts.Member;
import library.enums.BookStatus;
import library.models.BarcodeReader;
import library.models.BookItem;
import library.search.Catalog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class CheckoutView {
    private final LibraryFX app;
    private final Catalog catalog;
    private final Member member;

    public CheckoutView(LibraryFX app, Catalog catalog, Member member) {
        this.app = app;
        this.catalog = catalog;
        this.member = member;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color:#ecf0f1;");
        Label title = new Label("CHECKOUT BOOK");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");
        TextField tf = new TextField();
        tf.setPromptText("Enter book title");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");
        msg.setWrapText(true);
        Button btn = new Button("Checkout");
        btn.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;");
        Button back = new Button("Back");
        back.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");

        btn.setOnAction(e -> {
            if (member == null) {
                msg.setText("Please login first!");
                return;
            }
            String t = tf.getText().trim();
            if (t.isEmpty()) {
                msg.setText("Enter a title!");
                return;
            }
            List<BookItem> books = catalog.searchByTitle(t);
            if (books.isEmpty()) {
                msg.setStyle("-fx-text-fill:red;");
                msg.setText("Book not found!");
                return;
            }
            BookItem book = books.stream()
                    .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                    .findFirst().orElse(null);
            if (book == null) {
                msg.setStyle("-fx-text-fill:orange;");
                msg.setText("Book is not available: " + books.get(0).getStatus());
                return;
            }
            if (app.getService().checkoutBook(member, book, new BarcodeReader("R-01"))) {
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Book checked out: " + book.getTitle() +
                        " Due: " + book.getDueDate());
                tf.clear();
                app.saveAllData();
            } else {
                msg.setStyle("-fx-text-fill:red;");
                msg.setText("Error! Possibly reached limit.");
            }
        });
        tf.setOnAction(e -> btn.fire());
        back.setOnAction(e -> app.showMainMenu(member));

        layout.getChildren().addAll(title, tf, btn, msg, back);
        return new Scene(layout, 500, 400);
    }
}
