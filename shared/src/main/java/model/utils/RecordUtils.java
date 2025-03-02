package model.utils;

import java.lang.reflect.Field;

public class RecordUtils {

    public static boolean isNull(Object record){
        if (record == null){
            return true;
        }
        Field[] fields = record.getClass().getDeclaredFields();
        try {
            for(Field field : fields){
                field.setAccessible(true);
                Object fieldVal = field.get(record);
                if (fieldVal instanceof String strVal && strVal.isEmpty()) {
                    return true;
                }
                if (fieldVal == null){
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
        return false;
    }
}
