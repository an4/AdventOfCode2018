import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** Advent of Code Day 3. */
public class Day03 {

    private static final String INPUT_FILE_PATH =
            "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day3.txt";

    public static void main(String[] args) {
        ArrayList<String> data = getData();
        System.out.println(partOne(data));
        System.out.println(partTwo(data));
    }

    private static ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        File file = new File(INPUT_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                data.add(sc.nextLine());
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return data;
    }

    /**
     * --- Day 3: No Matter How You Slice It ---
     * The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit (thanks to
     * someone who helpfully wrote its box IDs on the wall of the warehouse in the middle of the
     * night). Unfortunately, anomalies are still affecting them - nobody can even agree on how to
     * cut the fabric.
     *
     * The whole piece of fabric they're working on is a very large square - at least 1000 inches on
     * each side.
     *
     * Each Elf has made a claim about which area of fabric would be ideal for Santa's suit. All
     * claims have an ID and consist of a single rectangle with edges parallel to the edges of the
     * fabric. Each claim's rectangle is defined as follows:
     *
     * The number of inches between the left edge of the fabric and the left edge of the rectangle.
     * The number of inches between the top edge of the fabric and the top edge of the rectangle.
     * The width of the rectangle in inches.
     * The height of the rectangle in inches.
     * A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from the
     * left edge, 2 inches from the top edge, 5 inches wide, and 4 inches tall. Visually, it claims
     * the square inches of fabric represented by # (and ignores the square inches of fabric
     * represented by .) in the diagram below:
     *
     * ...........
     * ...........
     * ...#####...
     * ...#####...
     * ...#####...
     * ...#####...
     * ...........
     * ...........
     * ...........
     * The problem is that many of the claims overlap, causing two or more claims to cover part of
     * the same areas. For example, consider the following claims:
     *
     * #1 @ 1,3: 4x4
     * #2 @ 3,1: 4x4
     * #3 @ 5,5: 2x2
     * Visually, these claim the following areas:
     *
     * ........
     * ...2222.
     * ...2222.
     * .11XX22.
     * .11XX22.
     * .111133.
     * .111133.
     * ........
     * The four square inches marked with X are claimed by both 1 and 2. (Claim 3, while adjacent to
     * the others, does not overlap either of them.)
     *
     * If the Elves all proceed with their own plans, none of them will have enough fabric. How many
     * square inches of fabric are within two or more claims?
     */
    private static int partOne(ArrayList<String> data) {
        int count = 0;
        int[][] matrix = new int[1000][1000];
        for (String claim : data) {
            String[] split = claim.split(" ");
            String[] coordinates = split[2].split(",");
            String[] sizes = split[3].split("x");
            for (int i = Integer.valueOf(coordinates[0]);
                 i < Integer.valueOf(coordinates[0]) + Integer.valueOf(sizes[0]); i++) {
                for (int j =
                     Integer.valueOf(coordinates[1].substring(0, coordinates[1].length() - 1));
                     j < Integer.valueOf(
                             coordinates[1].substring(0, coordinates[1].length() - 1)) +
                             Integer.valueOf(sizes[1]); j++) {
                    if (matrix[i][j] == 1) {
                        count++;
                    }
                    matrix[i][j]++;
                }
            }
        }
        return count;
    }

    /**
     * --- Part Two ---
     * Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single square
     * inch of fabric with any other claim. If you can somehow draw attention to it, maybe the Elves
     * will be able to make Santa's suit after all!
     *
     * For example, in the claims above, only claim 3 is intact after all claims are made.
     *
     * What is the ID of the only claim that doesn't overlap?
     */
    private static String partTwo(ArrayList<String> data) {
        int[][] matrix = buildMatrix(data);
        for (String claim : data) {
            String[] split = claim.split(" ");
            String[] coordinates = split[2].split(",");
            String[] sizes = split[3].split("x");
            boolean doesNotOverlap = true;
            int x = Integer.valueOf(coordinates[0]);
            int width = Integer.valueOf(sizes[0]);
            int y = Integer.valueOf(coordinates[1].substring(0, coordinates[1].length() - 1));
            int height = Integer.valueOf(sizes[1]);
            for (int i = x; i<x+width; i++) {
                for (int j = y; j<y+height; j++) {
                    if (matrix[i][j] > 1) {
                        doesNotOverlap = false;
                    }
                }
            }
            if (doesNotOverlap) {
                return split[0];
            }
        }
        return "";
    }

    private static int[][] buildMatrix(ArrayList<String> data) {
        int[][] matrix = new int[1000][1000];
        for (String claim : data) {
            String[] split = claim.split(" ");
            String[] coordinates = split[2].split(",");
            String[] sizes = split[3].split("x");
            int x = Integer.valueOf(coordinates[0]);
            int width = Integer.valueOf(sizes[0]);
            int y = Integer.valueOf(coordinates[1].substring(0, coordinates[1].length() - 1));
            int height = Integer.valueOf(sizes[1]);
            for (int i = x; i<x+width; i++) {
                for (int j = y; j<y+height; j++) {
                    matrix[i][j]++;
                }
            }
        }
        return matrix;
    }
}
