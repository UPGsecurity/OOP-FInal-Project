package library.ui;

import library.LibraryFX;
import library.accounts.Librarian;
import library.accounts.Member;
import library.models.BarcodeReader;
import library.models.BookItem;
import library.transactions.BookLending;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ReturnView {
    private final LibraryFX app;
    private final Member member;
    private final Librarian librarian;

    public ReturnView(LibraryFX app, Member member, Librarian librarian) {
        this.app = app;
        this.member = member;
        this.librarian = librarian;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color:#ecf0f1;");
        Label title = new Label("RETURN BOOK");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");
        TextField tf = new TextField();
        tf.setPromptText("Book title to return");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");
        msg.setWrapText(true);
        Button btn = new Button("Return");
        btn.setStyle("-fx-background-color:#f39c12;-fx-text-fill:white;");
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
            BookItem book = member.getBorrowedBooks().stream()
                    .filter(b -> b.getTitle().toLowerCase().contains(t.toLowerCase()))
                    .findFirst().orElse(null);
            if (book == null) {
                msg.setStyle("-fx-text-fill:red;");
                msg.setText("You did not borrow this book!");
                return;
            }

            double fineAmount = 0.0;
            BookLending lending = member.getActiveLendings().stream()
                    .filter(l -> l.getBookItem().equals(book))
                    .findFirst().orElse(null);
            if (lending != null && lending.isOverdue()) {
                fineAmount = lending.getOverdueDays() * 1.0;
            }

            if (app.getService().returnBook(member, book, new BarcodeReader("R-01"), librarian)) {
                msg.setStyle("-fx-text-fill:green;");
                String fineText = (fineAmount > 0) ? " Fine: $" + fineAmount : "";
                msg.setText("Book returned: " + book.getTitle() + fineText);
                tf.clear();
                app.saveAllData();
            } else {
                msg.setStyle("-fx-text-fill:red;");
                msg.setText("Error during return!");
            }
        });
        tf.setOnAction(e -> btn.fire());
        back.setOnAction(e -> app.showMainMenu(member));

        layout.getChildren().addAll(title, tf, btn, msg, back);
        return new Scene(layout, 500, 400);
    }
}
