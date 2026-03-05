package ug.daes.onboarding.util;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Converter
public class ListConverter implements AttributeConverter<List<String>, String> {
    private static Logger logger = LoggerFactory.getLogger(ListConverter.class);

    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(List list) {

        String listJson;
        try {
            listJson = objectMapper.writeValueAsString(list);
        } catch (final Exception e) {
            return null;
        }

        return listJson;
    }

    @Override
    public List convertToEntityAttribute(String listJson) {
        try {
            if (listJson == null || listJson.isBlank()) {
                return Collections.emptyList();
            }
            listJson = listJson.trim();
            return objectMapper.readValue(listJson, List.class);
        } catch (final Exception e) {
            logger.warn("Failed to deserialize listJson: {}", listJson, e);
            return Collections.emptyList();
        }
    }
}