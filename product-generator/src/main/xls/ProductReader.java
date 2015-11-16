package main.xls;

import core.io.IReader;
import core.io.impl.AbstractReader;
import core.xls.Config;
import main.beans.Group;
import main.beans.Product;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.IOException;

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
 * @author Mikhail Boldinov
 */
public class ProductReader extends AbstractReader implements IReader<Product> {

    public ProductReader(String fileName, Config config) throws IOException {
        super(fileName, config);
    }

    @Override
    public Product read() throws IOException {
        HSSFSheet sheet = getSheet(0);
        int rows = getNumberOfRows(sheet);
        Product product = new Product();
        product.setDescription(getStringValue(sheet, 0, DESCRIPTION));
        product.setProducer(getStringValue(sheet, 0, PRODUCER));
        product.setSerialNumber(getStringValue(sheet, 0, SERIAL_NUMBER));
        product.setShortDescription(getStringValue(sheet, 0, SHORT_DESCRIPTION));
        product.setImageExtension(getStringValue(sheet, 0, IMAGE_EXTENSION));

        for (int currentRow = config.getRow(FIRST_GROUP_NUM); currentRow < rows; currentRow++) {
            Group group = new Group();

            int rowNum = getGroupRowNum(sheet, currentRow, NUM);
            if (rowNum < 0) {
                continue;
            }

            group.setName(getStringValue(sheet, rowNum, GROUP));
            group.setSeparator(getStringValue(sheet, rowNum, SEPARATOR));
            while (++rowNum < rows && !isGroupStart(sheet, rowNum, NUM)) {
                group.addCode(getStringValue(sheet, rowNum, CODE), getStringValue(sheet, rowNum, CODE_DESCRIPTION));
            }
            product.addGroup(group);
        }
        return product;
    }
}
