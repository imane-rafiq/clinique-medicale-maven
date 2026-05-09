package controller;

import dao.RendezVousDAO;
import model.RendezVous;
import observer.MedecinObserver;
import observer.Observer;
import observer.Subject;

import java.util.ArrayList;
import java.util.List;

public class RendezVousController implements Subject {
    private final RendezVousDAO dao = new RendezVousDAO();
    private final List<Observer> observers = new ArrayList<>();

    public List<RendezVous> lister(Integer medecinId) {
        return dao.lister(medecinId);
    }

    public boolean ajouter(RendezVous rdv) {
        boolean ok = dao.ajouter(rdv);

        if (ok) {
            addObserver(new MedecinObserver(rdv.getMedecinId()));
            notifyObservers(messageNotification(rdv));
            observers.clear();
        }

        return ok;
    }

    public boolean modifier(RendezVous rdv) {
        return dao.modifier(rdv);
    }

    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    private String messageNotification(RendezVous rdv) {
        return "Nouveau rendez-vous ajouté\n" +
                "Patient ID : " + rdv.getPatientId() + "\n" +
                "Date : " + rdv.getDateRdv() + "\n" +
                "Heure : " + rdv.getHeureRdv() + "\n" +
                "Statut : " + rdv.getStatut();
    }
}
