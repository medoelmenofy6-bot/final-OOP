package clinic.ui;

import clinic.service.ClinicManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClinicManager manager = new ClinicManager();
            MainFrame frame = new MainFrame(manager);
            frame.setVisible(true);
        });
    }
}
