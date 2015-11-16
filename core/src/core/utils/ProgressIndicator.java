package core.utils;

/**
 * @author Mikhail Boldinov
 */
public class ProgressIndicator implements Runnable {

    private static final int PROGRESS_LENGTH = 10;
    private static final String PROGRESS_SYMBOL = ".";
    private static final String EMPTY;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PROGRESS_LENGTH; i++) {
            sb.append(" ");
        }
        EMPTY = sb.toString();

    }

    private boolean run = true;

    @Override
    public void run() {
        int i = 0;
        while (run) {
            System.out.print(PROGRESS_SYMBOL);
            if (i >= PROGRESS_LENGTH) {
                i = 0;
                System.out.print("\r");
                System.out.print(EMPTY);
                System.out.print("\r");
            }
            try {
                Thread.sleep(500l);
            } catch (InterruptedException ignore) {
            }
            i++;
        }
    }

    public void hide() {
        System.out.print("\r");
        System.out.print(EMPTY);
        System.out.print("\r");
        run = false;
    }
}
