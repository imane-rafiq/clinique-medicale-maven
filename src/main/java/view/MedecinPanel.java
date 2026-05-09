package view;

import controller.MedecinController;
import model.Medecin;
import util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MedecinPanel extends JPanel {
    private final MedecinController controller = new MedecinController();

    private final JTextField txtId = ViewStyle.field();
    private final JTextField txtNom = ViewStyle.field();
    private final JTextField txtSpecialite = ViewStyle.field();
    private final JTextField txtHoraires = ViewStyle.field();

    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnVider;

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{"ID", "Nom", "Spécialité", "Horaires"}, 0);

    private final JTable table = new JTable(tableModel);

    public MedecinPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ViewStyle.WHITE_BG);
        setBorder(new EmptyBorder(30, 38, 35, 38));

        add(ViewStyle.title("Gestion des Médecins"), BorderLayout.NORTH);

        JPanel form = ViewStyle.formCard();
        form.setLayout(new GridLayout(4, 2, 12, 12));

        txtId.setEditable(false);

        form.add(ViewStyle.label("ID"));
        form.add(txtId);
        form.add(ViewStyle.label("Nom"));
        form.add(txtNom);
        form.add(ViewStyle.label("Spécialité"));
        form.add(txtSpecialite);
        form.add(ViewStyle.label("Horaires"));
        form.add(txtHoraires);

        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(350, 0));
        left.add(form, BorderLayout.NORTH);
        left.add(createButtons(), BorderLayout.CENTER);

        add(left, BorderLayout.WEST);
        add(ViewStyle.scroll(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirDepuisTable();
                verifierSelection();
            }
        });

        charger(controller.lister());
        verifierSelection();
    }

    private JPanel createButtons() {
        JPanel panel = ViewStyle.buttonPanel(4);

        JButton btnAjouter = createButton("Ajouter");
        btnModifier = createButton("Modifier");
        btnSupprimer = createButton("Supprimer");
        btnVider = createButton("Vider");

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

    private void charger(List<Medecin> list) {
        tableModel.setRowCount(0);

        for (Medecin m : list) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getNom(),
                    m.getSpecialite(),
                    m.getHoraires()
            });
        }

        verifierSelection();
    }

    private void remplirDepuisTable() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtNom.setText(tableModel.getValueAt(row, 1).toString());
            txtSpecialite.setText(tableModel.getValueAt(row, 2).toString());
            txtHoraires.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    private void verifierSelection() {
        boolean selected = table.getSelectedRow() >= 0;
        btnModifier.setEnabled(selected);
        btnSupprimer.setEnabled(selected);
    }

    private void ajouter() {
        if (txtNom.getText().trim().isEmpty()) {
            MessageUtil.error("Nom obligatoire.");
            return;
        }

        boolean ok = controller.ajouter(
                new Medecin(
                        txtNom.getText().trim(),
                        txtSpecialite.getText().trim(),
                        txtHoraires.getText().trim()
                )
        );

        if (ok) {
            MessageUtil.info("Médecin ajouté.");
            charger(controller.lister());
            vider();
        } else {
            MessageUtil.error("Erreur ajout médecin.");
        }
    }

    private void modifier() {
        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un médecin.");
            return;
        }

        boolean ok = controller.modifier(
                new Medecin(
                        Integer.parseInt(txtId.getText()),
                        txtNom.getText().trim(),
                        txtSpecialite.getText().trim(),
                        txtHoraires.getText().trim()
                )
        );

        if (ok) {
            MessageUtil.info("Médecin modifié.");
            charger(controller.lister());
            vider();
        } else {
            MessageUtil.error("Erreur modification médecin.");
        }
    }

    private void supprimer() {
        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un médecin.");
            return;
        }

        if (!MessageUtil.confirm("Supprimer ce médecin ?")) return;

        boolean ok = controller.supprimer(Integer.parseInt(txtId.getText()));

        if (ok) {
            MessageUtil.info("Médecin supprimé.");
            charger(controller.lister());
            vider();
        } else {
            MessageUtil.error("Erreur suppression médecin.");
        }
    }

    private void vider() {
        txtId.setText("");
        txtNom.setText("");
        txtSpecialite.setText("");
        txtHoraires.setText("");
        table.clearSelection();
        verifierSelection();
    }
}
