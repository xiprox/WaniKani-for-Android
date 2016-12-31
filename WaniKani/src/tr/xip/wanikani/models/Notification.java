package tr.xip.wanikani.models;

import java.io.Serializable;

public class Notification implements Serializable {
    public static final String DATA_NOTIFICATION_ID = "notification_id";
    public static final String DATA_NOTIFICATION_TITLE = "notification_title";
    public static final String DATA_NOTIFICATION_SHORT_TEXT = "notification_short_text";
    public static final String DATA_NOTIFICATION_TEXT = "notification_text";
    public static final String DATA_NOTIFICATION_IMAGE = "notification_image";
    public static final String DATA_NOTIFICATION_ACTION_URL = "notification_action_url";
    public static final String DATA_NOTIFICATION_ACTION_TEXT = "notification_action_text";

    private int id;
    private String title;
    private String shortText;
    private String text;
    private String image;
    private String actionUrl;
    private String actionText;
    private boolean read;

    public Notification(int id, String title, String shortText, String text, String image, String actionUrl, String actionText, boolean read) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.text = text;
        this.image = image;
        this.actionUrl = actionUrl;
        this.actionText = actionText;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
