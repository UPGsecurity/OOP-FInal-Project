package library.ui;

import library.LibraryFX;
import library.accounts.Member;
import library.enums.AccountStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ManageUsersView {
    private final LibraryFX app;

    public ManageUsersView(LibraryFX app) {
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:#ecf0f1;");

        Label title = new Label("MANAGE USERS");
        title.setStyle("-fx-font-size:20px;-fx-font-weight:bold;");

        ListView<String> userList = new ListView<>();
        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;");
        Button blockBtn = new Button("Block");
        blockBtn.setStyle("-fx-background-color:#e74c3c;-fx-text-fill:white;");
        Button unblockBtn = new Button("Activate");
        unblockBtn.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;");
        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");

        Runnable refreshUsers = () -> {
            userList.getItems().clear();
            for (LibraryFX.UserAccount ua : app.getRegisteredUsers()) {
                String status = ua.member != null ? ua.member.getStatus().toString() : "UNKNOWN";
                userList.getItems().add(ua.name + " | " + ua.email + " | " + status);
            }
        };
        refreshUsers.run();

        refreshBtn.setOnAction(e -> refreshUsers.run());

        blockBtn.setOnAction(e -> {
            int idx = userList.getSelectionModel().getSelectedIndex();
            if (idx < 0) {
                msg.setText("Select a user!");
                return;
            }
            LibraryFX.UserAccount ua = app.getRegisteredUsers().get(idx);
            if (ua.member != null) {
                app.getLibrarian().blockMember(ua.member);
                app.saveAllData();
                refreshUsers.run();
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Blocked: " + ua.name);
            }
        });

        unblockBtn.setOnAction(e -> {
            int idx = userList.getSelectionModel().getSelectedIndex();
            if (idx < 0) {
                msg.setText("Select a user!");
                return;
            }
            LibraryFX.UserAccount ua = app.getRegisteredUsers().get(idx);
            if (ua.member != null) {
                app.getLibrarian().unblockMember(ua.member);
                app.saveAllData();
                refreshUsers.run();
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Activated: " + ua.name);
            }
        });

        backBtn.setOnAction(e -> app.showAdminMenu(app.getLibrarian()));

        layout.getChildren().addAll(title, userList, refreshBtn, blockBtn, unblockBtn, backBtn, msg);
        return new Scene(layout, 550, 500);
    }
}
