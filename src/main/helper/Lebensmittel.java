package main.helper;

import main.utility.Option;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;

public class Lebensmittel {
    Kategorie kategorie;
    String name;
    LocalDate haltbarkeitsdatum;

    public Lebensmittel(Kategorie kategorie, String name, String haltbarkeitsdatum) throws DateTimeException {
        this.kategorie = kategorie;
        this.name = name;
        this.haltbarkeitsdatum = LocalDate.parse(haltbarkeitsdatum, Option.DATE_TIME_FORMATTER);
    }

    /**
     * vergleicht 'Lebensmittel' Objekt mit anderem Objekt
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true; // prüfen ob identisch
        if (object == null || getClass() != object.getClass()) return false; // prüfen ob Objekt null ist oder eine andere Klasse hat
        Lebensmittel lebensmittel = (Lebensmittel) object;
        return Objects.equals(kategorie, lebensmittel.kategorie) && Objects.equals(name, lebensmittel.name) && Objects.equals(haltbarkeitsdatum, lebensmittel.haltbarkeitsdatum);
    }

    @Override
    public int hashCode() { // Hashcode basierend auf Kategorie, Name und Haltbarkeitsdatum
        return Objects.hash(kategorie, name, haltbarkeitsdatum);
    }

    @Override
    public String toString() {
        return name + ": " + haltbarkeitsdatum.format(Option.DATE_TIME_FORMATTER);
    }
}
