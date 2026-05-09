package dao;

import model.Utilisateur;
import util.DBConnection;
import util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
   // utiliser par AuthController
    public Utilisateur login(String username, String password) {
        String sql = "SELECT * FROM utilisateurs WHERE username=? AND password=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hashPassword(password.trim()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int medecinValue = rs.getInt("medecin_id");
                Integer medecinId = rs.wasNull() ? null : medecinValue;

                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        medecinId
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Utilisateur> lister() {
        List<Utilisateur> list = new ArrayList<>();

        String sql = "SELECT * FROM utilisateurs ORDER BY id DESC";

        try (Connection con = DBConnection.getInstance().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int medecinValue = rs.getInt("medecin_id");
                Integer medecinId = rs.wasNull() ? null : medecinValue;

                list.add(new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        medecinId
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean ajouter(String username, String password, String role, Integer medecinId) {
        if ("ADMIN".equals(role)) {
            return false;
        }

        String sql = "INSERT INTO utilisateurs(username, password, role, medecin_id) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hashPassword(password.trim()));
            ps.setString(3, role);

            if (medecinId == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, medecinId);
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(int id, String username, String password, String role, Integer medecinId) {
        if ("ADMIN".equals(role)) {
            return false;
        }

        String sql;

        if (password == null || password.trim().isEmpty()) {
            sql = "UPDATE utilisateurs SET username=?, role=?, medecin_id=? WHERE id=? AND role <> 'ADMIN'";
        } else {
            sql = "UPDATE utilisateurs SET username=?, password=?, role=?, medecin_id=? WHERE id=? AND role <> 'ADMIN'";
        }

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (password == null || password.trim().isEmpty()) {
                ps.setString(1, username.trim());
                ps.setString(2, role);

                if (medecinId == null) ps.setNull(3, Types.INTEGER);
                else ps.setInt(3, medecinId);

                ps.setInt(4, id);
            } else {
                ps.setString(1, username.trim());
                ps.setString(2, PasswordUtil.hashPassword(password.trim()));
                ps.setString(3, role);

                if (medecinId == null) ps.setNull(4, Types.INTEGER);
                else ps.setInt(4, medecinId);

                ps.setInt(5, id);
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id=? AND role <> 'ADMIN'";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}