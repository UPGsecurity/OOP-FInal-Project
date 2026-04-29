package library.ui;

import library.LibraryFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class AllUsersView {
    private final LibraryFX app;

    public AllUsersView(LibraryFX app) { this.app = app; }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:#ecf0f1;");

        Label title = new Label("ALL USERS");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

        ListView<String> list = new ListView<>();
        for (LibraryFX.UserAccount ua : app.getRegisteredUsers()) {
            list.getItems().add(ua.name + " | " + ua.email + " | " + ua.member.getStatus());
        }

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showAdminMenu(app.getLibrarian()));

        layout.getChildren().addAll(title, list, backBtn);
        return new Scene(layout, 600, 500);
    }
}
