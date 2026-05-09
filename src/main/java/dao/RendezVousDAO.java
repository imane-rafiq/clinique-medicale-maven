package dao;

import model.RendezVous;
import util.DBConnection;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {

    public List<RendezVous> lister(Integer medecinId) {
        List<RendezVous> list = new ArrayList<>();

        String sql = medecinId == null
                ? "SELECT * FROM rendezvous ORDER BY date_rdv DESC, heure_rdv DESC"
                : "SELECT * FROM rendezvous WHERE medecin_id=? ORDER BY date_rdv DESC, heure_rdv DESC";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (medecinId != null) {
                ps.setInt(1, medecinId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new RendezVous(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("medecin_id"),
                        rs.getString("date_rdv"),
                        rs.getString("heure_rdv"),
                        rs.getString("statut")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean ajouter(RendezVous rdv) {
        if (!heureDansHorairesMedecin(rdv.getMedecinId(), rdv.getHeureRdv())) {
            return false;
        }

        if (existeDeja(rdv.getMedecinId(), rdv.getDateRdv(), rdv.getHeureRdv(), 0)) {
            return false;
        }

        String sql = "INSERT INTO rendezvous(patient_id, medecin_id, date_rdv, heure_rdv, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rdv.getPatientId());
            ps.setInt(2, rdv.getMedecinId());
            ps.setString(3, rdv.getDateRdv());
            ps.setString(4, rdv.getHeureRdv());
            ps.setString(5, rdv.getStatut());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(RendezVous rdv) {
        if (!heureDansHorairesMedecin(rdv.getMedecinId(), rdv.getHeureRdv())) {
            return false;
        }

        if (existeDeja(rdv.getMedecinId(), rdv.getDateRdv(), rdv.getHeureRdv(), rdv.getId())) {
            return false;
        }

        String sql = "UPDATE rendezvous SET patient_id=?, medecin_id=?, date_rdv=?, heure_rdv=?, statut=? WHERE id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rdv.getPatientId());
            ps.setInt(2, rdv.getMedecinId());
            ps.setString(3, rdv.getDateRdv());
            ps.setString(4, rdv.getHeureRdv());
            ps.setString(5, rdv.getStatut());
            ps.setInt(6, rdv.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM rendezvous WHERE id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean heureDansHorairesMedecin(int medecinId, String heureRdv) {
        String sql = "SELECT horaires FROM medecins WHERE id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, medecinId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String horaires = rs.getString("horaires");

                if (horaires == null || !horaires.contains("-")) {
                    return false;
                }

                String[] parts = horaires.split("-");

                LocalTime debut = parseHeure(parts[0].trim());
                LocalTime fin = parseHeure(parts[1].trim());
                LocalTime rdv = parseHeure(heureRdv.trim());

                return !rdv.isBefore(debut) && rdv.isBefore(fin);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean existeDeja(int medecinId, String dateRdv, String heureRdv, int idActuel) {
        String sql = "SELECT id FROM rendezvous WHERE medecin_id=? AND date_rdv=? AND heure_rdv=? AND id<>?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, medecinId);
            ps.setString(2, dateRdv);
            ps.setString(3, heureRdv);
            ps.setInt(4, idActuel);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private LocalTime parseHeure(String heure) {
        if (heure.length() == 5) {
            return LocalTime.parse(heure, DateTimeFormatter.ofPattern("HH:mm"));
        }

        return LocalTime.parse(heure, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}