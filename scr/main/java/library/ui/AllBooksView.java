package library.ui;

import library.LibraryFX;
import library.models.BookItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AllBooksView {
    private final LibraryFX app;

    public AllBooksView(LibraryFX app) { this.app = app; }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:#ecf0f1;");

        Label title = new Label("ALL BOOKS");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

        ListView<String> list = new ListView<>();
        for (BookItem b : app.getCatalog().getAllBooks()) {
            list.getItems().add(b.getTitle() + " | " + b.getAuthor().getName() + " | " + b.getStatus());
        }

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showAdminMenu(app.getLibrarian()));

        layout.getChildren().addAll(title, list, backBtn);
        return new Scene(layout, 600, 500);
    }
}
