package model;

public class Medecin {
    private int id;
    private String nom;
    private String specialite;
    private String horaires;

    public Medecin() {}

    public Medecin(int id, String nom, String specialite, String horaires) {
        this.id = id;
        this.nom = nom;
        this.specialite = specialite;
        this.horaires = horaires;
    }

    public Medecin(String nom, String specialite, String horaires) {
        this.nom = nom;
        this.specialite = specialite;
        this.horaires = horaires;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getHoraires() { return horaires; }
    public void setHoraires(String horaires) { this.horaires = horaires; }
}
