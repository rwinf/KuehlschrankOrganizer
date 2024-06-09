package main;

import com.google.gson.Gson;

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

import com.google.gson.GsonBuilder;
import main.helper.Kategorie;
import main.helper.Lebensmittel;
import main.helper.LebensmittelInhalt;
import main.helper.json.DateFormatTypeAdapter;
import main.utility.Option;
import main.utility.Sprache;

public class KuehlschrankOrganizer extends JFrame implements KuehlschrankOrganizerInterface {
    private final JLabel haltbarkeitsdatumLabel;
    private final JLabel eingabeLabel;
    private final JButton hinzufuegenButton; //zum Hinzufügen eines Lebensmittels
    private final JButton entfernenButton; //zum Entfernen des ausgewählten Lebensmittels
    private final JTextField lebensmittelEingabe; //Eingabefenster des Lebensmittels
    private final JTextField haltbarkeitsdatumEingabe; //Eingabefenster des Haltbarkeitsdatums
    private final JPanel lebensmittelListePanel; //enthält alle JLists und stellt sie dar
    //enthält noch einmal alle JLists, die generiert werden, weil es sonst zu Problemen beim casten gekommen wäre
    private final List<JList<String>> lebensmitteljListListe;
    private final JButton spracheButton; //Wert zum Ändern der Sprache
    private final JComboBox<String> kategorieAuswahl; //Dropdown-Menü zur Auswahl der Kategorie
    private LebensmittelInhalt lebensmittelInhalt;
    Gson gson;
    private int sprache;

    public KuehlschrankOrganizer() {
        //setzen der Sprache bei Applikations-Start
        sprache = Sprache.DEUTSCH;

        //Hier beginnt der Part zur Erstellung der grafischen Oberfläche
        //Rahmen und Layout
        setSize(1200, 800); // Geändert: Größe angepasst
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Eingabebereich
        JPanel eingabePanel = new JPanel();
        eingabePanel.setLayout(new FlowLayout());

        eingabeLabel = new JLabel();
        eingabePanel.add(eingabeLabel);

        lebensmittelEingabe = new JTextField(15);
        eingabePanel.add(lebensmittelEingabe);

        haltbarkeitsdatumLabel = new JLabel();
        eingabePanel.add(haltbarkeitsdatumLabel);

        haltbarkeitsdatumEingabe = new JTextField(10);
        eingabePanel.add(haltbarkeitsdatumEingabe);

        // Auswahl der Kategorie
        kategorieAuswahl = new JComboBox<>();
        eingabePanel.add(kategorieAuswahl);

        // Button zum Hinzufügen zur Liste
        hinzufuegenButton = new JButton();
        hinzufuegenButton.addActionListener(_ -> hinzufuegenEintrag());
        eingabePanel.add(hinzufuegenButton);

        // Button zum Entfernen aus der Liste
        entfernenButton = new JButton();
        entfernenButton.addActionListener(_ -> entfernenEintrag());
        eingabePanel.add(entfernenButton);

        add(eingabePanel, BorderLayout.NORTH);

        // Initialisieren des Sprache-Buttons
        spracheButton = new JButton();
        spracheButton.addActionListener(_ -> spracheAendern());
        eingabePanel.add(spracheButton);

        //Anzeigebereich
        lebensmittelListePanel = new JPanel();
        lebensmitteljListListe = new ArrayList<>();
        lebensmittelListePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Es werden für jede Kategorie eine eigenes JLabel und JList generiert
        for (int i = 0; i < Kategorie.values().length; i++) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
            jPanel.setAlignmentY(0.0f);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            jPanel.add(new JLabel(), gridBagConstraints);
            JList<String> lebensmittelListe = new JList<>(new DefaultListModel<>());
            lebensmittelListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            lebensmittelListe.addListSelectionListener(_ -> removeSelection(lebensmittelListe));
            jPanel.add(lebensmittelListe, gridBagConstraints);
            lebensmitteljListListe.add(lebensmittelListe);
            lebensmittelListePanel.add(jPanel);
        }
        add(lebensmittelListePanel);

