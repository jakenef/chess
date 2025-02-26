package dataaccess;

import java.util.HashMap;

public abstract class MemoryDataAccess<K, T> {
    protected final HashMap<K, T> dataMap = new HashMap<>();

    public void deleteAll() {
        dataMap.clear();
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (T value : dataMap.values()) {
            stringBuilder.append(value.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}
