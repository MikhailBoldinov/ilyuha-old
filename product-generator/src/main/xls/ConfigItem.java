package main.xls;

/**
 * @author Mikhail Boldinov, 20.09.15
 */
public class ConfigItem {
    private int rowOffset;
    private Column column;

    public ConfigItem(int rowOffset, Column column) {
        this.rowOffset = rowOffset;
        this.column = column;
    }

    int getRowOffset() {
        return rowOffset;
    }

    int getColNum() {
        return column.getNum();
    }
}
