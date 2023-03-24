package com.erswebapp.backend.utilities;

import com.erswebapp.backend.exception.InvalidRoleException;
import com.erswebapp.backend.model.Role;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.ordinal();
    }

    @Override
    public Role convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (Role r : Role.values()) {
            if (r.ordinal() == dbData.intValue()) {
                return r;
            }
        }
        throw new InvalidRoleException("The role ordinal: " + dbData.intValue() + " can not be recognized");
    }
}
