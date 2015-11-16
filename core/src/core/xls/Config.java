package core.xls;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Boldinov
 */
public class Config {
    private Map<IField, ConfigItem> configItems = new HashMap<>();

    public void add(IField field, ConfigItem configItem) {
        configItems.put(field, configItem);
    }

    public int getRow(IField field) {
        return get(field).getRowOffset();
    }

    public int getColumn(IField field) {
        return get(field).getColNum();
    }

    private ConfigItem get(IField field) {
        if (!configItems.containsKey(field)) {
            throw new IllegalArgumentException(String.format("No config found for field '%s'", field));
        }
        return configItems.get(field);
    }
}

