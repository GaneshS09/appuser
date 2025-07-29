package com.eagle.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class Patcher {

    public static <T> void fieldPatcher(T existingObject, T upateObject) throws IllegalAccessException {
            // GET THE COMPILED VERSION OF THE CLASS
        Class<?> objectClass = existingObject.getClass();
        Field[] objectField = objectClass.getDeclaredFields();

        for(Field field : objectField){
            // CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);
            // CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING OBJECT
            Object value = field.get(upateObject);

            if(value != null) {
                field.set(existingObject,value);
            }

            field.setAccessible(false);
        }

//        Class<?> objectClass = existingObject.getClass();
//        Field[] objectFields = objectClass.getDeclaredFields();
//        System.out.println(objectFields.length);
//
//        for (Field field : objectFields) {
//            System.out.println(field.getName());
//            // CANT ACCESS IF THE FIELD IS PRIVATE
//            field.setAccessible(true);
//            // CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING OBJECT
//            Object value = field.get(incompleteObject);
//            if (value != null) {
//                field.set(existingObject, value);
//            }
//            // MAKE THE FIELD PRIVATE AGAIN
//            field.setAccessible(false);
//        }
    }
}
