package library.ui;

import library.LibraryFX;
import library.accounts.Member;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainMenuView {
    private final LibraryFX app;
    private final Member member;

    public MainMenuView(LibraryFX app, Member member) {
        this.app = app;
        this.member = member;
    }

    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#ecf0f1;");

        VBox top = new VBox(5);
        top.setStyle("-fx-background-color:#2c3e50;");
        top.setPadding(new Insets(15));
        String name = member != null ? member.getPerson().getName() : "Guest";
        int cnt = member != null ? member.getTotalBooksCheckedout() : 0;
        Label wl = new Label("Welcome, " + name + "!");
        wl.setStyle("-fx-font-size:18px;-fx-font-weight:bold;-fx-text-fill:white;");
        Label il = new Label("Books borrowed: " + cnt + " / 5");
        il.setStyle("-fx-text-fill:#bdc3c7;");
        top.getChildren().addAll(wl, il);

        VBox menu = new VBox(15);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(30));

        Button s1 = mkBtn("Search books", "#3498db");
        Button s2 = mkBtn("Checkout book", "#2ecc71");
        Button s3 = mkBtn("Return book", "#f39c12");
        Button s4 = mkBtn("My books", "#9b59b6");
        Button s5 = mkBtn("Logout", "#e74c3c");

        s1.setOnAction(e -> app.showSearchScreen());
        s2.setOnAction(e -> app.showCheckoutScreen());
        s3.setOnAction(e -> app.showReturnScreen());
        s4.setOnAction(e -> app.showMyBooksScreen());
        s5.setOnAction(e -> {
            app.logout();
            app.showLoginScreen();
        });

        menu.getChildren().addAll(s1, s2, s3, s4, s5);
        root.setTop(top);
        root.setCenter(menu);
        return new Scene(root, 500, 550);
    }

    private Button mkBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + color + ";-fx-text-fill:white;" +
                "-fx-font-size:14px;-fx-padding:12px 20px;");
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }
}
