package library.ui;

import library.LibraryFX;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class RegisterView {
    private final LibraryFX app;

    public RegisterView(LibraryFX app) {
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:linear-gradient(to bottom,#2c3e50,#27ae60);");

        Label title = new Label("CREATE ACCOUNT");
        title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:white;");

        VBox form = new VBox(10);
        form.setStyle("-fx-background-color:white;-fx-background-radius:10;");
        form.setPadding(new Insets(20));

        TextField nameF = new TextField();
        nameF.setPromptText("Full name");
        TextField emailF = new TextField();
        emailF.setPromptText("Email");
        TextField phoneF = new TextField();
        phoneF.setPromptText("Phone");
        PasswordField passF = new PasswordField();
        passF.setPromptText("Password (min 4)");
        PasswordField confirmF = new PasswordField();
        confirmF.setPromptText("Confirm password");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");
        msg.setWrapText(true);

        Button regBtn = new Button("REGISTER");
        regBtn.setStyle("-fx-background-color:#27ae60;-fx-text-fill:white;-fx-font-size:14px;");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");
        backBtn.setMaxWidth(Double.MAX_VALUE);

        regBtn.setOnAction(e -> {
            String name = nameF.getText().trim();
            String email = emailF.getText().trim();
            String phone = phoneF.getText().trim();
            String pass = passF.getText();
            String conf = confirmF.getText();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                msg.setText("Please fill all fields!");
                return;
            }
            if (!email.contains("@")) {
                msg.setText("Invalid email!");
                return;
            }
            if (!pass.equals(conf)) {
                msg.setText("Passwords do not match!");
                return;
            }
            if (pass.length() < 4) {
                msg.setText("Password must be at least 4 characters!");
                return;
            }

            boolean success = app.registerUser(name, email, phone, pass);
            if (success) {
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Registration successful! Please login.");
                PauseTransition pt = new PauseTransition(Duration.seconds(2));
                pt.setOnFinished(ev -> app.showLoginScreen());
                pt.play();
            } else {
                msg.setStyle("-fx-text-fill:red;");
                msg.setText("This email is already registered!");
            }
        });

        backBtn.setOnAction(e -> app.showLoginScreen());
        form.getChildren().addAll(nameF, emailF, phoneF, passF, confirmF, msg, regBtn, backBtn);
        layout.getChildren().addAll(title, form);
        return new Scene(layout, 500, 550);
    }
}
