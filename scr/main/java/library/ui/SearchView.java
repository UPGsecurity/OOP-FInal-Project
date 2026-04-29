package library.ui;

import library.LibraryFX;
import library.enums.BookStatus;
import library.models.BookItem;
import library.search.Catalog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class SearchView {
    private final LibraryFX app;
    private final Catalog catalog;
    private ListView<String> searchBookList;
    private TextField searchField;

    public SearchView(LibraryFX app, Catalog catalog) {
        this.app = app;
        this.catalog = catalog;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color:#ecf0f1;");

        Label titleLabel = new Label("SEARCH BOOKS");
        titleLabel.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

        HBox sBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Title or author...");
        searchField.setPrefWidth(300);
        Button sb = new Button("Search");
        sb.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;");
        Button rb = new Button("Refresh");
        rb.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;");
        sBox.getChildren().addAll(searchField, sb, rb);

        searchBookList = new ListView<>();
        updateBookList(catalog.getAllBooks());

        sb.setOnAction(e -> {
            String q = searchField.getText().trim();
            if (q.isEmpty()) {
                updateBookList(catalog.getAllBooks());
                return;
            }
            List<BookItem> res = new ArrayList<>(catalog.searchByTitle(q));
            for (BookItem b : catalog.searchByAuthor(q))
                if (!res.contains(b)) res.add(b);
            updateBookList(res);
        });

        searchField.setOnAction(e -> sb.fire());
        rb.setOnAction(e -> {
            searchField.clear();
            updateBookList(catalog.getAllBooks());
        });

        Button reserveBtn = new Button("Reserve");
        reserveBtn.setStyle("-fx-background-color:#f39c12;-fx-text-fill:white;");
        reserveBtn.setOnAction(e -> {
            int selectedIdx = searchBookList.getSelectionModel().getSelectedIndex();
            if (selectedIdx < 0 || selectedIdx >= searchBookList.getItems().size()) {
                showAlert("Please select a book from the list!");
                return;
            }
            String line = searchBookList.getItems().get(selectedIdx);
            int dashIndex = line.indexOf("—");
            if (dashIndex == -1) {
                showAlert("Could not identify book title.");
                return;
            }
            String bookTitle = line.substring(line.indexOf("]") + 1, dashIndex).trim();
            List<BookItem> books = catalog.searchByTitle(bookTitle);
            if (!books.isEmpty()) {
                BookItem book = books.get(0);
                if (app.getCurrentMember() != null) {
                    if (book.getStatus() == BookStatus.AVAILABLE) {
                        showAlert("Book is available, you can check it out directly.");
                    } else {
                        app.getCurrentMember().reserveBookItem(book);
                        showAlert("Reserved: " + book.getTitle());
                        updateBookList(catalog.getAllBooks());
                    }
                } else {
                    showAlert("Please login first!");
                }
            } else {
                showAlert("Book not found!");
            }
        });

        Button back = new Button("Back to Menu");
        back.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");
        back.setOnAction(e -> app.showMainMenu(app.getCurrentMember()));

        HBox buttonBar = new HBox(10, reserveBtn, back);
        buttonBar.setAlignment(Pos.CENTER);

        layout.getChildren().addAll(titleLabel, sBox, searchBookList, buttonBar);
        return new Scene(layout, 650, 500);
    }

    private void updateBookList(List<BookItem> books) {
        searchBookList.getItems().clear();
        if (books.isEmpty()) {
            searchBookList.getItems().add("No books found");
            return;
        }
        for (BookItem b : books) {
            String icon = b.getStatus() == BookStatus.AVAILABLE ? "[✓]" :
                    b.getStatus() == BookStatus.LOANED ? "[↑]" : "[·]";
            String st = b.getStatus() == BookStatus.AVAILABLE ? "Available" :
                    b.getStatus() == BookStatus.LOANED ? "Checked out" : "Reserved";
            searchBookList.getItems().add(icon + " " + b.getTitle() +
                    " — " + b.getAuthor().getName() + " [" + st + "]");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
