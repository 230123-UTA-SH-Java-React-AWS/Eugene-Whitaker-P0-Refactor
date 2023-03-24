package com.erswebapp.backend.utilities;

import com.erswebapp.backend.exception.InvalidStatusException;
import com.erswebapp.backend.model.Status;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Status attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.ordinal();
    }

    @Override
    public Status convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        for (Status s : Status.values()) {
            if (s.ordinal() == dbData.intValue()) {
                return s;
            }
        }

        throw new InvalidStatusException("The status ordinal: " + dbData.intValue() + " can not be recognized");
    }

}
