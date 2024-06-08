import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helper.Kategorie;
import helper.Lebensmittel;
import helper.LebensmittelInhalt;
import helper.json.DateFormatTypeAdapter;
import utility.Option;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class KuehlschrankOrganizer extends JFrame { // Definition der Klasse
    private final JTextField lebensmittelEingabe;
    private final JTextField haltbarkeitsdatumEingabe; // Hinzugefügt: Eingabefeld für Haltbarkeitsdatum
    private final JTextArea lebensmittelListe;
    // Hinzugefügt: ComboBox zur Auswahl der Kategorie
    private final JComboBox<String> kategorieAuswahl; // Dropdown-Menü zur Auswahl der Kategorie
    // Hinzugefügt: Zwei separate Listen für Lebensmittel und Getränke
    private LebensmittelInhalt lebensmittelInhalt;
    Gson gson;

    public KuehlschrankOrganizer() {
        // Hier beginnt der Part zur Erstellung der grafischen Oberfläche
        // Rahmen und Layout
        setTitle("Kühlschrank Organizer");
        setSize(1200, 800); // Geändert: Größe angepasst
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Eingabebereich
        JPanel eingabePanel = new JPanel();
        eingabePanel.setLayout(new FlowLayout());

        JLabel lebensmittelLabel = new JLabel("Eingabe:");
        eingabePanel.add(lebensmittelLabel);

        lebensmittelEingabe = new JTextField(15); // Geändert: Größe angepasst
        eingabePanel.add(lebensmittelEingabe);

        JLabel haltbarkeitsdatumLabel = new JLabel("Haltbarkeitsdatum:"); // Hinzugefügt: Label für Haltbarkeitsdatum
        eingabePanel.add(haltbarkeitsdatumLabel);

        haltbarkeitsdatumEingabe = new JTextField(10); // Hinzugefügt: Eingabefeld für Haltbarkeitsdatum
        eingabePanel.add(haltbarkeitsdatumEingabe);

        // Hinzugefügt: ComboBox zur Auswahl der Kategorie
        kategorieAuswahl = new JComboBox<>(Kategorie.getAsStringArray());
        eingabePanel.add(kategorieAuswahl);

        // Button zum Hinzufügen zur Liste
        JButton hinzufuegenButton = new JButton("Hinzufügen");
        hinzufuegenButton.addActionListener(_ -> hinzufuegenEintrag());
        eingabePanel.add(hinzufuegenButton);

        // Button zum Entfernen aus der Liste
        JButton entfernenButton = new JButton("Entfernen");
        entfernenButton.addActionListener(_ -> entfernenEintrag());
        eingabePanel.add(entfernenButton);

        add(eingabePanel, BorderLayout.NORTH);

        // Anzeigebereich
        lebensmittelListe = new JTextArea();
        lebensmittelListe.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(lebensmittelListe);
        add(scrollPane, BorderLayout.CENTER);

        //Initialisierung von GSON, für JSON Dateiarbeit
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new DateFormatTypeAdapter().nullSafe())
                .setPrettyPrinting().create();

        // Initialisierung der Liste aus der JSON Datei
        loadLebensmittelInhalt();
        aktualisierenLebensmittelListe();

        // Anzeigen
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveLebensmittelInhalt();
            }
        });
        // Hier endet der Part zur Erstellung der grafischen Oberfläche
    }

    private void saveLebensmittelInhalt() { // speichern des aktuellen Inhalts vom Kühlschrank in eine JSON-Datei
        try {
            FileWriter fileWriter = new FileWriter(Option.PATH);
            gson.toJson(lebensmittelInhalt, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void loadLebensmittelInhalt() {  // lädt den Inhalt des Kühlschranks aus einer JSON Datei
        try {
            FileReader fileReader = new FileReader(Option.PATH);
            lebensmittelInhalt = gson.fromJson(fileReader, LebensmittelInhalt.class);
            fileReader.close();
        } catch (FileNotFoundException e) {
            lebensmittelInhalt = new LebensmittelInhalt();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // Geändert: Methode zum Hinzufügen von Einträgen
    private void hinzufuegenEintrag() {
        String eintrag = lebensmittelEingabe.getText().trim(); // Holt den Text aus einem Eingabefeld lebensmittelEingabe, entfernt führende und folgende Leerzeichen und speichert diesen in der Variablen lebensmittel.
        String haltbarkeitsdatum = haltbarkeitsdatumEingabe.getText().trim(); // Holt den Text aus dem Eingabefeld haltbarkeitsdatumEingabe, entfernt führende und folgende Leerzeichen und speichert diesen in der Variablen haltbarkeitsdatum.
        if (!eintrag.isEmpty() && !haltbarkeitsdatum.isEmpty()) {
            String kategorie = (String) kategorieAuswahl.getSelectedItem();
            lebensmittelInhalt.add(Kategorie.get(kategorie), eintrag, haltbarkeitsdatum);
            aktualisierenLebensmittelListe();
            lebensmittelEingabe.setText("");
            haltbarkeitsdatumEingabe.setText(""); // Hinzugefügt: Textfeld für Haltbarkeitsdatum leeren
        }
    }

    // Geändert: Methode zum Entfernen von Einträgen
    private void entfernenEintrag() {
        String eintrag = lebensmittelEingabe.getText().trim();
        String haltbarkeitsdatum = haltbarkeitsdatumEingabe.getText().trim();
        if (!eintrag.isEmpty() && !haltbarkeitsdatum.isEmpty()) {
            String kategorie = (String) kategorieAuswahl.getSelectedItem();
            lebensmittelInhalt.remove(Kategorie.get(kategorie), eintrag, haltbarkeitsdatum);
            aktualisierenLebensmittelListe();
            lebensmittelEingabe.setText("");
            haltbarkeitsdatumEingabe.setText(""); // Hinzugefügt: Textfeld für Haltbarkeitsdatum leeren
        }
    }

    // Geändert: Methode zur Aktualisierung der Anzeige
    private void aktualisierenLebensmittelListe() {
        lebensmittelListe.setText(""); // setzt den Textbereich auf einen leeren String, wodurch der bisherige Inhalt gelöscht wird
        Map<Kategorie, LebensmittelInhalt> kategorieLebensmittelInhaltMap = lebensmittelInhalt.getNachKategorie(); // Map ordnet jeder kategorie ihren jeweiligen Inhalt zu
        for (Kategorie kategorie : kategorieLebensmittelInhaltMap.keySet()) {
            if (kategorieLebensmittelInhaltMap.get(kategorie).isEmpty()) continue;
            lebensmittelListe.append(kategorie.toString() + ":\n");
            for (Lebensmittel lebensmittel : kategorieLebensmittelInhaltMap.get(kategorie)) {
                lebensmittelListe.append(lebensmittel.toString() + "\n");
            }
            lebensmittelListe.append("\n");
        }
    }

    @Override
    public String toString() {
        return "KuehlschrankOrganizer{" +
                "lebensmittelEingabe=" + lebensmittelEingabe +
                ", haltbarkeitsdatumEingabe=" + haltbarkeitsdatumEingabe +
                ", lebensmittelListe=" + lebensmittelListe +
                ", kategorieAuswahl=" + kategorieAuswahl +
                ", lebensmittelInhalt=" + lebensmittelInhalt +
                ", gson=" + gson +
                '}';
    }

    public static void main(String[] args) {
        // Benutzt SwingUtilities.invokeLater, um sicherzustellen, dass der GUI-Code im Event-Dispatch-Thread (EDT) ausgeführt wird.
        // neue Instanz
        SwingUtilities.invokeLater(KuehlschrankOrganizer::new);
    }
}
