package clinic;

import clinic.service.ClinicManager;
import clinic.ui.MainFrame;

import javax.swing.*;

/**
 * Program entry point.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            ClinicManager manager = new ClinicManager();
            MainFrame frame = new MainFrame(manager);
            frame.setVisible(true);

        });
    }
}
