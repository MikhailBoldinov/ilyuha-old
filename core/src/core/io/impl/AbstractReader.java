package core.io.impl;

import core.xls.Config;
import core.xls.IField;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Mikhail Boldinov
 */
public abstract class AbstractReader {
    private static final HSSFDataFormatter FORMATTER = new HSSFDataFormatter(new Locale("ru", "RU"));

    protected Config config;
    protected String fileName;

    private HSSFWorkbook wb;

    protected AbstractReader(String fileName, Config config) throws IOException {
        this.fileName = fileName;
        this.config = config;
        initialize();
    }

    private void initialize() throws IOException {
        File file = new File(fileName);
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
        wb = new HSSFWorkbook(fs);
    }

    protected HSSFSheet getSheet(int num) {
        return wb.getSheetAt(0);
    }

    protected boolean isGroupStart(HSSFSheet sheet, int rowNum, IField markerFiled) {
        HSSFRow row = sheet.getRow(rowNum);
        String value = getStringValue(row.getCell(config.getColumn(markerFiled)));
        return value != null && !value.isEmpty();
    }

    protected int getGroupRowNum(HSSFSheet sheet, int rowNum, IField markerFiled) {
        HSSFRow row = sheet.getRow(rowNum);
        String value = getStringValue(row.getCell(config.getColumn(markerFiled)));
        if (value == null || value.isEmpty()) {
            return -1;
        }
        return row.getRowNum();
    }

    protected String getStringValue(HSSFSheet sheet, int rowNum, IField field) {
        HSSFCell cell = getCell(sheet, rowNum, field);
        return getStringValue(cell);
    }

    protected String getStringValue(HSSFCell cell) {
        if (cell != null) {
            try {
                return cell.getStringCellValue();
            } catch (IllegalStateException e) {
                return FORMATTER.formatCellValue(cell);
            }
        }
        return null;
    }

    protected Integer getNumericValue(HSSFSheet sheet, int rowNum, IField field) {
        HSSFCell cell = getCell(sheet, rowNum, field);
        if (cell != null) {
            String value = FORMATTER.formatCellValue(cell).replaceAll("\n", "");
            return Integer.parseInt(value);
        }
        return null;
    }

    protected Date getDateValue(HSSFSheet sheet, int rowNum, IField field) {
        HSSFCell cell = getCell(sheet, rowNum, field);
        if (cell != null) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                try {
                    return simpleDateFormat.parse(FORMATTER.formatCellValue(cell));
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return null;
    }

    protected HSSFCell getCell(HSSFSheet sheet, int rowNum, IField field) {
        HSSFRow row = sheet.getRow(rowNum + config.getRow(field));
        if (row == null) {
            debug(String.format("No row found. Row: %s.", rowNum + config.getRow(field)));
            return null;
        }
        return row.getCell(config.getColumn(field));
    }

    protected int getNumberOfRows(HSSFSheet sheet) {
        return sheet.getPhysicalNumberOfRows();
    }

    private void debug(Object msg) {
        System.out.println(msg);
    }

    private void debug(Object msg, int rowNum) {
        System.out.println(String.format("%s: %s", rowNum, msg));
    }
}
