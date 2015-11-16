package main.beans;

/**
 * @author Mikhail Boldinov
 */
public class Code {
    private String key;
    private String value;

    public Code(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
