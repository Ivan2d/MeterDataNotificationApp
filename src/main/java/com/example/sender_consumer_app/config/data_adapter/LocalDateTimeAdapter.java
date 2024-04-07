package com.example.sender_consumer_app.config.data_adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public JsonElement serialize(LocalDateTime src,
                                 Type typeOfSrc,
                                 JsonSerializationContext context) {
        return new JsonPrimitive(DATE_TIME_FORMATTER.format(src));
    }


    @Override
    public LocalDateTime deserialize(JsonElement json,
                                     Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), DATE_TIME_FORMATTER);
    }
}