package core.xls;

/**
 * @author Mikhail Boldinov
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
