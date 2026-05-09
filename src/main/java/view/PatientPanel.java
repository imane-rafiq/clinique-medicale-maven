package view;

import controller.PatientController;
import model.Patient;
import util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {
    private final PatientController controller = new PatientController();

    private final JTextField txtId = ViewStyle.field();
    private final JTextField txtNom = ViewStyle.field();
    private final JTextField txtPrenom = ViewStyle.field();
    private final JTextField txtDate = ViewStyle.field();
    private final JTextField txtTel = ViewStyle.field();
    private final JTextField txtRecherche = ViewStyle.field();

    private JButton btnModifier;
    private JButton btnSupprimer;

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{"Numéro du Dossier", "Nom", "Prénom", "Date naissance", "Téléphone"}, 0);

    private final JTable table = new JTable(tableModel);

    public PatientPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(ViewStyle.WHITE_BG);
        setBorder(new EmptyBorder(30, 38, 35, 38));

        add(ViewStyle.title("Gestion des Patients"), BorderLayout.NORTH);

        JPanel form = ViewStyle.formCard();
        form.setLayout(new GridLayout(5, 2, 12, 12));

        txtId.setEditable(false);

        form.add(ViewStyle.label("Numéro du Dossier"));
        form.add(txtId);
        form.add(ViewStyle.label("Nom"));
        form.add(txtNom);
        form.add(ViewStyle.label("Prénom"));
        form.add(txtPrenom);
        form.add(ViewStyle.label("Date naissance"));
        form.add(txtDate);
        form.add(ViewStyle.label("Téléphone"));
        form.add(txtTel);

        JPanel left = new JPanel(new BorderLayout(0, 20));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(350, 0));
        left.add(form, BorderLayout.NORTH);
        left.add(createButtons(), BorderLayout.CENTER);

        JPanel search = new JPanel(new BorderLayout(10, 10));
        search.setOpaque(false);

        JButton btnSearch = createButton("Chercher");
        btnSearch.setPreferredSize(new Dimension(115, 38));

        search.add(txtRecherche, BorderLayout.CENTER);
        search.add(btnSearch, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        center.add(search, BorderLayout.NORTH);
        center.add(ViewStyle.scroll(table), BorderLayout.CENTER);

        add(left, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirDepuisTable();
                verifierSelection();
            }
        });

        btnSearch.addActionListener(e -> charger(controller.rechercher(txtRecherche.getText())));

        charger(controller.lister());
        verifierSelection();
    }

    private JPanel createButtons() {
        JPanel panel = ViewStyle.buttonPanel(4);

        JButton btnAjouter = createButton("Ajouter");
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

    private void charger(List<Patient> patients) {
        tableModel.setRowCount(0);

        for (Patient p : patients) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNom(),
                    p.getPrenom(),
                    p.getDateNaissance(),
                    p.getTelephone()
            });
        }

        verifierSelection();
    }

    private void remplirDepuisTable() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtNom.setText(tableModel.getValueAt(row, 1).toString());
            txtPrenom.setText(tableModel.getValueAt(row, 2).toString());
            txtDate.setText(tableModel.getValueAt(row, 3).toString());
            txtTel.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    private void verifierSelection() {
        boolean selected = table.getSelectedRow() >= 0;
        btnModifier.setEnabled(selected);
        btnSupprimer.setEnabled(selected);
    }

    private boolean champsOk() {
        return !txtNom.getText().trim().isEmpty()
                && !txtPrenom.getText().trim().isEmpty();
    }

    private void ajouter() {
        if (!champsOk()) {
            MessageUtil.error("Nom et prénom obligatoires.");
            return;
        }

        boolean ok = controller.ajouter(
                new Patient(
                        txtNom.getText().trim(),
                        txtPrenom.getText().trim(),
                        txtDate.getText().trim(),
                        txtTel.getText().trim()
                )
        );

        if (ok) {
            MessageUtil.info("Patient ajouté.");
            charger(controller.lister());
            vider();
        } else {
            MessageUtil.error("Erreur ajout patient.");
        }
    }

    private void modifier() {
        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un patient.");
            return;
        }

        boolean ok = controller.modifier(
                new Patient(
                        Integer.parseInt(txtId.getText()),
                        txtNom.getText().trim(),
                        txtPrenom.getText().trim(),
                        txtDate.getText().trim(),
                        txtTel.getText().trim()
                )
        );

        if (ok) {
            MessageUtil.info("Patient modifié.");
            charger(controller.lister());
            vider();
        } else {
            MessageUtil.error("Erreur modification patient.");
        }
    }

    private void supprimer() {
        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un patient.");
            return;
        }

        if (!MessageUtil.confirm("Supprimer ce patient ?")) return;

        boolean ok = controller.supprimer(Integer.parseInt(txtId.getText()));

        if (ok) {
            MessageUtil.info("Patient supprimé.");
            charger(controller.lister());
            vider();
        } else {
            MessageUtil.error("Erreur suppression patient.");
        }
    }

    private void vider() {
        txtId.setText("");
        txtNom.setText("");
        txtPrenom.setText("");
        txtDate.setText("");
        txtTel.setText("");
        table.clearSelection();
        verifierSelection();
    }
}
