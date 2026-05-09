package view;

import controller.UtilisateurController;
import model.Utilisateur;
import util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UtilisateurPanel extends JPanel {
    private final UtilisateurController controller = new UtilisateurController();

    private final JTextField txtId = ViewStyle.field();
    private final JTextField txtUsername = ViewStyle.field();
    private final JPasswordField txtPassword = new JPasswordField();

    private final JComboBox<String> comboRole =
            new JComboBox<>(new String[]{"MEDECIN", "SECRETAIRE"});

    private final JTextField txtMedecinId = ViewStyle.field();

    private JButton btnModifier;
    private JButton btnSupprimer;

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{"ID", "Username", "Rôle", "Médecin ID"}, 0);

    private final JTable table = new JTable(tableModel);

    public UtilisateurPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ViewStyle.WHITE_BG);
        setBorder(new EmptyBorder(30, 38, 35, 38));

        add(ViewStyle.title("Gestion des Utilisateurs"), BorderLayout.NORTH);

        JPanel form = ViewStyle.formCard();
        form.setLayout(new GridLayout(5, 2, 12, 12));

        txtId.setEditable(false);
        ViewStyle.styleField(txtPassword);
        ViewStyle.styleCombo(comboRole);

        form.add(ViewStyle.label("ID"));
        form.add(txtId);

        form.add(ViewStyle.label("Username"));
        form.add(txtUsername);

        form.add(ViewStyle.label("Mot de passe"));
        form.add(txtPassword);

        form.add(ViewStyle.label("Rôle"));
        form.add(comboRole);

        form.add(ViewStyle.label("Médecin ID"));
        form.add(txtMedecinId);

        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(370, 0));
        left.add(form, BorderLayout.NORTH);
        left.add(createButtons(), BorderLayout.CENTER);

        add(left, BorderLayout.WEST);
        add(ViewStyle.scroll(table), BorderLayout.CENTER);

        comboRole.addActionListener(e -> updateMedecinField());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirDepuisTable();
                verifierSelection();
            }
        });

        updateMedecinField();
        charger();
        verifierSelection();
    }

    private JPanel createButtons() {
        JPanel panel = ViewStyle.buttonPanel(4);

        JButton btnAjouter = createButton("Créer compte");
        btnModifier = createButton("Modifier");
        btnSupprimer = createButton("Supprimer");
        JButton btnVider = createButton("Vider");

        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);

        panel.add(btnAjouter);
        panel.add(btnModifier);
        panel.add(btnSupprimer);
        panel.add(btnVider);

        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnVider.addActionListener(e -> vider());

        return panel;
    }

    private JButton createButton(String text) {
        return ViewStyle.button(text);
    }

    private void updateMedecinField() {
        String role = comboRole.getSelectedItem().toString();

        if ("MEDECIN".equals(role)) {
            txtMedecinId.setEditable(true);
            txtMedecinId.setBackground(Color.WHITE);
        } else {
            txtMedecinId.setText("");
            txtMedecinId.setEditable(false);
            txtMedecinId.setBackground(new Color(230, 236, 242));
        }
    }

    private Integer getMedecinId() {
        if (!"MEDECIN".equals(comboRole.getSelectedItem().toString())) {
            return null;
        }

        if (txtMedecinId.getText().trim().isEmpty()) {
            return null;
        }

        return Integer.parseInt(txtMedecinId.getText().trim());
    }

    private String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }

    private void charger() {
        tableModel.setRowCount(0);

        List<Utilisateur> users = controller.lister();

        for (Utilisateur u : users) {
            tableModel.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    u.getRole(),
                    u.getMedecinId() == null ? "" : u.getMedecinId()
            });
        }

        verifierSelection();
    }

    private void remplirDepuisTable() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtUsername.setText(tableModel.getValueAt(row, 1).toString());
            comboRole.setSelectedItem(tableModel.getValueAt(row, 2).toString());

            Object medecinId = tableModel.getValueAt(row, 3);
            txtMedecinId.setText(medecinId == null ? "" : medecinId.toString());

            txtPassword.setText("");
            updateMedecinField();
        }
    }

    private void verifierSelection() {
        boolean selected = table.getSelectedRow() >= 0;
        btnModifier.setEnabled(selected);
        btnSupprimer.setEnabled(selected);
    }

    private void ajouter() {
        if (txtUsername.getText().trim().isEmpty() || getPassword().isEmpty()) {
            MessageUtil.error("Username et mot de passe obligatoires.");
            return;
        }

        try {
            boolean ok = controller.ajouter(
                    txtUsername.getText().trim(),
                    getPassword(),
                    comboRole.getSelectedItem().toString(),
                    getMedecinId()
            );

            if (ok) {
                MessageUtil.info("Compte utilisateur créé.");
                charger();
                vider();
            } else {
                MessageUtil.error("Erreur : création impossible.");
            }

        } catch (Exception e) {
            MessageUtil.error("Médecin ID invalide.");
        }
    }

    private void modifier() {
        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un utilisateur.");
            return;
        }

        if (txtUsername.getText().trim().isEmpty()) {
            MessageUtil.error("Username obligatoire.");
            return;
        }

        try {
            boolean ok = controller.modifier(
                    Integer.parseInt(txtId.getText()),
                    txtUsername.getText().trim(),
                    getPassword(),
                    comboRole.getSelectedItem().toString(),
                    getMedecinId()
            );

            if (ok) {
                MessageUtil.info("Utilisateur modifié.");
                charger();
                vider();
            } else {
                MessageUtil.error("Erreur : modification impossible.");
            }

        } catch (Exception e) {
            MessageUtil.error("Médecin ID invalide.");
        }
    }

    private void supprimer() {
        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un utilisateur.");
            return;
        }

        if (!MessageUtil.confirm("Supprimer cet utilisateur ?")) return;

        boolean ok = controller.supprimer(Integer.parseInt(txtId.getText()));

        if (ok) {
            MessageUtil.info("Utilisateur supprimé.");
            charger();
            vider();
        } else {
            MessageUtil.error("Erreur : suppression impossible.");
        }
    }

    private void vider() {
        txtId.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        comboRole.setSelectedIndex(0);
        txtMedecinId.setText("");
        table.clearSelection();
        updateMedecinField();
        verifierSelection();
    }
}
