package library.ui;

import library.LibraryFX;
import library.accounts.Librarian;
import library.accounts.Member;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView {
    private final LibraryFX app;

    public LoginView(LibraryFX app) {
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom,#2c3e50,#3498db);");

        Label title = new Label("LIBRARY SYSTEM");
        title.setStyle("-fx-font-size:28px;-fx-font-weight:bold;-fx-text-fill:white;");

        VBox form = new VBox(10);
        form.setStyle("-fx-background-color:white;-fx-background-radius:10;");
        form.setPadding(new Insets(20));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");
        msg.setWrapText(true);

        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;-fx-font-size:14px;");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Button regBtn = new Button("Create new account");
        regBtn.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;-fx-font-size:14px;");
        regBtn.setMaxWidth(Double.MAX_VALUE);

        Hyperlink forgot = new Hyperlink("Forgot password?");

        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String pass = passField.getText();
            if (email.isEmpty() || pass.isEmpty()) {
                msg.setText("Please enter email and password!");
                return;
            }
            Object user = app.authenticate(email, pass);
            if (user instanceof Librarian) {
                app.showAdminMenu((Librarian) user);
            } else if (user instanceof Member) {
                app.showMainMenu((Member) user);
            } else {
                msg.setText("Invalid email or password!");
            }
        });

        passField.setOnAction(e -> loginBtn.fire());
        regBtn.setOnAction(e -> app.showRegisterScreen());
        forgot.setOnAction(e -> app.showForgotPasswordScreen());

        form.getChildren().addAll(
                new Label("Email:"), emailField,
                new Label("Password:"), passField,
                msg, loginBtn, regBtn, forgot
        );
        layout.getChildren().addAll(title, form);
        return new Scene(layout, 500, 550);
    }
}
