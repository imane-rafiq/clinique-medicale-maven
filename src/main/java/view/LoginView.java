package view;

import controller.AuthController;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController();

        setTitle("Connexion - Clinique Médicale");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel main = new BackgroundPanel("src/main/resources/images/clinique1.png");
        main.setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(360, 230));

        // === TEXT FIELD STYLE ===
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setBackground(new Color(255, 255, 255, 200)); // semi-transparent white
        txtUsername.setForeground(Color.DARK_GRAY);
        txtUsername.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(0, 120, 200), 2),
                "Nom d'utilisateur"
        ));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBackground(new Color(255, 255, 255, 200));
        txtPassword.setForeground(Color.DARK_GRAY);
        txtPassword.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(0, 120, 200), 2),
                "Mot de passe"
        ));

        JButton btnLogin = new JButton("Se connecter");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 15));
        btnLogin.setBackground(new Color(15, 53, 108));
        btnLogin.setForeground(Color.WHITE);

        btnLogin.setOpaque(true);
        btnLogin.setContentAreaFilled(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lostPasswordLabel = new JLabel("Mot de passe oublié? Contactez l'administration");
        lostPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lostPasswordLabel.setForeground(new Color(150, 150, 150));
        lostPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lostPasswordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername.setMaximumSize(new Dimension(360, 60));
        txtPassword.setMaximumSize(new Dimension(360, 60));
        btnLogin.setMaximumSize(new Dimension(360, 38));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(txtUsername);
        form.add(Box.createVerticalStrut(12));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(16));
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(15));
        form.add(lostPasswordLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(185, 0, 0, 0);

        main.add(form, gbc);

        add(main);

        btnLogin.addActionListener(e -> login());
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        Utilisateur user = authController.seConnecter(username, password);

        if (user != null) {
            new DashboardView(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Nom d'utilisateur ou mot de passe incorrect",
                    "Erreur de connexion",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}