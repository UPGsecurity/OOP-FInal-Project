package library.ui;

import library.LibraryFX;
import library.enums.BookFormat;
import library.models.BookItem;
import library.models.Author;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class EditBookView {
    private final LibraryFX app;

    public EditBookView(LibraryFX app) {
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:#ecf0f1;");

        Label title = new Label("EDIT BOOK DETAILS");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

        // Search section
        TextField searchField = new TextField();
        searchField.setPromptText("Book title or barcode");
        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;");

        ListView<String> resultsList = new ListView<>();
        resultsList.setPrefHeight(150);

        // Edit form
        Label editLabel = new Label("Edit selected book");
        editLabel.setStyle("-fx-font-weight:bold;");

        TextField isbnField = new TextField(); isbnField.setPromptText("ISBN");
        TextField nameField = new TextField(); nameField.setPromptText("Book title");
        TextField subjectField = new TextField(); subjectField.setPromptText("Subject");
        TextField publisherField = new TextField(); publisherField.setPromptText("Publisher");
        TextField langField = new TextField(); langField.setPromptText("Language");
        TextField pagesField = new TextField(); pagesField.setPromptText("Number of pages");
        TextField authorField = new TextField(); authorField.setPromptText("Author");
        TextField barcodeField = new TextField(); barcodeField.setPromptText("Barcode");
        ComboBox<BookFormat> formatBox = new ComboBox<>();
        formatBox.getItems().setAll(BookFormat.values());
        formatBox.setPromptText("Format");
        TextField priceField = new TextField(); priceField.setPromptText("Price");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");

        Button updateBtn = new Button("Update");
        updateBtn.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;");
        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");

        // Search button
        searchBtn.setOnAction(e -> {
            String q = searchField.getText().trim();
            if (q.isEmpty()) return;
            resultsList.getItems().clear();
            List<BookItem> books = app.getCatalog().searchByTitle(q);
            if (books.isEmpty()) books = app.getCatalog().searchByAuthor(q);
            for (BookItem b : books) {
                resultsList.getItems().add(b.getTitle() + " | " + b.getBarcode() + " | " + b.getAuthor().getName());
            }
            if (books.isEmpty()) msg.setText("Book not found!");
            else msg.setText("");
        });

        // Fill form when selected from list
        resultsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            String titlePart = newVal.split(" \\| ")[0];
            List<BookItem> books = app.getCatalog().searchByTitle(titlePart);
            if (!books.isEmpty()) {
                BookItem book = books.get(0);
                isbnField.setText(book.getISBN());
                nameField.setText(book.getTitle());
                subjectField.setText(book.getSubject());
                publisherField.setText(book.getPublisher());
                langField.setText(book.getLanguage());
                pagesField.setText(String.valueOf(book.getNumberOfPages()));
                authorField.setText(book.getAuthor().getName());
                barcodeField.setText(book.getBarcode());
                formatBox.setValue(book.getFormat());
                priceField.setText(String.valueOf(book.getPrice()));
            }
        });

        // Update button
        updateBtn.setOnAction(e -> {
            int idx = resultsList.getSelectionModel().getSelectedIndex();
            if (idx < 0) {
                msg.setText("Please select a book first!");
                return;
            }
            try {
                String oldTitle = resultsList.getItems().get(idx).split(" \\| ")[0];
                List<BookItem> books = app.getCatalog().searchByTitle(oldTitle);
                if (books.isEmpty()) {
                    msg.setText("Book not found!");
                    return;
                }
                BookItem oldBook = books.get(0);

                // Remove old book from catalog
                app.getLibrarian().removeBookItem(oldBook, app.getCatalog());

                // Create new book
                String isbn = isbnField.getText().trim();
                String name = nameField.getText().trim();
                String subject = subjectField.getText().trim();
                String publisher = publisherField.getText().trim();
                String lang = langField.getText().trim();
                int pages = Integer.parseInt(pagesField.getText().trim());
                String authorName = authorField.getText().trim();
                String barcode = barcodeField.getText().trim();
                BookFormat format = formatBox.getValue();
                double price = Double.parseDouble(priceField.getText().trim());

                if (isbn.isEmpty() || name.isEmpty() || authorName.isEmpty() || barcode.isEmpty() || format == null) {
                    msg.setText("Please fill all fields!");
                    // If error, add old book back
                    app.getLibrarian().addBookItem(oldBook, app.getCatalog());
                    return;
                }

                Author author = new Author(authorName, "");
                BookItem newBook = new BookItem(isbn, name, subject, publisher, lang, pages, author, barcode, format, price);
                newBook.setStatus(oldBook.getStatus()); // preserve status
                app.getLibrarian().addBookItem(newBook, app.getCatalog());
                app.saveAllData();

                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Book updated: " + name);

                // Update list
                resultsList.getItems().set(idx, newBook.getTitle() + " | " + newBook.getBarcode() + " | " + newBook.getAuthor().getName());
                // Clear form
                isbnField.clear(); nameField.clear(); subjectField.clear(); publisherField.clear();
                langField.clear(); pagesField.clear(); authorField.clear(); barcodeField.clear();
                formatBox.setValue(null); priceField.clear();
                searchField.clear();

            } catch (NumberFormatException ex) {
                msg.setText("Pages and price must be numbers!");
            }
        });

        backBtn.setOnAction(e -> app.showAdminMenu(app.getLibrarian()));

        layout.getChildren().addAll(
                title, searchField, searchBtn, resultsList,
                new Separator(), editLabel,
                isbnField, nameField, subjectField, publisherField,
                langField, pagesField, authorField, barcodeField, formatBox, priceField,
                updateBtn, backBtn, msg
        );
        return new Scene(layout, 600, 800);
    }
}
