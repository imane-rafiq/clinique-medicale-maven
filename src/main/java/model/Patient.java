package model;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String telephone;

    public Patient() {}

    public Patient(int id, String nom, String prenom, String dateNaissance, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
    }

    public Patient(String nom, String prenom, String dateNaissance, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}
