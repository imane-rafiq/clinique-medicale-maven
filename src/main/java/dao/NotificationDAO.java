package dao;

import model.Notification;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public boolean ajouter(Notification notification) {
        String sql = "INSERT INTO notifications(medecin_id, message) VALUES (?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notification.getMedecinId());
            ps.setString(2, notification.getMessage());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Notification> listerParMedecin(int medecinId) {
        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM notifications WHERE medecin_id=? ORDER BY date_notification DESC";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, medecinId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("id"),
                        rs.getInt("medecin_id"),
                        rs.getString("message"),
                        rs.getString("date_notification"),
                        rs.getBoolean("lu")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean marquerCommeLue(int id) {
        String sql = "UPDATE notifications SET lu=true WHERE id=?";

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