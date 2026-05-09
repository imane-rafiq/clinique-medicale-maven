package controller;

import dao.UtilisateurDAO;
import model.Utilisateur;

import java.util.List;

public class UtilisateurController {
    private final UtilisateurDAO dao = new UtilisateurDAO();

    public List<Utilisateur> lister() {
        return dao.lister();
    }

    public boolean ajouter(String username, String password, String role, Integer medecinId) {
        return dao.ajouter(username, password, role, medecinId);
    }

    public boolean modifier(int id, String username, String password, String role, Integer medecinId) {
        return dao.modifier(id, username, password, role, medecinId);
    }

    public boolean supprimer(int id) {
        return dao.supprimer(id);
    }
}