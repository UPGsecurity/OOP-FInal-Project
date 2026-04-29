package library.ui;

import library.LibraryFX;
import library.models.BookItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class DeleteBookView {
    private final LibraryFX app;

    public DeleteBookView(LibraryFX app) {
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:#ecf0f1;");

        Label title = new Label("DELETE BOOK");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Book title or barcode");
        ListView<String> resultsList = new ListView<>();
        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;");
        Button deleteBtn = new Button("Delete selected book");
        deleteBtn.setStyle("-fx-background-color:#e74c3c;-fx-text-fill:white;");
        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");

        searchBtn.setOnAction(e -> {
            String q = searchField.getText().trim();
            if (q.isEmpty()) return;
            resultsList.getItems().clear();
            List<BookItem> books = app.getCatalog().searchByTitle(q);
            for (BookItem b : books) {
                resultsList.getItems().add(b.getTitle() + " | " + b.getBarcode() + " | " + b.getAuthor().getName());
            }
            if (books.isEmpty()) msg.setText("Book not found!");
            else msg.setText("");
        });

        deleteBtn.setOnAction(e -> {
            int idx = resultsList.getSelectionModel().getSelectedIndex();
            if (idx < 0) {
                msg.setText("Select a book!");
                return;
            }
            String line = resultsList.getItems().get(idx);
            String titlePart = line.split(" \\| ")[0];
            List<BookItem> books = app.getCatalog().searchByTitle(titlePart);
            if (!books.isEmpty()) {
                app.getLibrarian().removeBookItem(books.get(0), app.getCatalog());
                app.saveAllData();
                resultsList.getItems().remove(idx);
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Book deleted: " + titlePart);
            }
        });

        backBtn.setOnAction(e -> app.showAdminMenu(app.getLibrarian()));

        layout.getChildren().addAll(title, searchField, searchBtn, resultsList, deleteBtn, backBtn, msg);
        return new Scene(layout, 550, 550);
    }
}
