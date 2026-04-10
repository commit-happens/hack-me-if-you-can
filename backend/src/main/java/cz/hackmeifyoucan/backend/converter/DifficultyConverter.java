package cz.hackmeifyoucan.backend.converter;

import cz.hackmeifyoucan.backend.enums.Difficulty;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DifficultyConverter implements AttributeConverter<Difficulty, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Difficulty attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getLevel();
    }

    @Override
    public Difficulty convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Difficulty.fromLevel(dbData);
    }
}

