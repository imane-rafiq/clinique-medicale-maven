package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UITheme {
    public static final Color BLUE_DARK = new Color(13, 59, 102);
    public static final Color BLUE = new Color(30, 118, 210);
    public static final Color BLUE_HOVER = new Color(21, 101, 192);
    public static final Color GREEN = new Color(0, 150, 136);
    public static final Color BG = new Color(245, 248, 252);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(35, 35, 35);
    public static final Color MUTED = new Color(110, 120, 130);
    public static final Color BORDER = new Color(220, 230, 240);

    public static Font titleFont() {
        return new Font("Segoe UI", Font.BOLD, 30);
    }

    public static Font subtitleFont() {
        return new Font("Segoe UI", Font.PLAIN, 15);
    }

    public static Font textFont() {
        return new Font("Segoe UI", Font.PLAIN, 14);
    }

    public static Font buttonFont() {
        return new Font("Segoe UI", Font.BOLD, 14);
    }

    public static JButton button(String text) {
        JButton button = new JButton(text);
        button.setFont(buttonFont());
        button.setForeground(Color.WHITE);
        button.setBackground(GREEN);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 38));
        return button;
    }

    public static JTextField field() {
        JTextField field = new JTextField();
        field.setFont(textFont());
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(textFont());
        label.setForeground(TEXT);
        return label;
    }

    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    public static void styleTable(JTable table) {
        table.setFont(textFont());
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(220, 235, 250));
        table.setSelectionForeground(TEXT);
        table.setGridColor(new Color(235, 240, 245));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(GREEN);
        header.setForeground(Color.BLACK);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(0, 38));

        table.getTableHeader().setReorderingAllowed(false);
    }
}