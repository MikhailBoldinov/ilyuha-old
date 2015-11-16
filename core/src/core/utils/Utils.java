package core.utils;

/**
 * @author Mikhail Boldinov
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
}
