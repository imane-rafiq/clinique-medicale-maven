package controller;

import dao.PatientDAO;
import model.Patient;

import java.util.List;

public class PatientController {
    private final PatientDAO dao = new PatientDAO();

    public boolean ajouter(Patient p) { return dao.ajouter(p); }
    public boolean modifier(Patient p) { return dao.modifier(p); }
    public boolean supprimer(int id) { return dao.supprimer(id); }
    public List<Patient> lister() { return dao.lister(); }
    public List<Patient> rechercher(String motCle) { return dao.rechercher(motCle); }
}
