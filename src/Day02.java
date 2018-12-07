import java.io.File;
import java.io.IOException;
import java.util.*;

/** Advent of Code Day 2. */
public class Day02 {

    private static final String INPUT_FILE_PATH = "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day2.txt";

    private enum Count {
        TWO, THREE, BOTH, NONE
    }

    public static void main(String[] args) {
        System.out.println(partOne());
        System.out.println(partTwo());
    }

    /**
     * --- Day 2: Inventory Management System ---
     * You stop falling through time, catch your breath, and check the screen on the device.
     * "Destination reached. Current Year: 1518. Current Location: North Pole Utility Closet 83N10."
     * You made it! Now, to find those anomalies.
     * <p>
     * Outside the utility closet, you hear footsteps and a voice. "...I'm not sure either. But now
     * that so many people have chimneys, maybe he could sneak in that way?" Another voice responds,
     * "Actually, we've been working on a new kind of suit that would let him fit through tight
     * spaces like that. But, I heard that a few days ago, they lost the prototype fabric, the
     * design plans, everything! Nobody on the team can even seem to remember important details of
     * the project!"
     * <p>
     * "Wouldn't they have had enough fabric to fill several boxes in the warehouse? They'd be
     * stored together, so the box IDs should be similar. Too bad it would take forever to search
     * the warehouse for two similar box IDs..." They walk too far away to hear any more.
     * <p>
     * Late at night, you sneak to the warehouse - who knows what kinds of paradoxes you could cause
     * if you were discovered - and use your fancy wrist device to quickly scan every box and
     * produce a list of the likely candidates (your puzzle input).
     * <p>
     * To make sure you didn't miss any, you scan the likely candidate boxes again, counting the
     * number that have an ID containing exactly two of any letter and then separately counting
     * those with exactly three of any letter. You can multiply those two counts together to get a
     * rudimentary checksum and compare it to what your device predicts.
     * <p>
     * For example, if you see the following box IDs:
     * <p>
     * abcdef contains no letters that appear exactly two or three times.
     * bababc contains two a and three b, so it counts for both.
     * abbcde contains two b, but no letter appears exactly three times.
     * abcccd contains three c, but no letter appears exactly two times.
     * aabcdd contains two a and two d, but it only counts once.
     * abcdee contains two e. ababab contains three a and three b, but it only counts once.
     * Of these box IDs, four of them contain a letter which appears exactly twice, and three of
     * them contain a letter which appears exactly three times.
     * Multiplying these together produces a checksum of 4 * 3 = 12.
     * <p>
     * What is the checksum for your list of box IDs?
     */
    private static int partOne() {
        File file = new File(INPUT_FILE_PATH);
        int doubles = 0;
        int triples = 0;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                switch (processWord(sc.nextLine())) {
                    case BOTH:
                        doubles++;
                        triples++;
                        break;
                    case TWO:
                        doubles++;
                        break;
                    case THREE:
                        triples++;
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return triples * doubles;
    }

    private static Count processWord(String word) {
        HashMap<Character, Integer> map = new HashMap<>();
        boolean foundTwo = false;
        boolean foundThree = false;
        for (int i = 0; i < word.length(); i++) {
            if (map.containsKey(word.charAt(i))) {
                map.put(word.charAt(i), map.get(word.charAt(i)) + 1);
            } else {
                map.put(word.charAt(i), 1);
            }
        }
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue().equals(2)) {
                foundTwo = true;
            }
            if (entry.getValue().equals(3)) {
                foundThree = true;
            }
        }
        if (foundTwo && foundThree) {
            return Count.BOTH;
        } else if (foundTwo) {
            return Count.TWO;
        } else if (foundThree) {
            return Count.THREE;
        }
        return Count.NONE;
    }

    /**
     * Confident that your list of box IDs is complete, you're ready to find the boxes full of
     * prototype fabric.
     *
     * The boxes will have IDs which differ by exactly one character at the same position in both
     * strings. For example, given the following box IDs:
     *
     * abcde
     * fghij
     * klmno
     * pqrst
     * fguij
     * axcye
     * wvxyz
     * The IDs abcde and axcye are close, but they differ by two characters (the second and fourth).
     * However, the IDs fghij and fguij differ by exactly one character, the third (h and u). Those
     * must be the correct boxes.
     *
     * What letters are common between the two correct box IDs? (In the example above, this is found
     * by removing the differing character from either ID, producing fgij.)
     */
    private static String partTwo() {
        File file = new File(INPUT_FILE_PATH);
        HashSet<String> set = new HashSet<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String word = sc.nextLine();
                for (String substring : getSubstringsFromWord(word)) {
                    if (set.contains(substring)) {
                        return substring;
                    }
                    set.add(substring);
                }
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return "";
    }

    private static HashSet<String> getSubstringsFromWord(String word) {
        HashSet<String> set = new HashSet<>();
        for (int i=0; i<word.length(); i++) {
            set.add(word.substring(0, i) + word.substring(i+1));
        }
        return set;
    }
}
