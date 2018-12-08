import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/** Advent of Code Day 5. */
public class Day05 {
    private static final String INPUT_FILE_PATH =
            "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day5.txt";

    /**
     * --- Day 5: Alchemical Reduction ---
     * You've managed to sneak in to the prototype suit manufacturing lab. The Elves are making
     * decent progress, but are still struggling with the suit's size reduction capabilities.
     *
     * While the very latest in 1518 alchemical technology might have solved their problem
     * eventually, you can do better. You scan the chemical composition of the suit's material and
     * discover that it is formed by extremely long polymers (one of which is available as your
     * puzzle input).
     *
     * The polymer is formed by smaller units which, when triggered, react with each other such that
     * two adjacent units of the same type and opposite polarity are destroyed. Units' types are
     * represented by letters; units' polarity is represented by capitalization. For instance, r and
     * R are units with the same type but opposite polarity, whereas r and s are entirely different
     * types and do not react.
     *
     * For example:
     *
     * In aA, a and A react, leaving nothing behind.
     * In abBA, bB destroys itself, leaving aA. As above, this then destroys itself, leaving
     * nothing.
     * In abAB, no two adjacent units are of the same type, and so nothing happens.
     * In aabAAB, even though aa and AA are of the same type, their polarities match, and so nothing
     * happens.
     * Now, consider a larger example, dabAcCaCBAcCcaDA:
     *
     * dabAcCaCBAcCcaDA  The first 'cC' is removed.
     * dabAaCBAcCcaDA    This creates 'Aa', which is removed.
     * dabCBAcCcaDA      Either 'cC' or 'Cc' are removed (the result is the same).
     * dabCBAcaDA        No further actions can be taken.
     * After all possible reactions, the resulting polymer contains 10 units.
     *
     * How many units remain after fully reacting the polymer you scanned? (Note: in this puzzle and
     * others, the input is large; if you copy/paste your input, make sure you get the whole
     * thing.)
     */
    private static int partOne() {
        Stack<Character> stack = new Stack<>();
        try {
            FileInputStream fis = new FileInputStream(INPUT_FILE_PATH);
            char current;
            while (fis.available() > 0) {
                current = (char) fis.read();
                if (stack.isEmpty()) {
                    stack.push(current);
                } else if (Math.abs(current - stack.peek()) == Math.abs('A' - 'a')) {
                    stack.pop();
                    continue;
                }
                stack.push(current);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stack.size();
    }

    /**
     * --- Part Two ---
     * Time to improve the polymer.
     *
     * One of the unit types is causing problems; it's preventing the polymer from collapsing as
     * much as it should. Your goal is to figure out which unit type is causing the most problems,
     * remove all instances of it (regardless of polarity), fully react the remaining polymer, and
     * measure its length.
     *
     * For example, again using the polymer dabAcCaCBAcCcaDA from above:
     *
     * Removing all A/a units produces dbcCCBcCcD. Fully reacting this polymer produces dbCBcD,
     * which has length 6.
     * Removing all B/b units produces daAcCaCAcCcaDA. Fully reacting this polymer produces
     * daCAcaDA, which has length 8.
     * Removing all C/c units produces dabAaBAaDA. Fully reacting this polymer produces daDA, which
     * has length 4.
     * Removing all D/d units produces abAcCaCBAcCcaA. Fully reacting this polymer produces abCBAc,
     * which has length 6.
     * In this example, removing all C/c units was best, producing the answer 4.
     *
     * What is the length of the shortest polymer you can produce by removing all units of exactly
     * one type and fully reacting the result?
     */
    private static int partTwo() {
        String polymer = getPolymer();
        int min = Integer.MAX_VALUE;
        for (char c = 'a'; c <= 'z'; c++) {
            String temp = polymer.replaceAll("" + c, "");
            temp = temp.replace("" + (Character.toUpperCase(c)), "");
            if (reactPolymer(temp) < min) {
                min = reactPolymer(temp);
            }
        }
        return min;
    }

    private static int reactPolymer(String polymer) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < polymer.length(); i++) {
            char current = polymer.charAt(i);
            if (stack.isEmpty()) {
                stack.push(current);
                continue;
            }
            if (Math.abs(current - stack.peek()) == Math.abs('A' - 'a')) {
                stack.pop();
                continue;
            }
            stack.push(current);
        }
        return stack.size();
    }

    private static String getPolymer() {
        File file = new File(INPUT_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) {
                return sc.nextLine();
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(partOne());
        System.out.println(partTwo());
    }

}