        // Initialisieren von GSON, für JSON Dateiarbeit
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new DateFormatTypeAdapter().nullSafe()).setPrettyPrinting().create();

        // Initialisieren der Liste aus der JSON Datei
        loadLebensmittelInhalt();
        aktualisierenLebensmittelListe();
        setText();

        // Anzeigen
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveLebensmittelInhalt();
            }
        });
    }

    /**
     * verhindert, dass man nicht mehr von einer JList deselektieren kann
     * @param jList1 jList, die aktuell ausgewählt ist
     */
    @Override
    public void removeSelection(JList<String> jList1) {
        for (JList<String> jList2 : lebensmitteljListListe) {
            if (jList1.equals(jList2)) continue;
            jList2.clearSelection();
        }
    }

    /**
     * speichern der ArrayList lebensmittelInhalt in einer JSON-Datei
     */
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

    /**
     * laden der ArrayList lebensmittelInhalt aus einer JSON-Datei
     */
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

    /**
     * wenn ein Eintrag durch User-Eingabe hinzugefügt wird
     */
    @Override
    public void hinzufuegenEintrag() {
        //auswerten der Eingabe-Daten
        String eintrag = lebensmittelEingabe.getText().trim(); // Holt den Text aus einem Eingabefeld lebensmittelEingabe, entfernt führende und folgende Leerzeichen und speichert diesen in der Variablen lebensmittel.
        String haltbarkeitsdatum = haltbarkeitsdatumEingabe.getText().trim(); // Holt den Text aus dem Eingabefeld haltbarkeitsdatumEingabe, entfernt führende und folgende Leerzeichen und speichert diesen in der Variablen haltbarkeitsdatum.
        String kategorieAuswahlSelectedItem = (String) kategorieAuswahl.getSelectedItem();
        if (eingabeIstFalsch(eintrag)) return;

        //hinzufügen eines neuen Lebensmittels aus den Eingabedaten zu lebensmittelInhalt
        try {
            lebensmittelInhalt.add(Kategorie.get(kategorieAuswahlSelectedItem), eintrag, haltbarkeitsdatum);

        } catch (DateTimeException e) {
            JOptionPane.showMessageDialog(this, Sprache.getUngueltiges(sprache) + " " + Sprache.getHaltbarkeitsdatum(sprache) + "!");
            return;
        }

        //aktualisierung der UI
        aktualisierenLebensmittelListe();
        lebensmittelEingabe.setText("");
        haltbarkeitsdatumEingabe.setText("");
    }

    /**
     * wenn ein Eintrag durch User-Eingabe entfernt wird
     */
    @Override
    public void entfernenEintrag() {
        //herausfinden, in welcher JList das Lebensmittel selektiert wurde
        for (JList<String> stringJList : lebensmitteljListListe) {
            if (stringJList.isSelectionEmpty()) continue;

            //herausfinden, welches Lebensmittel in der selektierten JList entfernt werden soll
            for (Lebensmittel lebensmittel : lebensmittelInhalt) {
                if (lebensmittel.toString().equals(stringJList.getSelectedValue())) {
                    lebensmittelInhalt.remove(lebensmittel); //Lebensmittel entfernen
                    break;
                }
            }
        }
        aktualisierenLebensmittelListe();
    }

    /**
     * @param eintrag zu überprüfender String
     * @return ob Feld Eintrag den vorgegebenen Anforderungen entspricht
     */
    @Override
    public boolean eingabeIstFalsch(String eintrag) {
        if (eintrag.isEmpty() || eintrag.contains("<") || eintrag.contains(">") || eintrag.contains("\"")) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe!");
            return true;
        }
        return false;
    }

    /**
     * entfernt alle Elemente aus lebensMittelListe und fügt die aktualisierten Daten wieder hinzu
     */
    @Override
    public void aktualisierenLebensmittelListe() {
        int index = 0;
        //iteriere durch die Komponenten von lebensmittelListePanel
        for (Component component1 : lebensmittelListePanel.getComponents()) {
            if (!(component1 instanceof JPanel jPanel)) continue;

            //iteriere durch die (2) Komponenten des JPanel
            for (Component component2 : jPanel.getComponents()) {
                if (!(component2 instanceof JLabel jLabel)) continue;

                //wenn es keine Lebensmittel einer Kategorie gibt, lasse das Label leer
                if (lebensmittelInhalt.getNachKategorie(Kategorie.get(index++)).isEmpty()) {
                    jLabel.setText("");
                    continue;
                }
                //setze das Label zum respektiven String aus sprache
                jLabel.setText((Kategorie.get(index - 1)).toString(sprache));
            }

        }
        index = 0;
        //iteriere durch die JLists aus lebensmitteljListListe
        for (JList<String> stringJList : lebensmitteljListListe) {
            if (!(stringJList.getModel() instanceof DefaultListModel<String> defaultListModel)) continue;
            defaultListModel.clear(); //lösche alle Elemente der Liste

            //füge alle Elemente zur JList hinzu
            for (Lebensmittel lebensmittel : lebensmittelInhalt.getNachKategorie(Kategorie.get(index++))) {
                defaultListModel.addElement(lebensmittel.toString());
            }
        }
    }

    //ändere die Sprache zur nächsten Sprache
    private void spracheAendern() {
        sprache = Sprache.next(sprache);
        setText();
    }

    //aktualisiere die UI
    private void setText() {
        setTitle(Sprache.getTitel(sprache));
        eingabeLabel.setText(Sprache.getEingabe(sprache) + ":");
        haltbarkeitsdatumLabel.setText(Sprache.getHaltbarkeitsdatum(sprache) + ":");
        kategorieAuswahl.removeAllItems();
        kategorieAuswahl.setModel(new DefaultComboBoxModel<>(Kategorie.getAlsStringArray(sprache)));
        hinzufuegenButton.setText(Sprache.getHinzufuegen(sprache));
        entfernenButton.setText(Sprache.getEntfernen(sprache));
        spracheButton.setText(Sprache.getSpracheName(sprache));
        aktualisierenLebensmittelListe();
    }

    @Override
    public String toString() {
        return "KuehlschrankOrganizer{" + "lebensmittelEingabe=" + lebensmittelEingabe + ", haltbarkeitsdatumEingabe=" + haltbarkeitsdatumEingabe + ", lebensmittelListePanel=" + lebensmittelListePanel + ", lebensmitteljListListe=" + lebensmitteljListListe + ", kategorieAuswahl=" + kategorieAuswahl + ", lebensmittelInhalt=" + lebensmittelInhalt + ", gson=" + gson + ", sprache=" + sprache + '}';
    }
}
