package library.notifications;

import java.util.Date;

public abstract class Notification {

    protected int    notificationId;
    protected Date   createdOn;
    protected String content;

    public Notification(int notificationId, String content) {
        this.notificationId = notificationId;
        this.content        = content;
        this.createdOn      = new Date();
    }

    
    public abstract boolean sendNotification();

    public int    getNotificationId() { return notificationId; }
    public String getContent()        { return content; }
    public Date   getCreatedOn()      { return createdOn; }
}
