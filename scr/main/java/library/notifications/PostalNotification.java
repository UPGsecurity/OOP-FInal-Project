package library.notifications;

import library.models.Address;

public class PostalNotification extends Notification {

    private Address address;

    public PostalNotification(int notificationId, String content, Address address) {
        super(notificationId, content);
        this.address = address;
    }

    @Override
    public boolean sendNotification() {
        if (address == null) {
            System.out.println("[ERROR] No address provided.");
            return false;
        }
        System.out.println("[MAIL] Sent -> " + address.toString());
        System.out.println("         Content: " + content);
        return true;
    }

    public Address getAddress() { return address; }
}
