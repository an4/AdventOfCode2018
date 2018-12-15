import java.math.BigInteger;
import java.util.HashMap;

public class Day12 {
    private static final long ITERATIONS_ONE = 20L;

    private static final long ITERATIONS_TWO = 50000000000L;

    private static final String INPUT =
            "#.#####.#.#.####.####.#.#...#.......##..##.#.#.#.###..#.....#.####..#.#######.#....####.#....##....#";

    private static final String[] RULES = new String[]{
            "##.## => .",
            "#.#.. => .",
            "..... => .",
            "##..# => #",
            "###.. => #",
            ".##.# => .",
            "..#.. => #",
            "##.#. => #",
            ".##.. => .",
            "#..#. => .",
            "###.# => #",
            ".#### => #",
            ".#.## => .",
            "#.##. => #",
            ".###. => #",
            "##### => .",
            "..##. => .",
            "#.#.# => .",
            "...#. => #",
            "..### => .",
            ".#.#. => #",
            ".#... => #",
            "##... => #",
            ".#..# => #",
            "#.### => #",
            "#..## => #",
            "....# => .",
            "####. => .",
            "#...# => #",
            "#.... => .",
            "...## => .",
            "..#.# => #"};

    private static long START_INDEX = 0;

    private static HashMap<String, String> buildRulesMap(String[] rules) {
        HashMap<String, String> rulesMap = new HashMap<>();
        for (int i = 0; i < rules.length; i++) {
            String[] split = rules[i].split(" => ");
            rulesMap.put(split[0], split[1]);
        }
        return rulesMap;
    }

    private static String getRuleToApply(String input, HashMap<String, String> rulesMap) {
        return rulesMap.getOrDefault(input, ".");
    }

    private static String getNextIteration(String input, HashMap<String, String> rulesMap) {
        StringBuilder output = new StringBuilder();
        input = trimEmptyPots(input);
        input = "...." + input + "....";
        START_INDEX -= 4;
        output.append("..");
        for (int i = 2; i < input.length() - 2; i++) {
            output.append(getRuleToApply(input.substring(i - 2, i + 3), rulesMap));
        }
        return trimEmptyPots(output.toString());
    }

    private static String trimEmptyPots(String input) {
        while (input.startsWith(".")) {
            input = input.substring(1);
            START_INDEX++;
        }
        while (input.endsWith(".")) {
            input = input.substring(0, input.length() - 1);
        }
        return input;
    }


    private static String getPotsWithFlowersCountAfterIThIteration(String potsLine,
                                                                   long iteration) {
        HashMap<String, String> rulesMap = buildRulesMap(RULES);

        HashMap<String, Long> cycle = new HashMap<>();

        for (long i = 0; i < iteration; i++) {
            potsLine = getNextIteration(potsLine, rulesMap);
            if (cycle.containsKey(potsLine)) {
                START_INDEX += ITERATIONS_TWO - i - 1;
                return potsLine;
            } else {
                cycle.put(potsLine, i);
            }
        }
        return potsLine;
    }

    private static void getSum(String input) {
        long offSet = START_INDEX;
        BigInteger sum = new BigInteger("0");
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '#') {
                BigInteger current = new BigInteger(String.valueOf(i))
                        .add(new BigInteger(String.valueOf(offSet)));
                sum = sum.add(current);
            }
        }
        System.out.println(sum.toString());
    }

    public static void main(String[] args) {
        System.out.println(getPotsWithFlowersCountAfterIThIteration(INPUT, ITERATIONS_ONE));

        getSum(getPotsWithFlowersCountAfterIThIteration(INPUT, ITERATIONS_TWO));
    }

}
