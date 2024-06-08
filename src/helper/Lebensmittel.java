package helper;

import utility.Option;

import java.time.LocalDate;
import java.util.Objects;

public class Lebensmittel {
    Kategorie kategorie;
    String name;
    LocalDate haltbarkeitsdatum;

    public Lebensmittel(Kategorie kategorie, String name, String haltbarkeitsdatum) {
        this.kategorie = kategorie;
        this.name = name;
        this.haltbarkeitsdatum = LocalDate.parse(haltbarkeitsdatum, Option.DATE_TIME_FORMATTER);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Lebensmittel lebensmittel = (Lebensmittel) object;
        return Objects.equals(kategorie, lebensmittel.kategorie) && Objects.equals(name, lebensmittel.name) && Objects.equals(haltbarkeitsdatum, lebensmittel.haltbarkeitsdatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kategorie, name, haltbarkeitsdatum);
    }

    @Override
    public String toString() {
        return "Lebensmittel{" +
                "kategorie=" + kategorie +
                ", name='" + name + '\'' +
                ", haltbarkeitsdatum=" + haltbarkeitsdatum +
                '}';
    }
}
