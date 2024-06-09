import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helper.Kategorie;
import helper.Lebensmittel;
import helper.LebensmittelInhalt;
import helper.json.DateFormatTypeAdapter;
import utility.Sprache;
import utility.Option;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KuehlschrankOrganizer extends JFrame implements KuehlschrankOrganizerInterface { // Definition der Klasse
    private final JLabel haltbarkeitsdatumLabel;
    private final JLabel eingabeLabel;
    private final JButton hinzufuegenButton;
    private final JButton entfernenButton;
    private final JTextField lebensmittelEingabe;
    private final JTextField haltbarkeitsdatumEingabe; // Hinzugefügt: Eingabefeld für Haltbarkeitsdatum
    private final JPanel lebensmittelListePanel;
    private final List<JList<String>> lebensmitteljListListe;
    private final JButton spracheButton;
    // Hinzugefügt: ComboBox zur Auswahl der Kategorie
    private final JComboBox<String> kategorieAuswahl; // Dropdown-Menü zur Auswahl der Kategorie
    // Hinzugefügt: Zwei separate Listen für Lebensmittel und Getränke
    private LebensmittelInhalt lebensmittelInhalt;
    Gson gson;
    private int sprache = 0;

    public KuehlschrankOrganizer() {
        // Hier beginnt der Part zur Erstellung der grafischen Oberfläche
        // Rahmen und Layout
        setTitle(Sprache.getTitel(sprache));
        setSize(1200, 800); // Geändert: Größe angepasst
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Eingabebereich
        JPanel eingabePanel = new JPanel();
        eingabePanel.setLayout(new FlowLayout());

        eingabeLabel = new JLabel(Sprache.getEingabe(sprache) + ":");
        eingabePanel.add(eingabeLabel);

        lebensmittelEingabe = new JTextField(15); // Geändert: Größe angepasst
        eingabePanel.add(lebensmittelEingabe);

        haltbarkeitsdatumLabel = new JLabel(Sprache.getHaltbarkeitsdatum(sprache) + ":"); // Hinzugefügt: Label für Haltbarkeitsdatum
        eingabePanel.add(haltbarkeitsdatumLabel);

        haltbarkeitsdatumEingabe = new JTextField(10); // Hinzugefügt: Eingabefeld für Haltbarkeitsdatum
        eingabePanel.add(haltbarkeitsdatumEingabe);

        // Hinzugefügt: ComboBox zur Auswahl der Kategorie
        kategorieAuswahl = new JComboBox<>(Kategorie.getAsStringArray(sprache));
        eingabePanel.add(kategorieAuswahl);

        // Button zum Hinzufügen zur Liste
        hinzufuegenButton = new JButton(Sprache.getHinzufuegen(sprache));
        hinzufuegenButton.addActionListener(_ -> hinzufuegenEintrag());
        eingabePanel.add(hinzufuegenButton);

        // Button zum Entfernen aus der Liste
        entfernenButton = new JButton(Sprache.getEntfernen(sprache));
        entfernenButton.addActionListener(_ -> entfernenEintrag());
        eingabePanel.add(entfernenButton);

        add(eingabePanel, BorderLayout.NORTH);

        // Anzeigebereich
        lebensmittelListePanel = new JPanel();
        lebensmitteljListListe = new ArrayList<>();
        lebensmittelListePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (int i = 0; i < Kategorie.values().length; i++) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
            jPanel.setAlignmentY(0.0f);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            jPanel.setAlignmentY(100);
            jPanel.add(new JLabel(), gridBagConstraints);
            JList<String> lebensmittelListe = new JList<>(new DefaultListModel<>());
            lebensmittelListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lebensmittelListe.addListSelectionListener(_ -> removeSelection(lebensmittelListe));
            jPanel.add(lebensmittelListe, gridBagConstraints);
            lebensmitteljListListe.add(lebensmittelListe);
            lebensmittelListePanel.add(jPanel);
        }
        add(lebensmittelListePanel);

        //Initialisierung des Sprache-Buttons
        spracheButton = new JButton(Sprache.getSpracheName(0));
        spracheButton.addActionListener(_ -> spracheAendern());
        eingabePanel.add(spracheButton);

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

    @Override
    public void removeSelection(JList<String> jList1) {
        for(JList<String> jList2 : lebensmitteljListListe) {
            if(jList1.equals(jList2)) continue;
            jList2.clearSelection();
        }
    }

    @Override
    public void saveLebensmittelInhalt() { // speichern des aktuellen Inhalts vom Kühlschrank in eine JSON-Datei
        try {
            FileWriter fileWriter = new FileWriter(Option.PATH);
            gson.toJson(lebensmittelInhalt, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void loadLebensmittelInhalt() {  // lädt den Inhalt des Kühlschranks aus einer JSON Datei
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
    @Override
    public void hinzufuegenEintrag() {
        String eintrag = lebensmittelEingabe.getText().trim(); // Holt den Text aus einem Eingabefeld lebensmittelEingabe, entfernt führende und folgende Leerzeichen und speichert diesen in der Variablen lebensmittel.
        String haltbarkeitsdatum = haltbarkeitsdatumEingabe.getText().trim(); // Holt den Text aus dem Eingabefeld haltbarkeitsdatumEingabe, entfernt führende und folgende Leerzeichen und speichert diesen in der Variablen haltbarkeitsdatum.
        String kategorieAuswahlSelectedItem = (String) kategorieAuswahl.getSelectedItem();
        if (eingabePruefen(eintrag, haltbarkeitsdatum)) return;
        try {
            lebensmittelInhalt.add(Kategorie.get(kategorieAuswahlSelectedItem), eintrag, haltbarkeitsdatum);

        } catch (DateTimeException e) {
            JOptionPane.showMessageDialog(this, Sprache.getUngueltiges(sprache) + " "
                    + Sprache.getHaltbarkeitsdatum(sprache) + "!");
            return;
        }
        aktualisierenLebensmittelListe();
        lebensmittelEingabe.setText("");
        haltbarkeitsdatumEingabe.setText(""); // Hinzugefügt: Textfeld für Haltbarkeitsdatum leeren
    }

    // Geändert: Methode zum Entfernen von Einträgen
    @Override
    public void entfernenEintrag() {
        for (JList<String> stringJList : lebensmitteljListListe) {
            if (stringJList.isSelectionEmpty()) continue;
            for (Lebensmittel lebensmittel : lebensmittelInhalt) {
                if (lebensmittel.toString().equals(stringJList.getSelectedValue())) {
                    lebensmittelInhalt.remove(lebensmittel);
                    break;
                }
            }
        }
        aktualisierenLebensmittelListe();
    }

    @Override
    public boolean eingabePruefen(String eintrag, String haltbarkeitsdatum) {
        if (eintrag.isEmpty()
                || eintrag.contains("<")
                || eintrag.contains(">")
                || eintrag.contains("\"")) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe!");
            return true;
        }
        return false;
    }

    // Geändert: Methode zur Aktualisierung der Anzeige
    @Override
    public void aktualisierenLebensmittelListe() {
        int index = 0;
        for (Component component1 : lebensmittelListePanel.getComponents()) {
            if (!(component1 instanceof JPanel jPanel)) continue;
            for (Component component2 : jPanel.getComponents()) {
                if (!(component2 instanceof JLabel jLabel)) continue;
                if (lebensmittelInhalt.getNachKategorie(Kategorie.get(index++)).isEmpty()) {
                    jLabel.setText("");
                    continue;
                }
                jLabel.setText((Kategorie.get(index - 1)).toString(sprache));
            }

        }
        index = 0;
        for (JList<String> stringJList : lebensmitteljListListe) {
            if (!(stringJList.getModel() instanceof DefaultListModel<String> defaultListModel)) continue;
            defaultListModel.clear();
            for (Lebensmittel lebensmittel : lebensmittelInhalt.getNachKategorie(Kategorie.get(index++))) {
                defaultListModel.addElement(lebensmittel.toString());
            }
        }
    }

    private void spracheAendern() {
        sprache = Sprache.next(sprache);
        setText();
    }

    private void setText() {
        eingabeLabel.setText(Sprache.getEingabe(sprache) + ":");
        haltbarkeitsdatumLabel.setText(Sprache.getHaltbarkeitsdatum(sprache) + ":");
        kategorieAuswahl.removeAllItems();
        kategorieAuswahl.setModel(new DefaultComboBoxModel<>(Kategorie.getAsStringArray(sprache)));
        hinzufuegenButton.setText(Sprache.getHinzufuegen(sprache));
        entfernenButton.setText(Sprache.getEntfernen(sprache));
        spracheButton.setText(Sprache.getSpracheName(sprache));
        aktualisierenLebensmittelListe();
    }

    @Override
    public String toString() {
        return "KuehlschrankOrganizer{" +
                "lebensmittelEingabe=" + lebensmittelEingabe +
                ", haltbarkeitsdatumEingabe=" + haltbarkeitsdatumEingabe +
                ", lebensmittelListePanel=" + lebensmittelListePanel +
                ", lebensmitteljListListe=" + lebensmitteljListListe +
                ", kategorieAuswahl=" + kategorieAuswahl +
                ", lebensmittelInhalt=" + lebensmittelInhalt +
                ", gson=" + gson +
                ", sprache=" + sprache +
                '}';
    }

    public static void main(String[] args) {
        // Benutzt SwingUtilities.invokeLater, um sicherzustellen, dass der GUI-Code im Event-Dispatch-Thread (EDT) ausgeführt wird.
        // neue Instanz
        SwingUtilities.invokeLater(KuehlschrankOrganizer::new);
    }
}
