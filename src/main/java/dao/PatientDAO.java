package dao;

import model.Patient;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    public boolean ajouter(Patient p) {
        String sql = "INSERT INTO patients(nom, prenom, date_naissance, telephone) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getDateNaissance());
            ps.setString(4, p.getTelephone());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean modifier(Patient p) {
        String sql = "UPDATE patients SET nom=?, prenom=?, date_naissance=?, telephone=? WHERE id=?";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getPrenom());
            ps.setString(3, p.getDateNaissance());
            ps.setString(4, p.getTelephone());
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM patients WHERE id=?";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Patient> lister() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id DESC";
        try (Connection con = DBConnection.getInstance().getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Patient(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("date_naissance"), rs.getString("telephone")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Patient> rechercher(String motCle) {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? OR id LIKE ? ORDER BY id DESC";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String q = "%" + motCle + "%";
            ps.setString(1, q); ps.setString(2, q); ps.setString(3, q); ps.setString(4, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Patient(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("date_naissance"), rs.getString("telephone")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
