package cz.hackmeifyoucan.backend.converter;

import cz.hackmeifyoucan.backend.enums.PlatformType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PlatformTypeConverter implements AttributeConverter<PlatformType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PlatformType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getId();
    }

    @Override
    public PlatformType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return PlatformType.fromId(dbData);
    }
}

