package dao;

import model.Medecin;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDAO {
    public boolean ajouter(Medecin m) {
        String sql = "INSERT INTO medecins(nom, specialite, horaires) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNom()); ps.setString(2, m.getSpecialite()); ps.setString(3, m.getHoraires());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean modifier(Medecin m) {
        String sql = "UPDATE medecins SET nom=?, specialite=?, horaires=? WHERE id=?";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNom()); ps.setString(2, m.getSpecialite()); ps.setString(3, m.getHoraires()); ps.setInt(4, m.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM medecins WHERE id=?";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Medecin> lister() {
        List<Medecin> list = new ArrayList<>();
        String sql = "SELECT * FROM medecins ORDER BY id DESC";
        try (Connection con = DBConnection.getInstance().getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new Medecin(rs.getInt("id"), rs.getString("nom"), rs.getString("specialite"), rs.getString("horaires")));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}
