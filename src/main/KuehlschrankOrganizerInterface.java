package main;

import javax.swing.*;

public interface KuehlschrankOrganizerInterface {
    void removeSelection(JList<String> jList1);

    void saveLebensmittelInhalt();

    void loadLebensmittelInhalt();

    // Geändert: Methode zum Hinzufügen von Einträgen
    void hinzufuegenEintrag();

    // Geändert: Methode zum Entfernen von Einträgen
    void entfernenEintrag();

    boolean eingabeIstFalsch(String eintrag, String haltbarkeitsdatum);

    // Geändert: Methode zur Aktualisierung der Anzeige
    void aktualisierenLebensmittelListe();
}
