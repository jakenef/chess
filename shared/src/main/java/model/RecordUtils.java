package model;

import com.google.gson.Gson;

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
                String fieldVal = (String) field.get(record);
                if (fieldVal.isEmpty()){
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
        return false;
    }
}
