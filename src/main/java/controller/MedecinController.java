package controller;

import dao.MedecinDAO;
import model.Medecin;

import java.util.List;

public class MedecinController {
    private final MedecinDAO dao = new MedecinDAO();

    public boolean ajouter(Medecin m) { return dao.ajouter(m); }
    public boolean modifier(Medecin m) { return dao.modifier(m); }
    public boolean supprimer(int id) { return dao.supprimer(id); }
    public List<Medecin> lister() { return dao.lister(); }
}
