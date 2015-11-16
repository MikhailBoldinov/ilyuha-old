package main;

import core.utils.ProgressIndicator;
import core.xls.Config;
import core.xls.ConfigItem;
import main.beans.Group;
import main.beans.Product;
import main.beans.ProductResult;
import main.service.ProductProcessor;
import main.xls.ProductReader;
import main.xls.ProductWriter;

import java.io.IOException;

import static core.xls.Column.A;
import static core.xls.Column.B;
import static core.xls.Column.C;
import static core.xls.Column.D;
import static core.xls.Column.E;
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
public class ProductGenerator {

    public static final String SLASH = "/";
    public static final String DASH = "-";
    public static final String DOT = ".";
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\r\n";

    private static final int CODES_COUNT_TO_WARN = 1000;

    private static final Config CONFIG = new Config();

    static {
        CONFIG.add(DESCRIPTION, new ConfigItem(0, B));
        CONFIG.add(PRODUCER, new ConfigItem(1, B));
        CONFIG.add(SERIAL_NUMBER, new ConfigItem(2, B));
        CONFIG.add(SHORT_DESCRIPTION, new ConfigItem(3, B));
        CONFIG.add(IMAGE_EXTENSION, new ConfigItem(4, B));
        CONFIG.add(FIRST_GROUP_NUM, new ConfigItem(7, A));
        CONFIG.add(NUM, new ConfigItem(0, A));
        CONFIG.add(GROUP, new ConfigItem(0, B));
        CONFIG.add(SEPARATOR, new ConfigItem(0, C));
        CONFIG.add(CODE, new ConfigItem(0, D));
        CONFIG.add(CODE_DESCRIPTION, new ConfigItem(0, E));
    }

    public static void main(String[] args) {
        try {
            String inFileName = args[0];
            String outFileName = args[1];

            System.out.println();
            System.out.println(String.format("Чтение входящего файла '%s' . . .", inFileName));

            ProductReader productReader = new ProductReader(inFileName, CONFIG);
            Product product = productReader.read();
            System.out.println("OK");

            int codesCount = getCodesCount(product);
            if (codesCount > CODES_COUNT_TO_WARN) {
                System.out.println(String.format("Количество возможных кодов - %d. Обработка может занять длительное время.", codesCount));
            }

            System.out.println("Обработка данных . . .");
            ProductProcessor productProcessor = new ProductProcessor(product);
            ProductResult productResult = productProcessor.process();
            System.out.println("OK");

            System.out.println(String.format("Сохранение результата в '%s' . . .", outFileName));
            ProgressIndicator progress = new ProgressIndicator();
            Thread progressThread = new Thread(progress);
            progressThread.start();
            ProductWriter productWriter = new ProductWriter(outFileName);
            productWriter.write(productResult);
            progress.hide();
            System.out.println("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getCodesCount(Product product) {
        int count = 1;
        for (Group group : product.getGroups()) {
            int codesCount = group.getCodes().size();
            if (codesCount != 0) {
                count *= codesCount;
            }
        }
        return count;
    }
}
