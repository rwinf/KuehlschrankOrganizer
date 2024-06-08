public interface KuehlschrankOrganizerInterface {
    void saveLebensmittelInhalt();

    void loadLebensmittelInhalt();

    // Geändert: Methode zum Hinzufügen von Einträgen
    void hinzufuegenEintrag();

    // Geändert: Methode zum Entfernen von Einträgen
    void entfernenEintrag();

    boolean eingabePruefen(String eintrag, String haltbarkeitsdatum);

    // Geändert: Methode zur Aktualisierung der Anzeige
    void aktualisierenLebensmittelListe();
}
