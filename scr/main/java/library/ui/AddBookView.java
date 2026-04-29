package library.ui;

import library.LibraryFX;
import library.enums.BookFormat;
import library.models.Author;
import library.models.BookItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AddBookView {
    private final LibraryFX app;

    public AddBookView(LibraryFX app) {
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:#ecf0f1;");

        Label title = new Label("ADD NEW BOOK");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

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

        Button addBtn = new Button("Add");
        addBtn.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;");
        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");

        addBtn.setOnAction(e -> {
            try {
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
                    return;
                }

                Author author = new Author(authorName, "");
                BookItem book = new BookItem(isbn, name, subject, publisher, lang, pages, author, barcode, format, price);
                app.getLibrarian().addBookItem(book, app.getCatalog());
                app.saveAllData();
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Book added: " + name);
                // clear fields
                isbnField.clear(); nameField.clear(); subjectField.clear(); publisherField.clear();
                langField.clear(); pagesField.clear(); authorField.clear(); barcodeField.clear();
                formatBox.setValue(null); priceField.clear();
            } catch (NumberFormatException ex) {
                msg.setText("Pages and price must be numbers!");
            }
        });

        backBtn.setOnAction(e -> app.showAdminMenu(app.getLibrarian()));

        layout.getChildren().addAll(title, isbnField, nameField, subjectField, publisherField,
                langField, pagesField, authorField, barcodeField, formatBox, priceField, addBtn, backBtn, msg);
        return new Scene(layout, 500, 650);
    }
}
