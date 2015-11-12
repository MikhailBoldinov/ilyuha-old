package main.xls;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Boldinov, 17.09.15
 */
public class Config {
    private Map<Field, ConfigItem> configItems = new HashMap<>();

    public void add(Field field, ConfigItem configItem) {
        configItems.put(field, configItem);
    }

    public int getRow(Field field) {
        return get(field).getRowOffset();
    }

    public int getColumn(Field field) {
        return get(field).getColNum();
    }

    private ConfigItem get(Field field) {
        if (!configItems.containsKey(field)) {
            throw new IllegalArgumentException(String.format("No config found for field '%s'", field));
        }
        return configItems.get(field);
    }
}

