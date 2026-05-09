package view;

import controller.NotificationController;
import model.Notification;
import model.Utilisateur;
import util.MessageUtil;
import util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NotificationPanel extends JPanel {
    private final NotificationController controller = new NotificationController();
    private final Utilisateur user;

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{"ID", "Message", "Date", "Lu"}, 0);

    private final JTable table = new JTable(tableModel);

    public NotificationPanel(Utilisateur user) {
        this.user = user;

        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.BG);
        setBorder(new EmptyBorder(30, 35, 30, 35));

        JLabel title = new JLabel("Mes Notifications");
        title.setFont(UITheme.titleFont());
        title.setForeground(UITheme.BLUE_DARK);

        add(title, BorderLayout.NORTH);

        UITheme.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);

        JButton btnLu = UITheme.button("Marquer comme lue");
        btnLu.addActionListener(e -> marquerCommeLue());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(btnLu);

        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        charger();
    }

    private void charger() {
        tableModel.setRowCount(0);

        if (user.getMedecinId() == null) return;

        for (Notification n : controller.listerParMedecin(user.getMedecinId())) {
            tableModel.addRow(new Object[]{
                    n.getId(),
                    n.getMessage(),
                    n.getDateNotification(),
                    n.isLu() ? "Oui" : "Non"
            });
        }
    }

    private void marquerCommeLue() {
        int row = table.getSelectedRow();

        if (row < 0) {
            MessageUtil.error("Sélectionner une notification.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());

        if (controller.marquerCommeLue(id)) {
            MessageUtil.info("Notification marquée comme lue.");
            charger();
        } else {
            MessageUtil.error("Erreur.");
        }
    }
}