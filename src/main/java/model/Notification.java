package model;

public class Notification {
    private int id;
    private int medecinId;
    private String message;
    private String dateNotification;
    private boolean lu;

    public Notification(int id, int medecinId, String message, String dateNotification, boolean lu) {
        this.id = id;
        this.medecinId = medecinId;
        this.message = message;
        this.dateNotification = dateNotification;
        this.lu = lu;
    }

    public Notification(int medecinId, String message) {
        this.medecinId = medecinId;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public int getMedecinId() {
        return medecinId;
    }

    public String getMessage() {
        return message;
    }

    public String getDateNotification() {
        return dateNotification;
    }

    public boolean isLu() {
        return lu;
    }
}