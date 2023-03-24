package com.erswebapp.backend.utilities;

import com.erswebapp.backend.exception.InvalidReimbursmentTypeException;
import com.erswebapp.backend.model.ReimbursmentType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ReimbursmentTypeConverter implements AttributeConverter<ReimbursmentType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ReimbursmentType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.ordinal();
    }

    @Override
    public ReimbursmentType convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (ReimbursmentType t : ReimbursmentType.values()) {
            if (t.ordinal() == dbData.intValue()) {
                return t;
            }
        }

        throw new InvalidReimbursmentTypeException(
                "The reimbusment type ordinal: " + dbData.intValue() + " can not be recognized");
    }

}
