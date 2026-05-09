package observer;

import controller.NotificationController;
import model.Notification;

public class MedecinObserver implements Observer {
    private final int medecinId;
    private final NotificationController controller = new NotificationController();

    public MedecinObserver(int medecinId) {
        this.medecinId = medecinId;
    }

    @Override
    public void update(String message) {
        controller.ajouter(new Notification(medecinId, message));
    }
}