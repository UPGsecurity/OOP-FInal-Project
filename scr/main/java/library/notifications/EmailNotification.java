package library.notifications;

public class EmailNotification extends Notification {

    private String email;

    public EmailNotification(int notificationId, String content, String email) {
        super(notificationId, content);
        this.email = email;
    }

    @Override
    public boolean sendNotification() {
        if (email == null || !email.contains("@")) {
            System.out.println("[ERROR] Invalid email address: " + email);
            return false;
        }
        System.out.println("[EMAIL] Sent -> " + email);
        System.out.println("        Content: " + content);
        return true;
    }

    public String getEmail() { return email; }
}
