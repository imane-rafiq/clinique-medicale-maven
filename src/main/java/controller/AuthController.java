package controller;

import dao.UtilisateurDAO;
import model.Utilisateur;

public class AuthController {
    //utilise DAO opour acceder au BDD
    private final UtilisateurDAO dao = new UtilisateurDAO();

    public Utilisateur seConnecter(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) return null;
        return dao.login(username.trim(), password.trim());
    }
}
