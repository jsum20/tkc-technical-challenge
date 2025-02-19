package com.jason.tkc.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class ConnectionConverter implements AttributeConverter<List<Connection>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Connection> connections) {
        try {
            return mapper.writeValueAsString(connections);
        } catch (Exception e) {
            throw new RuntimeException("Error converting list of connections to JSON", e);
        }
    }

    @Override
    public List<Connection> convertToEntityAttribute(String json) {
        try {
            return mapper.readValue(json, new TypeReference<List<Connection>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to list of connections", e);
        }
    }
}