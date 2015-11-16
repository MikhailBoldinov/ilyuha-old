package core.io.impl;

import core.xls.Column;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Mikhail Boldinov
 */
public abstract class AbstractWriter {
    protected static final int MAX_ROWS_NUMBER = 65535;

    protected String fileName;
    protected Workbook wb;

    protected AbstractWriter(String fileName) {
        this.fileName = fileName;
        wb = new HSSFWorkbook();
    }

    protected Sheet createNewSheet(Workbook wb) {
        Sheet sheet = wb.createSheet();
        sheet.createFreezePane(0, 1);
        writeHeader(sheet);
        return sheet;
    }

    protected abstract void writeHeader(Sheet sheet);

    protected void addCell(Sheet sheet, int rowNumber, Column column, CellStyle style) {
        addCell(sheet, rowNumber, column, null, style);
    }

    protected void addCell(Sheet sheet, int rowNumber, Column column, String value, CellStyle style) {
        Row row = sheet.getRow(rowNumber);
        Cell cell = row.createCell(column.getNum());
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(style);
    }

    protected void setColumnWidth(Sheet sheet, Column column, boolean autoSize) {
        setColumnWidth(sheet, column, autoSize, 0);
    }

    protected void setColumnWidth(Sheet sheet, Column column, boolean autoSize, int width) {
        if (autoSize) {
            sheet.autoSizeColumn(column.getNum());
        } else {
            sheet.setColumnWidth(column.getNum(), width);
        }
    }
}
