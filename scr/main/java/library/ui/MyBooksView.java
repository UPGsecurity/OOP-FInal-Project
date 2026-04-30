package library.ui;

import library.LibraryFX;
import library.accounts.Member;
import library.models.BookItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Date;
import java.util.List;

public class MyBooksView {
    private final LibraryFX app;
    private final Member member;

    public MyBooksView(LibraryFX app, Member member) {
        this.app = app;
        this.member = member;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color:#ecf0f1;");
        Label title = new Label("MY BOOKS");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");
        Label countL = new Label();
        ListView<String> lv = new ListView<>();

        Runnable refresh = () -> {
            lv.getItems().clear();
            if (member == null) {
                lv.getItems().add("Please login first!");
                return;
            }
            int cnt = member.getTotalBooksCheckedout();
            countL.setText("Total: " + cnt + " / 5");
            countL.setStyle("-fx-text-fill:" + (cnt >= 5 ? "red" : "#2c3e50") + ";");
            List<BookItem> bk = member.getBorrowedBooks();
            if (bk.isEmpty()) {
                lv.getItems().add("No books borrowed");
                return;
            }
            for (BookItem b : bk) {
                String due = b.getDueDate() != null ? b.getDueDate().toString() : "—";
                boolean ov = b.getDueDate() != null && new Date().after(b.getDueDate());
                lv.getItems().add((ov ? "[!] " : "[ok] ") + b.getTitle()
                        + " | Due: " + due + (ov ? " [OVERDUE!]" : ""));
            }
        };
        refresh.run();

        Button refBtn = new Button("Refresh");
        refBtn.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;");
        refBtn.setOnAction(e -> refresh.run());
        Button back = new Button("Back");
        back.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");
        back.setOnAction(e -> app.showMainMenu(member));

        HBox bx = new HBox(10, refBtn, back);
        bx.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(title, countL, lv, bx);
        return new Scene(layout, 550, 500);
    }
}
