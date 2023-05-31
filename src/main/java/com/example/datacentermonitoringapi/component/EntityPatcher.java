package com.example.datacentermonitoringapi.component;

import com.example.datacentermonitoringapi.configuration.exception.HttpRuntimeException;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityPatcher<E, T> {
    public E patch(E entity, T object) {
        Map<String, Object> map = convertToMap(object);
        return patch(entity, map);
    }

    private Map<String, Object> convertToMap(T object) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(object);
                map.put(field.getName(), value);
            } catch (IllegalAccessException e) {
                throw new HttpRuntimeException("Patch failed, IllegalAccessException", HttpStatus.BAD_REQUEST, e.getCause());
            }
        }
        return map;
    }

    private E patch(E entity, Map<String, Object> data) {
        Class<?> clazz = entity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            data.forEach((key, value) -> {
                if (key.equalsIgnoreCase(field.getName())) {
                    setValue(key, entity, value);
                }
            });
        }
        return entity;
    }

    private void setValue(String fieldName, E data, Object value) {
        try {
            var field = data.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getAnnotatedType().getType().getTypeName().equals(Long.class.getTypeName())) {
                var longValue = value != null ? Long.valueOf(value.toString()) : null;
                field.set(data, longValue);
            } else {
                field.set(data, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new HttpRuntimeException("Patch failed, NoSuchFieldException | IllegalAccessException", HttpStatus.BAD_REQUEST, e.getCause());
        }
    }
}
