package util;

import javax.swing.JOptionPane;

public class MessageUtil {
    public static void info(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
