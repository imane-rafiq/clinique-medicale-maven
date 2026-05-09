package model;

public class RendezVous {
    private int id;
    private int patientId;
    private int medecinId;
    private String dateRdv;
    private String heureRdv;
    private String statut;

    public RendezVous() {}

    public RendezVous(int id, int patientId, int medecinId, String dateRdv, String heureRdv, String statut) {
        this.id = id;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.dateRdv = dateRdv;
        this.heureRdv = heureRdv;
        this.statut = statut;
    }

    public RendezVous(int patientId, int medecinId, String dateRdv, String heureRdv, String statut) {
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.dateRdv = dateRdv;
        this.heureRdv = heureRdv;
        this.statut = statut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getMedecinId() { return medecinId; }
    public void setMedecinId(int medecinId) { this.medecinId = medecinId; }
    public String getDateRdv() { return dateRdv; }
    public void setDateRdv(String dateRdv) { this.dateRdv = dateRdv; }
    public String getHeureRdv() { return heureRdv; }
    public void setHeureRdv(String heureRdv) { this.heureRdv = heureRdv; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
