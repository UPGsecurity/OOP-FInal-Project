package library.ui;

import library.LibraryFX;
import library.EmailSender;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ForgotPasswordView {
    private final LibraryFX app;

    public ForgotPasswordView(LibraryFX app) {
        this.app = app;
    }

    public Scene getScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(40));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color:linear-gradient(to bottom,#2c3e50,#e74c3c);");

        Label title = new Label("RESET PASSWORD");
        title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:white;");

        VBox form = new VBox(10);
        form.setStyle("-fx-background-color:white;-fx-background-radius:10;");
        form.setPadding(new Insets(20));

        TextField emailF = new TextField();
        emailF.setPromptText("Your email");
        Label msg = new Label();
        msg.setStyle("-fx-text-fill:red;");
        msg.setWrapText(true);
        Button sendBtn = new Button("Send code");
        sendBtn.setStyle("-fx-background-color:#3498db;-fx-text-fill:white;");

        VBox codeBox = new VBox(8);
        codeBox.setVisible(false);
        TextField codeF = new TextField();
        codeF.setPromptText("6-digit code");
        Button verifyBtn = new Button("Verify code");
        verifyBtn.setStyle("-fx-background-color:#f39c12;-fx-text-fill:white;");
        codeBox.getChildren().addAll(new Label("Step 2: Enter the code"), codeF, verifyBtn);

        VBox passBox = new VBox(8);
        passBox.setVisible(false);
        PasswordField newPassF = new PasswordField();
        newPassF.setPromptText("New password");
        PasswordField confPassF = new PasswordField();
        confPassF.setPromptText("Confirm password");
        Button resetBtn = new Button("Update password");
        resetBtn.setStyle("-fx-background-color:#2ecc71;-fx-text-fill:white;");
        passBox.getChildren().addAll(new Label("Step 3: New password"), newPassF, confPassF, resetBtn);

        sendBtn.setOnAction(e -> {
            String email = emailF.getText().trim();
            if (email.isEmpty()) {
                msg.setText("Enter your email!");
                return;
            }
            LibraryFX.UserAccount user = app.findUserByEmail(email);
            if (user == null) {
                msg.setText("Email not found!");
                return;
            }
            String code = EmailSender.generateCode();
            EmailSender.sendResetCode(email, code);
            app.setResetCode(code);
            app.setResetUser(user);
            msg.setStyle("-fx-text-fill:green;");
            msg.setText("Code sent to: " + email);
            codeBox.setVisible(true);
            sendBtn.setDisable(true);
            emailF.setEditable(false);
        });

        verifyBtn.setOnAction(e -> {
            if (codeF.getText().equals(app.getResetCode())) {
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Code verified!");
                passBox.setVisible(true);
                verifyBtn.setDisable(true);
            } else {
                msg.setStyle("-fx-text-fill:red;");
                msg.setText("Wrong code!");
            }
        });

        resetBtn.setOnAction(e -> {
            String np = newPassF.getText();
            String cp = confPassF.getText();
            if (np.isEmpty()) {
                msg.setText("Enter password!");
                return;
            }
            if (np.length() < 4) {
                msg.setText("At least 4 characters!");
                return;
            }
            if (!np.equals(cp)) {
                msg.setText("Passwords do not match!");
                return;
            }
            LibraryFX.UserAccount resetUser = app.getResetUser();
            if (resetUser != null) {
                resetUser.password = np;
                if (resetUser.member != null) resetUser.member.setPassword(np);
                app.saveAllData();
                msg.setStyle("-fx-text-fill:green;");
                msg.setText("Password updated! Please login.");
                PauseTransition pt = new PauseTransition(Duration.seconds(2));
                pt.setOnFinished(ev -> app.showLoginScreen());
                pt.play();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color:#7f8c8d;-fx-text-fill:white;");
        backBtn.setOnAction(e -> app.showLoginScreen());

        form.getChildren().addAll(
                new Label("Step 1: Enter your email"),
                emailF, sendBtn, codeBox, passBox, msg);
        layout.getChildren().addAll(title, form, backBtn);
        return new Scene(layout, 500, 620);
    }
}
