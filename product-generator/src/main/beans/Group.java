package main.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Boldinov
 */
public class Group {
    private String name;
    private String separator;
    private List<Code> codes = new ArrayList<>();
    private int position = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void addCode(String key, String value) {
        this.codes.add(new Code(key, value));
    }

    public int getPosition() {
        return position;
    }

    public boolean incrementPosition() {
        if (position < codes.size() - 1) {
            position++;
            return true;
        } else {
            position = 0;
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
