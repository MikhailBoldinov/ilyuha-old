package main.utils;

import main.beans.Group;
import main.beans.Product;

/**
 * @author Mikhail Boldinov, 20.09.15
 */
public final class Utils {

    private static final String PROGRESS_BAR_SEPARATOR = "|";
    private static final String PROGRESS_BAR_SYMBOL = "=";

    private Utils() {
    }

    public static String buildString(String... str) {
        StringBuilder builder = new StringBuilder();
        for (String s : str) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static void showProgress(int iteration, int iterationCount) {
        int pct = iteration >= iterationCount - 1 ? 100 : 100 * iteration / iterationCount;
        StringBuilder progress = new StringBuilder(PROGRESS_BAR_SEPARATOR);
        for (int i = 1; i <= pct; i = i + 2) {
            progress.append(PROGRESS_BAR_SYMBOL);
        }
        while (progress.length() <= 50) {
            progress.append(" ");
        }
        progress.append(PROGRESS_BAR_SEPARATOR);
        progress.append(" ");
        progress.append(pct);
        progress.append("%\r");
        System.out.print(progress);
    }

    public static int getCodesCount(Product product) {
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
