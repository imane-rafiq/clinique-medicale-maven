package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class ViewStyle {
    private ViewStyle() {}

    public static final Color WHITE_BG = new Color(250, 252, 255);
    public static final Color HEADER_DARK = new Color(5, 22, 43);
    public static final Color NAV_DARK = new Color(8, 35, 65);
    public static final Color CARD_DARK = new Color(7, 42, 78);
    public static final Color CARD_DARK_2 = new Color(10, 58, 105);
    public static final Color CARD_BORDER = new Color(18, 76, 130);
    public static final Color BLUE_ACTIVE = new Color(32, 112, 185);
    public static final Color TEXT_DARK = new Color(8, 35, 65);
    public static final Color TEXT_MUTED = new Color(90, 105, 122);
    public static final Color SHADOW = new Color(0, 0, 0, 28);

    public static Font titleFont() {
        return new Font("Segoe UI", Font.BOLD, 28);
    }

    public static Font labelFont() {
        return new Font("Segoe UI", Font.BOLD, 13);
    }

    public static Font fieldFont() {
        return new Font("Segoe UI", Font.PLAIN, 14);
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(titleFont());
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(labelFont());
        label.setForeground(Color.WHITE);
        return label;
    }

    public static JTextField field() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    public static void styleField(JTextField field) {
        field.setFont(fieldFont());
        field.setForeground(TEXT_DARK);
        field.setBackground(Color.WHITE);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 222, 235)),
                new EmptyBorder(7, 10, 7, 10)
        ));
    }

    public static void styleCombo(JComboBox<?> combo) {
        combo.setFont(fieldFont());
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT_DARK);
        combo.setBorder(BorderFactory.createLineBorder(new Color(210, 222, 235)));
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(fieldFont());
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField field = ((JSpinner.DefaultEditor) editor).getTextField();
            styleField(field);
        }
    }

    public static JPanel panel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(WHITE_BG);
        panel.setBorder(new EmptyBorder(30, 38, 35, 38));
        return panel;
    }

    public static JPanel formCard() {
        JPanel panel = new RoundedPanel(true);
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(22, 22, 22, 22));
        return panel;
    }

    public static JPanel buttonPanel(int rows) {
        JPanel panel = new JPanel(new GridLayout(rows, 1, 0, 12));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(4, 38, 0, 38));
        return panel;
    }

    public static JButton button(String text) {
        return new NavyButton(text);
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(31);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT_DARK);
        table.setSelectionBackground(new Color(218, 235, 252));
        table.setSelectionForeground(TEXT_DARK);
        table.setGridColor(new Color(226, 232, 240));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setBackground(HEADER_DARK);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
    }

    public static JScrollPane scroll(JTable table) {
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 228, 238)));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public static class RoundedPanel extends JPanel {
        private final boolean dark;

        public RoundedPanel(boolean dark) {
            this.dark = dark;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth() - 8;
            int h = getHeight() - 8;

            g2.setColor(SHADOW);
            g2.fillRoundRect(4, 6, w, h, 14, 14);

            if (dark) {
                g2.setPaint(new GradientPaint(0, 0, CARD_DARK, getWidth(), getHeight(), CARD_DARK_2));
            } else {
                g2.setColor(Color.WHITE);
            }
            g2.fillRoundRect(0, 0, w, h, 14, 14);

            g2.setColor(dark ? CARD_BORDER : new Color(220, 228, 238));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class NavyButton extends JButton {
        private boolean hovered;

        public NavyButton(String text) {
            super(text);
            setPreferredSize(new Dimension(180, 40));
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color c1;
            Color c2;

            if (!isEnabled()) {
                c1 = new Color(120, 135, 150);
                c2 = new Color(105, 120, 135);
            } else if (hovered) {
                c1 = new Color(45, 135, 215);
                c2 = new Color(28, 100, 170);
            } else {
                c1 = BLUE_ACTIVE;
                c2 = new Color(25, 93, 158);
            }

            g2.setPaint(new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
