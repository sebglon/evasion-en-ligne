/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.dozer.CustomConverter;
import org.dozer.DozerConverter;
import org.dozer.MappingException;

/**
 *
 * @author sgl
 * @param <T>
 */
public class EnumToStringConverter implements CustomConverter {

@Override
public Object convert(Object destination, Object source, Class<?> destinationClass,    Class<?> sourceClass) {
    if(source == null)
        return null;
    if(destinationClass != null){
        if(destinationClass.getSimpleName().equalsIgnoreCase("String")){
            return this.getString(source);
        }else if( destinationClass.isEnum()){

            return this.getEnum(destinationClass, source);

        }else{
            throw new MappingException(new StringBuilder("Converter ").append(this.getClass().getSimpleName())
                       .append(" was used incorrectly. Arguments were: ")
                       .append(destinationClass.getClass().getName())
                       .append(" and ")
                       .append(source).toString());
        }
    }
    return null;
}

private Object getString(Object object){
    String value = object.toString();
    return value;
}
private Object getEnum(Class<?> destinationClass, Object source){
    Object enumeration = null;

    Method [] ms = destinationClass.getMethods();
    for(Method m : ms){
        if(m.getName().equalsIgnoreCase("valueOf")){
            try {
                enumeration = m.invoke( destinationClass.getClass(), (String)source);
            }
            catch (IllegalArgumentException e) {
                throw new MappingException(e);
            }
            catch (IllegalAccessException e) {
                throw new MappingException(e);
            }
            catch (InvocationTargetException e) {
                throw new MappingException(e);
            }
            return enumeration;
        }
    }
    return null;
}
}
