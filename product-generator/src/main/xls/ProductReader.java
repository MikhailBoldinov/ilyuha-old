package main.xls;

import main.beans.Group;
import main.beans.Product;
import main.utils.Utils;
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

import static main.xls.Field.CODE;
import static main.xls.Field.CODE_DESCRIPTION;
import static main.xls.Field.DESCRIPTION;
import static main.xls.Field.FIRST_GROUP_NUM;
import static main.xls.Field.GROUP;
import static main.xls.Field.IMAGE_EXTENSION;
import static main.xls.Field.NUM;
import static main.xls.Field.PRODUCER;
import static main.xls.Field.SEPARATOR;
import static main.xls.Field.SERIAL_NUMBER;
import static main.xls.Field.SHORT_DESCRIPTION;


/**
 * @author Mikhail Boldinov, 17.09.15
 */
public class ProductReader {
    private static final HSSFDataFormatter FORMATTER = new HSSFDataFormatter(new Locale("ru", "RU"));

    private HSSFSheet sheet;
    private String fileName;
    private Config config;
    private Product product;

    public ProductReader(String fileName, Config config) throws IOException {
        this.fileName = fileName;
        this.config = config;
    }

    public Product getProduct() {
        return product;
    }

    public void read() throws IOException {
        File file = new File(fileName);
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        sheet = wb.getSheetAt(0);

        int rows = sheet.getPhysicalNumberOfRows();
        product = new Product();
        product.setDescription(getStringValue(0, DESCRIPTION));
        product.setProducer(getStringValue(0, PRODUCER));
        product.setSerialNumber(getStringValue(0, SERIAL_NUMBER));
        product.setShortDescription(getStringValue(0, SHORT_DESCRIPTION));
        product.setImageExtension(getStringValue(0, IMAGE_EXTENSION));

        for (int currentRow = config.getRow(FIRST_GROUP_NUM); currentRow < rows; currentRow++) {
            Group group = new Group();

            int rowNum = getGroupRowNum(currentRow, sheet);
            if (rowNum < 0) {
                continue;
            }

            group.setName(getStringValue(rowNum, GROUP));
            group.setSeparator(getStringValue(rowNum, SEPARATOR));
            while (++rowNum < rows && !isGroupStart(rowNum, sheet)) {
                group.addCode(getStringValue(rowNum, CODE), getStringValue(rowNum, CODE_DESCRIPTION));
            }
            product.addGroup(group);
        }
    }

    private boolean isGroupStart(int rowNum, HSSFSheet sheet) {
        HSSFRow row = sheet.getRow(rowNum);
        String value = getStringValue(row.getCell(config.getColumn(NUM)));
        return value != null && !value.isEmpty();
    }

    private int getGroupRowNum(int rowNum, HSSFSheet sheet) {
        HSSFRow row = sheet.getRow(rowNum);
        String value = getStringValue(row.getCell(config.getColumn(NUM)));
        if (value == null || value.isEmpty()) {
            return -1;
        }
        return row.getRowNum();
    }

    private String getStringValue(int rowNum, Field field) {
        HSSFCell cell = getCell(rowNum, field);
        return getStringValue(cell);
    }

    private String getStringValue(HSSFCell cell) {
        if (cell != null) {
            try {
                return cell.getStringCellValue();
            } catch (IllegalStateException e) {
                return FORMATTER.formatCellValue(cell);
            }
        }
        return null;
    }

    private Integer getNumericValue(int rowNum, Field field) {
        HSSFCell cell = getCell(rowNum, field);
        if (cell != null) {
            String value = FORMATTER.formatCellValue(cell).replaceAll("\n", "");
            return Integer.parseInt(value);
        }
        return null;
    }

    private Date getDateValue(int rowNum, Field field) {
        HSSFCell cell = getCell(rowNum, field);
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

    private HSSFCell getCell(int rowNum, Field field) {
        HSSFRow row = sheet.getRow(rowNum + config.getRow(field));
        if (row == null) {
            debug(String.format("No row found. Row: %s.", rowNum + config.getRow(field)));
            return null;
        }
        return row.getCell(config.getColumn(field));
    }

    private void debug(Object msg) {
        System.out.println(msg);
    }

    private void debug(Object msg, int rowNum) {
        System.out.println(String.format("%s: %s", rowNum, msg));
    }
}
