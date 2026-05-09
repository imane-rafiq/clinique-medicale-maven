package view;

import controller.RendezVousController;
import model.RendezVous;
import model.Utilisateur;
import service.ExportService;
import util.MessageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RendezVousPanel extends JPanel {
    private final RendezVousController controller = new RendezVousController();
    private final Utilisateur user;

    private final boolean canAddDelete;
    private final boolean canModify;
    private final boolean canExport;

    private final JTextField txtId = ViewStyle.field();
    private final JTextField txtPatientId = ViewStyle.field();
    private final JTextField txtMedecinId = ViewStyle.field();
    private final JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
    private final JTextField txtHeure = ViewStyle.field();

    private final JComboBox<String> comboStatut =
            new JComboBox<>(new String[]{"PLANIFIE", "ANNULE", "TERMINE"});

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{"ID", "Patient ID", "Médecin ID", "Date", "Heure", "Statut"}, 0);

    private final JTable table = new JTable(tableModel);

    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnVider;
    private JButton btnExcel;
    private JButton btnPDF;

    public RendezVousPanel(Utilisateur user) {
        this.user = user;

        this.canAddDelete = "ADMIN".equals(user.getRole()) || "SECRETAIRE".equals(user.getRole());
        this.canModify = "ADMIN".equals(user.getRole()) || "SECRETAIRE".equals(user.getRole()) || "MEDECIN".equals(user.getRole());
        this.canExport = true;

        setLayout(new BorderLayout(20, 20));
        setBackground(ViewStyle.WHITE_BG);
        setBorder(new EmptyBorder(28, 35, 25, 35));

        JLabel title = ViewStyle.title(
                "MEDECIN".equals(user.getRole()) ? "Mes Rendez-vous" : "Gestion des Rendez-vous"
        );
        add(title, BorderLayout.NORTH);

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        ViewStyle.styleSpinner(dateSpinner);
        ViewStyle.styleCombo(comboStatut);

        txtId.setEditable(false);
        setFormEditable(canModify);

        JPanel form = ViewStyle.formCard();
        form.setLayout(new GridLayout(6, 2, 10, 9));
        form.setPreferredSize(new Dimension(390, 355));
        form.setMaximumSize(new Dimension(390, 355));

        form.add(ViewStyle.label("ID"));
        form.add(txtId);

        form.add(ViewStyle.label("Patient ID"));
        form.add(txtPatientId);

        form.add(ViewStyle.label("Médecin ID"));
        form.add(txtMedecinId);

        form.add(ViewStyle.label("Date"));
        form.add(dateSpinner);

        form.add(ViewStyle.label("Heure hh:mm:ss"));
        form.add(txtHeure);

        form.add(ViewStyle.label("Statut"));
        form.add(comboStatut);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(390, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        left.add(form);
        left.add(Box.createVerticalStrut(10));
        left.add(createButtons());

        add(left, BorderLayout.WEST);
        add(ViewStyle.scroll(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                remplirDepuisTable();
                verifierSelection();
            }
        });

        charger();
        verifierSelection();
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 9, 8));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(390, 82));
        panel.setMaximumSize(new Dimension(390, 82));

        btnAjouter = ViewStyle.button("Ajouter");
        btnModifier = ViewStyle.button("Modifier");
        btnSupprimer = ViewStyle.button("Supprimer");
        btnVider = ViewStyle.button("Vider");
        btnExcel = ViewStyle.button("Excel");
        btnPDF = ViewStyle.button("PDF");

        btnAjouter.setVisible(canAddDelete);
        btnSupprimer.setVisible(canAddDelete);
        btnVider.setVisible(canModify);
        btnModifier.setVisible(canModify);
        btnExcel.setVisible(canExport);
        btnPDF.setVisible(canExport);

        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);

        panel.add(btnAjouter);
        panel.add(btnModifier);
        panel.add(btnSupprimer);
        panel.add(btnVider);
        panel.add(btnExcel);
        panel.add(btnPDF);

        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnVider.addActionListener(e -> vider());
        btnExcel.addActionListener(e -> ExportService.exportExcel(table));
        btnPDF.addActionListener(e -> ExportService.exportPDF(table));

        return panel;
    }

    private void setFormEditable(boolean editable) {
        txtPatientId.setEditable(editable);
        txtMedecinId.setEditable(editable);
        txtHeure.setEditable(editable);
        dateSpinner.setEnabled(editable);
        comboStatut.setEnabled(editable);
    }

    private String getDateChoisie() {
        return new SimpleDateFormat("yyyy-MM-dd").format((Date) dateSpinner.getValue());
    }

    private Integer filtreMedecin() {
        return "MEDECIN".equals(user.getRole()) ? user.getMedecinId() : null;
    }

    private void charger() {
        tableModel.setRowCount(0);

        for (RendezVous r : controller.lister(filtreMedecin())) {
            tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getPatientId(),
                    r.getMedecinId(),
                    r.getDateRdv(),
                    r.getHeureRdv(),
                    r.getStatut()
            });
        }

        verifierSelection();
    }

    private void remplirDepuisTable() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtPatientId.setText(tableModel.getValueAt(row, 1).toString());
            txtMedecinId.setText(tableModel.getValueAt(row, 2).toString());

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd")
                        .parse(tableModel.getValueAt(row, 3).toString());
                dateSpinner.setValue(date);
            } catch (Exception ex) {
                dateSpinner.setValue(new Date());
            }

            txtHeure.setText(tableModel.getValueAt(row, 4).toString());
            comboStatut.setSelectedItem(tableModel.getValueAt(row, 5).toString());
        }
    }

    private void verifierSelection() {
        boolean selected = table.getSelectedRow() >= 0;

        if (btnModifier != null) {
            btnModifier.setEnabled(selected && canModify);
        }

        if (btnSupprimer != null) {
            btnSupprimer.setEnabled(selected && canAddDelete);
        }
    }

    private boolean champsOk() {
        return !txtPatientId.getText().trim().isEmpty()
                && !txtMedecinId.getText().trim().isEmpty()
                && !txtHeure.getText().trim().isEmpty();
    }

    private RendezVous getFormRdv(boolean avecId) {
        if (avecId) {
            return new RendezVous(
                    Integer.parseInt(txtId.getText()),
                    Integer.parseInt(txtPatientId.getText()),
                    Integer.parseInt(txtMedecinId.getText()),
                    getDateChoisie(),
                    txtHeure.getText().trim(),
                    comboStatut.getSelectedItem().toString()
            );
        }

        return new RendezVous(
                Integer.parseInt(txtPatientId.getText()),
                Integer.parseInt(txtMedecinId.getText()),
                getDateChoisie(),
                txtHeure.getText().trim(),
                comboStatut.getSelectedItem().toString()
        );
    }

    private void ajouter() {
        if (!canAddDelete) {
            MessageUtil.error("Vous n'avez pas le droit d'ajouter un rendez-vous.");
            return;
        }

        if (!champsOk()) {
            MessageUtil.error("Tous les champs principaux sont obligatoires.");
            return;
        }

        try {
            boolean ok = controller.ajouter(getFormRdv(false));

            if (ok) {
                MessageUtil.info("Rendez-vous ajouté.");
                charger();
                vider();
            } else {
                MessageUtil.error("Erreur : horaire invalide, conflit horaire ou données incorrectes.");
            }
        } catch (Exception e) {
            MessageUtil.error("Patient ID, Médecin ID et Heure doivent être valides.");
        }
    }

    private void modifier() {
        if (!canModify) {
            MessageUtil.error("Vous n'avez pas le droit de modifier un rendez-vous.");
            return;
        }

        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un rendez-vous.");
            return;
        }

        if (!champsOk()) {
            MessageUtil.error("Tous les champs principaux sont obligatoires.");
            return;
        }

        try {
            boolean ok = controller.modifier(getFormRdv(true));

            if (ok) {
                MessageUtil.info("Rendez-vous modifié.");
                charger();
                vider();
            } else {
                MessageUtil.error("Erreur : horaire invalide, conflit horaire ou données incorrectes.");
            }
        } catch (Exception e) {
            MessageUtil.error("Patient ID, Médecin ID et Heure doivent être valides.");
        }
    }

    private void supprimer() {
        if (!canAddDelete) {
            MessageUtil.error("Vous n'avez pas le droit de supprimer un rendez-vous.");
            return;
        }

        if (txtId.getText().isEmpty()) {
            MessageUtil.error("Sélectionner un rendez-vous.");
            return;
        }

        if (!MessageUtil.confirm("Supprimer ce rendez-vous ?")) return;

        boolean ok = controller.supprimer(Integer.parseInt(txtId.getText()));

        if (ok) {
            MessageUtil.info("Rendez-vous supprimé.");
            charger();
            vider();
        } else {
            MessageUtil.error("Erreur suppression rendez-vous.");
        }
    }

    private void vider() {
        txtId.setText("");
        txtPatientId.setText("");
        txtMedecinId.setText("");
        txtHeure.setText("");
        comboStatut.setSelectedIndex(0);
        dateSpinner.setValue(new Date());

        table.clearSelection();
        verifierSelection();
    }
}