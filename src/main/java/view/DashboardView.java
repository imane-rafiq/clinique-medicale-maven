package view;

import controller.MedecinController;
import controller.PatientController;
import controller.RendezVousController;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JFrame {
    private final Utilisateur user;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private TabButton activeTab;

    private final PatientController patientController = new PatientController();
    private final MedecinController medecinController = new MedecinController();
    private final RendezVousController rendezVousController = new RendezVousController();

    private static final Color WHITE_BG = new Color(250, 252, 255);

    private static final Color HEADER_DARK = new Color(5, 22, 43);
    private static final Color NAV_DARK = new Color(8, 35, 65);
    private static final Color NAV_DARK_2 = new Color(10, 44, 82);

    private static final Color CARD_DARK = new Color(7, 42, 78);
    private static final Color CARD_DARK_2 = new Color(10, 58, 105);
    private static final Color CARD_BORDER = new Color(18, 76, 130);

    private static final Color BLUE_ACTIVE = new Color(32, 112, 185);

    private static final Color TEXT_DARK = new Color(8, 35, 65);
    private static final Color TEXT_MUTED = new Color(90, 105, 122);
    private static final Color TEXT_LIGHT = Color.WHITE;

    private static final Color SHADOW = new Color(0, 0, 0, 30);

    public DashboardView(Utilisateur user) {
        this.user = user;

        setTitle("Clinique Médicale");
        setSize(1200, 700);
        setMinimumSize(new Dimension(1000, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(WHITE_BG);

        main.add(createTopArea(), BorderLayout.NORTH);

        contentPanel.setBackground(WHITE_BG);
        contentPanel.add(createDashboardPanel(), "dashboard");

        if ("ADMIN".equals(user.getRole()) || "SECRETAIRE".equals(user.getRole())) {
            contentPanel.add(new PatientPanel(), "patients");
        }

        if ("ADMIN".equals(user.getRole())) {
            contentPanel.add(new MedecinPanel(), "medecins");
            contentPanel.add(new UtilisateurPanel(), "utilisateurs");
        }

        contentPanel.add(new RendezVousPanel(user), "rdv");

        if ("MEDECIN".equals(user.getRole())) {
            contentPanel.add(new NotificationPanel(user), "notifications");
        }

        main.add(contentPanel, BorderLayout.CENTER);

        add(main);
        cardLayout.show(contentPanel, "dashboard");
    }

    private JPanel createTopArea() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(HEADER_DARK);

        GradientPanel header = new GradientPanel(HEADER_DARK, NAV_DARK);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 86));
        header.setBorder(new EmptyBorder(0, 38, 0, 38));

        JLabel title = new JLabel("Système de Gestion Clinique");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_LIGHT);

        JLabel userInfo = new JLabel(user.getUsername() + "  |  " + user.getRole());
        userInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userInfo.setForeground(new Color(230, 238, 248));

        header.add(title, BorderLayout.WEST);
        header.add(userInfo, BorderLayout.EAST);

        GradientPanel nav = new GradientPanel(NAV_DARK, NAV_DARK_2);
        nav.setLayout(new BorderLayout());
        nav.setPreferredSize(new Dimension(0, 62));
        nav.setBorder(new EmptyBorder(8, 38, 8, 38));

        JPanel tabsBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        tabsBar.setOpaque(false);

        TabButton dashboardTab = createTab("Tableau de bord", "dashboard");
        tabsBar.add(dashboardTab);

        if ("ADMIN".equals(user.getRole()) || "SECRETAIRE".equals(user.getRole())) {
            tabsBar.add(createTab("Patients", "patients"));
        }

        if ("ADMIN".equals(user.getRole())) {
            tabsBar.add(createTab("Médecins", "medecins"));
            tabsBar.add(createTab("Utilisateurs", "utilisateurs"));
        }

        if ("MEDECIN".equals(user.getRole())) {
            tabsBar.add(createTab("Mes rendez-vous", "rdv"));
            tabsBar.add(createTab("Notifications", "notifications"));
        } else {
            tabsBar.add(createTab("Rendez-vous", "rdv"));
        }

        RoundedButton logout = new RoundedButton("Déconnexion");
        logout.setPreferredSize(new Dimension(142, 38));
        logout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logout.setForeground(Color.WHITE);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logout.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        nav.add(tabsBar, BorderLayout.WEST);
        nav.add(logout, BorderLayout.EAST);

        top.add(header, BorderLayout.NORTH);
        top.add(nav, BorderLayout.SOUTH);

        setActiveTab(dashboardTab);

        return top;
    }

    private TabButton createTab(String text, String cardName) {
        TabButton tab = new TabButton(text);
        tab.setPreferredSize(new Dimension(158, 42));
        tab.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tab.setForeground(Color.WHITE);
        tab.setFocusPainted(false);
        tab.setBorderPainted(false);
        tab.setContentAreaFilled(false);
        tab.setCursor(new Cursor(Cursor.HAND_CURSOR));

        tab.addActionListener(e -> {
            setActiveTab(tab);
            cardLayout.show(contentPanel, cardName);
        });

        return tab;
    }

    private void setActiveTab(TabButton tab) {
        if (activeTab != null) {
            activeTab.setActive(false);
        }

        activeTab = tab;
        activeTab.setActive(true);
    }

    private JPanel createDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 22));
        dashboard.setBackground(WHITE_BG);
        dashboard.setBorder(new EmptyBorder(34, 38, 38, 38));

        JPanel welcome = new JPanel(new GridLayout(2, 1, 0, 4));
        welcome.setOpaque(false);

        JLabel title = new JLabel("Bienvenue, " + user.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT_DARK);

        JLabel subtitle = new JLabel("Rôle : " + user.getRole());
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(TEXT_MUTED);

        welcome.add(title);
        welcome.add(subtitle);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        int patients = 0;
        int medecins = 0;
        int rdv = 0;

        try {
            patients = patientController.lister().size();
        } catch (Exception ignored) {}

        try {
            medecins = medecinController.lister().size();
        } catch (Exception ignored) {}

        try {
            rdv = rendezVousController.lister(
                    "MEDECIN".equals(user.getRole()) ? user.getMedecinId() : null
            ).size();
        } catch (Exception ignored) {}

        JPanel stats = new JPanel(new GridLayout(1, 3, 28, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        stats.setPreferredSize(new Dimension(0, 120));

        if ("MEDECIN".equals(user.getRole())) {
            stats.add(createStatCard("Mes rendez-vous", String.valueOf(rdv)));
            stats.add(createStatCard("Notifications", "Actives"));
            stats.add(createStatCard("Accès", "Limité"));
        } else {
            stats.add(createStatCard("Patients", String.valueOf(patients)));
            stats.add(createStatCard("Médecins", String.valueOf(medecins)));
            stats.add(createStatCard("Rendez-vous", String.valueOf(rdv)));
        }

        JPanel actions = new JPanel(new GridLayout(1, 3, 28, 0));
        actions.setOpaque(false);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        actions.setPreferredSize(new Dimension(0, 150));

        if ("ADMIN".equals(user.getRole())) {
            actions.add(createActionCard("Patients", "Gestion des dossiers patients."));
            actions.add(createActionCard("Médecins", "Gestion des médecins et spécialités."));
            actions.add(createActionCard("Utilisateurs", "Gestion des comptes et rôles."));
        }

        if ("SECRETAIRE".equals(user.getRole())) {
            actions.add(createActionCard("Patients", "Gestion des dossiers patients."));
            actions.add(createActionCard("Rendez-vous", "Planification des rendez-vous."));
            actions.add(createActionCard("Export", "Export PDF et Excel."));
        }

        if ("MEDECIN".equals(user.getRole())) {
            actions.add(createActionCard("Mes rendez-vous", "Consultation de vos rendez-vous."));
            actions.add(createActionCard("Notifications", "Notifications reçues."));
            actions.add(createActionCard("Sécurité", "Accès limité selon votre rôle."));
        }

        center.add(stats);
        center.add(Box.createVerticalStrut(34));
        center.add(actions);
        center.add(Box.createVerticalGlue());

        dashboard.add(welcome, BorderLayout.NORTH);
        dashboard.add(center, BorderLayout.CENTER);

        return dashboard;
    }

    private JPanel createStatCard(String title, String value) {
        DashboardCard card = new DashboardCard(false);
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(new EmptyBorder(24, 26, 22, 26));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createActionCard(String title, String text) {
        DashboardCard card = new DashboardCard(true);
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(new EmptyBorder(26, 26, 24, 26));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel desc = new JLabel(text);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc.setForeground(new Color(225, 235, 245));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(desc);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    static class GradientPanel extends JPanel {
        private final Color c1;
        private final Color c2;

        public GradientPanel(Color c1, Color c2) {
            this.c1 = c1;
            this.c2 = c2;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    static class DashboardCard extends JPanel {
        private final boolean actionCard;
        private boolean hovered = false;

        public DashboardCard(boolean actionCard) {
            this.actionCard = actionCard;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int radius = 14;
            int h = getHeight() - 8;
            int w = getWidth() - 8;

            g2d.setColor(SHADOW);
            g2d.fillRoundRect(4, 6, w, h, radius, radius);

            Color left = hovered ? new Color(12, 58, 105) : CARD_DARK;
            Color right = hovered ? new Color(16, 72, 128) : CARD_DARK_2;

            g2d.setPaint(new GradientPaint(0, 0, left, getWidth(), getHeight(), right));
            g2d.fillRoundRect(0, 0, w, h, radius, radius);

            g2d.setColor(CARD_BORDER);
            g2d.drawRoundRect(0, 0, w - 1, h - 1, radius, radius);

            if (actionCard) {
                g2d.setColor(new Color(255, 255, 255, 8));
                g2d.fillOval(getWidth() - 120, -80, 180, 180);
            }

            g2d.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedButton extends JButton {
        private boolean hovered = false;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color c1 = hovered ? new Color(45, 135, 215) : BLUE_ACTIVE;
            Color c2 = hovered ? new Color(28, 100, 170) : new Color(25, 93, 158);

            g2d.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            g2d.dispose();
            super.paintComponent(g);
        }
    }

    static class TabButton extends JButton {
        private boolean active = false;
        private boolean hovered = false;

        public TabButton(String text) {
            super(text);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color c1;
            Color c2;

            if (active) {
                c1 = BLUE_ACTIVE;
                c2 = new Color(25, 94, 160);
            } else if (hovered) {
                c1 = new Color(17, 60, 105);
                c2 = new Color(12, 48, 88);
            } else {
                c1 = new Color(12, 48, 88);
                c2 = new Color(9, 39, 72);
            }

            g2d.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            if (active) {
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(28, getHeight() - 5, getWidth() - 56, 3, 8, 8);
            }

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}