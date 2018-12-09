import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** Advent of Code Day 8. */
public class Day08 {

    private static final String INPUT_FILE_PATH =
            "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day8.txt";

    private static Integer[] readInput() {
        ArrayList<Integer> arr = new ArrayList<>();
        File file = new File(INPUT_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextInt()) {
                arr.add(sc.nextInt());
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return arr.toArray(new Integer[arr.size()]);
    }

    private static void partOne() {
        Integer[] arr = readInput();
        findEndNode(0, arr.length - 1, 1, arr);
        int metadataSum = 0;
        for (int i = 0; i < arr.length; i++) {
            metadataSum += arr[i];
        }
        System.out.println(metadataSum);
    }

    private static int findEndNode(int start, int end, int children, Integer[] arr) {
        if (start >= end || start >= arr.length || end >= arr.length || start == -1) {
            return -1;
        }
        if (arr[start] == 0 && children == 1) {
            int currentNodeEnd = start + arr[start + 1] + 2;
            arr[start] = 0;
            arr[start + 1] = 0;
            return currentNodeEnd;
        } else if (children == 1) {
            int startOfNextChild = start + 2;
            int endOfLastChild = end - arr[start + 1];
            int numberOfChildrenForCurrentNode = arr[start];
            arr[start] = 0;
            arr[start + 1] = 0;
            return findEndNode(startOfNextChild, endOfLastChild, numberOfChildrenForCurrentNode,
                    arr);
        } else if (arr[start] == 0) {
            int startOfNextChild = start + 2 + arr[start +1];
            arr[start] = 0;
            arr[start + 1] = 0;
            return findEndNode(startOfNextChild, end, children - 1, arr);
        } else {
            int startOfInnerNextChild = start + 2;
            int numberOfChildrenForCurrentNode = arr[start];
            arr[start] = 0;
            arr[start + 1] = 0;
            int endOfLastChild =
                    findEndNode(startOfInnerNextChild, end, numberOfChildrenForCurrentNode, arr);
            return findEndNode(endOfLastChild + 1, end, children - 1, arr);
        }
    }

    public static void main(String[] args) {
        partOne();
    }
}
