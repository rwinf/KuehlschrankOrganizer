import main.KuehlschrankOrganizer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Benutzt SwingUtilities.invokeLater, um sicherzustellen, dass der GUI-Code im Event-Dispatch-Thread (EDT) ausgeführt wird.
        // neue Instanz
        SwingUtilities.invokeLater(KuehlschrankOrganizer::new);
    }
}
