package main.xls;

import core.io.IWriter;
import core.io.impl.AbstractWriter;
import main.beans.Code;
import main.beans.ProductResult;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileOutputStream;
import java.io.IOException;

import static core.xls.Column.A;
import static core.xls.Column.B;
import static core.xls.Column.C;
import static core.xls.Column.D;
import static core.xls.Column.E;
import static core.xls.Column.F;
import static core.xls.Column.G;
import static core.xls.Column.H;
import static core.xls.Column.I;
import static core.xls.Column.J;
import static core.xls.Column.K;
import static core.xls.Column.L;
import static core.xls.Column.M;
import static core.xls.Column.N;
import static core.xls.Column.O;
import static core.xls.Column.P;
import static core.xls.Column.Q;

/**
 * @author Mikhail Boldinov
 */
public class ProductWriter extends AbstractWriter implements IWriter<ProductResult> {

    private static final int EMPTY_COLUMN_WIDTH = 600;
    private static final int SHORT_DESCRIPTION_COLUMN_WIDTH = 8000;
    private static final int DESCRIPTION_COLUMN_WIDTH = 12000;
    private static final short HEADER_HEIGHT = 500;
    private static final short ROW_HEIGHT = 1200;
    private static final int HEADER_ROW_NUMBER = 0;
    private static final String FONT_NAME = "Calibri";
    private static final short FONT_SIZE = 11;

    public ProductWriter(String fileName) {
        super(fileName);
    }

    @Override
    public void write(ProductResult productResult) throws IOException {
        Sheet sheet = createNewSheet(wb);
        CellStyle evenRowStyle = getEvenRowStyle(true);
        CellStyle evenRowStyleNoBorder = getEvenRowStyle(false);
        CellStyle oddRowStyle = getOddRowStyle(true);
        CellStyle oddRowStyleNoBorder = getOddRowStyle(false);
        CellStyle style, styleNoBorder;

        int currentRow = HEADER_ROW_NUMBER + 1;
        for (Code productCode : productResult.getProductCodes()) {
            Row row = sheet.createRow(currentRow);
            if (currentRow % 2 == 0) {
                style = evenRowStyle;
                styleNoBorder = evenRowStyleNoBorder;
            } else {
                style = oddRowStyle;
                styleNoBorder = oddRowStyleNoBorder;
            }
            row.setHeight(ROW_HEIGHT);
            addCell(sheet, currentRow, A, productResult.getProducer_serialNumber(), style);
            addCell(sheet, currentRow, B, productResult.getModel(productCode.getKey()), style);
            addCell(sheet, currentRow, C, styleNoBorder);
            addCell(sheet, currentRow, D, productResult.getProducer_model(productCode.getKey()), style);
            addCell(sheet, currentRow, E, styleNoBorder);
            addCell(sheet, currentRow, F, styleNoBorder);
            addCell(sheet, currentRow, G, productResult.getProducer(), style);
            addCell(sheet, currentRow, H, styleNoBorder);
            addCell(sheet, currentRow, I, styleNoBorder);
            addCell(sheet, currentRow, J, styleNoBorder);
            addCell(sheet, currentRow, K, styleNoBorder);
            addCell(sheet, currentRow, L, styleNoBorder);
            addCell(sheet, currentRow, M, styleNoBorder);
            addCell(sheet, currentRow, N, styleNoBorder);
            addCell(sheet, currentRow, O, productResult.getShortDescription(), style);
            addCell(sheet, currentRow, P, productResult.getDescription(productCode.getKey(), productCode.getValue()), style);
            addCell(sheet, currentRow, Q, productResult.getImage(), style);
            currentRow++;
            if (currentRow > MAX_ROWS_NUMBER) {
                sheet = createNewSheet(wb);
                currentRow = HEADER_ROW_NUMBER + 1;
            }
        }
        setColumnsWidth();

        FileOutputStream fileOut = new FileOutputStream(fileName);
        wb.write(fileOut);
        fileOut.close();
    }

    protected void writeHeader(Sheet sheet) {
        Row header = sheet.createRow(HEADER_ROW_NUMBER);
        header.setHeight(HEADER_HEIGHT);

        CellStyle style = getHeaderStyle();
        addCell(sheet, HEADER_ROW_NUMBER, A, "Производитель/Серия", style);
        addCell(sheet, HEADER_ROW_NUMBER, B, "Модель", style);
        addCell(sheet, HEADER_ROW_NUMBER, C, style);
        addCell(sheet, HEADER_ROW_NUMBER, D, "Производитель/модель", style);
        addCell(sheet, HEADER_ROW_NUMBER, E, style);
        addCell(sheet, HEADER_ROW_NUMBER, F, style);
        addCell(sheet, HEADER_ROW_NUMBER, G, "Производитель", style);
        addCell(sheet, HEADER_ROW_NUMBER, H, style);
        addCell(sheet, HEADER_ROW_NUMBER, I, style);
        addCell(sheet, HEADER_ROW_NUMBER, J, style);
        addCell(sheet, HEADER_ROW_NUMBER, K, style);
        addCell(sheet, HEADER_ROW_NUMBER, L, style);
        addCell(sheet, HEADER_ROW_NUMBER, M, style);
        addCell(sheet, HEADER_ROW_NUMBER, N, style);
        addCell(sheet, HEADER_ROW_NUMBER, O, "Краткое описание", style);
        addCell(sheet, HEADER_ROW_NUMBER, P, "Полное описание", style);
        addCell(sheet, HEADER_ROW_NUMBER, Q, "Картинка", style);
    }

    private void setColumnsWidth() {
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            setColumnWidth(sheet, A, true);
            setColumnWidth(sheet, B, true);
            setColumnWidth(sheet, C, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, D, true);
            setColumnWidth(sheet, E, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, F, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, G, true);
            setColumnWidth(sheet, H, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, I, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, J, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, K, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, L, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, M, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, N, false, EMPTY_COLUMN_WIDTH);
            setColumnWidth(sheet, O, false, SHORT_DESCRIPTION_COLUMN_WIDTH);
            setColumnWidth(sheet, P, false, DESCRIPTION_COLUMN_WIDTH);
            setColumnWidth(sheet, Q, true);
        }
    }

    private CellStyle getHeaderStyle() {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName(FONT_NAME);
        font.setFontHeightInPoints(FONT_SIZE);
        font.setBold(true);
        font.setItalic(true);
        cellStyle.setFont(font);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        cellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        return cellStyle;
    }

    private CellStyle getEvenRowStyle(boolean sideBorders) {
        CellStyle cellStyle = getStyle(sideBorders);
        cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        return cellStyle;
    }

    private CellStyle getOddRowStyle(boolean sideBorders) {
        CellStyle cellStyle = getStyle(sideBorders);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        return cellStyle;
    }

    private CellStyle getStyle(boolean sideBorders) {
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName(FONT_NAME);
        font.setFontHeightInPoints(FONT_SIZE);
        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        cellStyle.setWrapText(true);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        if (sideBorders) {
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        }
        return cellStyle;
    }
}
