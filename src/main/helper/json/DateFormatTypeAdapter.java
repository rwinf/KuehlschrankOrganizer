package main.helper.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import main.utility.Option;

import java.io.IOException;
import java.time.LocalDate;

public class DateFormatTypeAdapter extends TypeAdapter<LocalDate> {
    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        if (localDate == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(localDate.format(Option.DATE_TIME_FORMATTER));
    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String string = jsonReader.nextString();
        return LocalDate.parse(string, Option.DATE_TIME_FORMATTER);
    }

    @Override
    public String toString() {
        return "DateFormatTypeAdapter{}";
    }
}
