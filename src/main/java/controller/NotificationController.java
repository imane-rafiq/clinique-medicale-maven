package controller;

import dao.NotificationDAO;
import model.Notification;

import java.util.List;

public class NotificationController {
    private final NotificationDAO dao = new NotificationDAO();

    public boolean ajouter(Notification notification) {
        return dao.ajouter(notification);
    }

    public List<Notification> listerParMedecin(int medecinId) {
        return dao.listerParMedecin(medecinId);
    }

    public boolean marquerCommeLue(int id) {
        return dao.marquerCommeLue(id);
    }
}