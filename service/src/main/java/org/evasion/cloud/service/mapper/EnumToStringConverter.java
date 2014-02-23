/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.dozer.CustomConverter;
import org.dozer.MappingException;

/**
 *
 * @author sgl
 */
public class EnumToStringConverter implements CustomConverter {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object convert(Object existingDestinationFieldValue,
            Object sourceFieldValue, Class destinationClass, Class sourceClass) {
        if (null == sourceFieldValue) {
            return null;
        }
        if (Enum.class.isAssignableFrom(destinationClass)) {
            return Enum.valueOf((Class<Enum>) destinationClass,
                    (String) sourceFieldValue);
        }
        if (Enum.class.isAssignableFrom(sourceClass)) {
            return sourceFieldValue.toString();
        }
        return null;
    }
}
