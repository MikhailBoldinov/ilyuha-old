package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://regex101.com/
 *
 * @author Mikhail Boldinov
 */
public class Main {
    private static final String TEST_STR =
            "{Концевой выключатель|Выключатель|Концевик} %code% {производства фирмы|компании|корпорации} Omron {предназначен|служит} для защитных {механизмов|систем}. %code% - это {высокотехнологичное|технологичное} решение Вашх проблем в {сфере|области} автоматизации. Для {размещения заказа|того, чтобы разместить заказ} свяжитесь с нами любым удобным для Вас способом.";

    private static final String[] CODES = {
            "D4N-4C62",
            "D4N-412G",
            "D4N-4120"
    };

    public static void main(String[] args) {
        String regex = "(\\{[^\\{]*\\})";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(TEST_STR);

        MatchResult matchResult = new MatchResult();
        while (matcher.find()) {
            matchResult.addSynonymList(matcher.group(0));
        }

        for (String code : CODES) {
            matcher = pattern.matcher(TEST_STR.replaceAll("%code%", code));

            int idx = 0;
            StringBuffer sb = new StringBuffer();

            while (matcher.find()) {
                matcher = matcher.appendReplacement(sb, matchResult.getSynonymList(idx++).getRandomSynonym());
            }
            matcher.appendTail(sb);
            System.out.println(sb.toString());
        }
    }
}

class MatchResult {
    private List<SynonymList> synonymLists = new ArrayList<>();

    public void addSynonymList(String synonyms) {
        synonymLists.add(new SynonymList(synonyms));
    }

    public SynonymList getSynonymList(int index) {
        return synonymLists.get(index);
    }
}

class SynonymList {
    private static final String START = "{";
    private static final String END = "}";
    private static final String SPLITTER = "\\|";

    private List<String> synonyms = new ArrayList<>();
    private int size;
    private Random random = new Random();

    public SynonymList(String synonyms) {
        buildSynonymList(synonyms);
    }

    private void buildSynonymList(String synonyms) {
        if (synonyms == null || synonyms.isEmpty()) {
            throw new IllegalArgumentException("Synonyms string is empty.");
        }
        if (!synonyms.startsWith(START) && !synonyms.endsWith(END)) {
            throw new IllegalArgumentException(String.format("Invalid synonyms string format: '%s'.", synonyms));
        }
        synonyms = synonyms.substring(1, synonyms.length() - 1);

        this.synonyms.addAll(Arrays.asList(synonyms.split(SPLITTER)));
        size = this.synonyms.size();
    }

    public String getRandomSynonym() {
        int idx = random.nextInt(size);
        return synonyms.get(idx);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String synonym : synonyms) {
            sb.append(synonym);
            sb.append(";");
        }
        return sb.toString();
    }
}
