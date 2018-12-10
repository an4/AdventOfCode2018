import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * --- Day 10: The Stars Align ---
 * It's no use; your navigation system simply isn't capable of providing walking directions in the
 * arctic circle, and certainly not in 1018.
 *
 * The Elves suggest an alternative. In times like these, North Pole rescue operations will arrange
 * points of light in the sky to guide missing Elves back to base. Unfortunately, the message is
 * easy to miss: the points move slowly enough that it takes hours to align them, but have so much
 * momentum that they only stay aligned for a second. If you blink at the wrong time, it might be
 * hours before another message appears.
 *
 * You can see these points of light floating in the distance, and record their position in the sky
 * and their velocity, the relative change in position per second (your puzzle input). The
 * coordinates are all given from your perspective; given enough time, those positions and
 * velocities will move the points into a cohesive message!
 *
 * Rather than wait, you decide to fast-forward the process and calculate what the points will
 * eventually spell.
 *
 * For example, suppose you note the following points:
 *
 * position=< 9,  1> velocity=< 0,  2>
 * position=< 7,  0> velocity=<-1,  0>
 * position=< 3, -2> velocity=<-1,  1>
 * position=< 6, 10> velocity=<-2, -1>
 * position=< 2, -4> velocity=< 2,  2>
 * position=<-6, 10> velocity=< 2, -2>
 * position=< 1,  8> velocity=< 1, -1>
 * position=< 1,  7> velocity=< 1,  0>
 * position=<-3, 11> velocity=< 1, -2>
 * position=< 7,  6> velocity=<-1, -1>
 * position=<-2,  3> velocity=< 1,  0>
 * position=<-4,  3> velocity=< 2,  0>
 * position=<10, -3> velocity=<-1,  1>
 * position=< 5, 11> velocity=< 1, -2>
 * position=< 4,  7> velocity=< 0, -1>
 * position=< 8, -2> velocity=< 0,  1>
 * position=<15,  0> velocity=<-2,  0>
 * position=< 1,  6> velocity=< 1,  0>
 * position=< 8,  9> velocity=< 0, -1>
 * position=< 3,  3> velocity=<-1,  1>
 * position=< 0,  5> velocity=< 0, -1>
 * position=<-2,  2> velocity=< 2,  0>
 * position=< 5, -2> velocity=< 1,  2>
 * position=< 1,  4> velocity=< 2,  1>
 * position=<-2,  7> velocity=< 2, -2>
 * position=< 3,  6> velocity=<-1, -1>
 * position=< 5,  0> velocity=< 1,  0>
 * position=<-6,  0> velocity=< 2,  0>
 * position=< 5,  9> velocity=< 1, -2>
 * position=<14,  7> velocity=<-2,  0>
 * position=<-3,  6> velocity=< 2, -1>
 * Each line represents one point. Positions are given as <X, Y> pairs: X represents how far left
 * (negative) or right (positive) the point appears, while Y represents how far up (negative) or
 * down (positive) the point appears.
 *
 * At 0 seconds, each point has the position given. Each second, each point's velocity is added to
 * its position. So, a point with velocity <1, -2> is moving to the right, but is moving upward
 * twice as quickly. If this point's initial position were <3, 9>, after 3 seconds, its position
 * would become <6, 3>.
 *
 * Over time, the points listed above would move like this:
 *
 * Initially:
 * ........#.............
 * ................#.....
 * .........#.#..#.......
 * ......................
 * #..........#.#.......#
 * ...............#......
 * ....#.................
 * ..#.#....#............
 * .......#..............
 * ......#...............
 * ...#...#.#...#........
 * ....#..#..#.........#.
 * .......#..............
 * ...........#..#.......
 * #...........#.........
 * ...#.......#..........
 *
 * After 1 second:
 * ......................
 * ......................
 * ..........#....#......
 * ........#.....#.......
 * ..#.........#......#..
 * ......................
 * ......#...............
 * ....##.........#......
 * ......#.#.............
 * .....##.##..#.........
 * ........#.#...........
 * ........#...#.....#...
 * ..#...........#.......
 * ....#.....#.#.........
 * ......................
 * ......................
 *
 * After 2 seconds:
 * ......................
 * ......................
 * ......................
 * ..............#.......
 * ....#..#...####..#....
 * ......................
 * ........#....#........
 * ......#.#.............
 * .......#...#..........
 * .......#..#..#.#......
 * ....#....#.#..........
 * .....#...#...##.#.....
 * ........#.............
 * ......................
 * ......................
 * ......................
 *
 * After 3 seconds:
 * ......................
 * ......................
 * ......................
 * ......................
 * ......#...#..###......
 * ......#...#...#.......
 * ......#...#...#.......
 * ......#####...#.......
 * ......#...#...#.......
 * ......#...#...#.......
 * ......#...#...#.......
 * ......#...#..###......
 * ......................
 * ......................
 * ......................
 * ......................
 *
 * After 4 seconds:
 * ......................
 * ......................
 * ......................
 * ............#.........
 * ........##...#.#......
 * ......#.....#..#......
 * .....#..##.##.#.......
 * .......##.#....#......
 * ...........#....#.....
 * ..............#.......
 * ....#......#...#......
 * .....#.....##.........
 * ...............#......
 * ...............#......
 * ......................
 * ......................
 * After 3 seconds, the message appeared briefly: HI. Of course, your message will be much longer
 * and will take many more seconds to appear.
 *
 * What message will eventually appear in the sky?
 *
 * --- Part Two ---
 * Good thing you didn't have to wait, because that would have taken a long time - much longer than
 * the 3 seconds in the example above.
 *
 * Impressed by your sub-hour communication capabilities, the Elves are curious: exactly how many
 * seconds would they have needed to wait for that message to appear?
 */
public class Day10 {

    private static final String INPUT_FILE_PATH =
            "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day10.txt";

    private static int min_x = Integer.MAX_VALUE;
    private static int min_y = Integer.MAX_VALUE;
    private static int max_x = Integer.MIN_VALUE;
    private static int max_y = Integer.MIN_VALUE;
    private static int x_range = Integer.MAX_VALUE;
    private static int y_range = Integer.MAX_VALUE;

    private static class Point {
        private int x;
        private int y;
        private int x_velocity;
        private int y_velocity;

        Point(int x, int y, int x_velocity, int y_velocity) {
            this.x = x;
            this.y = y;
            this.x_velocity = x_velocity;
            this.y_velocity = y_velocity;
        }

        void updatePoint() {
            this.x += x_velocity;
            this.y += y_velocity;
        }

        void undoUpdate() {
            this.x -= x_velocity;
            this.y -= y_velocity;
        }
    }

    private static ArrayList<Point> readData() {
        ArrayList<Point> points = new ArrayList<>();

        File file = new File(INPUT_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                String pointData = sc.nextLine();
                int y = Integer.valueOf(pointData.substring(10, 16).trim());
                int x = Integer.valueOf(pointData.substring(18, 24).trim());
                int y_velocity = Integer.valueOf(pointData.substring(36, 38).trim());
                int x_velocity = Integer.valueOf(pointData.substring(40, 42).trim());
                points.add(new Point(x, y, x_velocity, y_velocity));

                if (min_x > x) min_x = x;
                if (max_x < x) max_x = x;
                if (min_y > y) min_y = y;
                if (max_y < y) max_y = y;
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        x_range = Math.abs(max_x - min_x);
        y_range = Math.abs(max_y - min_y);
        return points;
    }

    private static void run(ArrayList<Point> points) {
        int prevRangeX;
        int prevRangeY;
        int step = 0;
        do {
            prevRangeX = x_range;
            prevRangeY = y_range;
            min_x = 0;
            min_y = 0;
            max_x = 0;
            max_y = 0;
            for (Point point : points) {
                point.updatePoint();
                if (min_x > point.x) min_x = point.x;
                if (max_x < point.x) max_x = point.x;
                if (min_y > point.y) min_y = point.y;
                if (max_y < point.y) max_y = point.y;
            }
            x_range = Math.abs(max_x - min_x);
            y_range = Math.abs(max_y - min_y);
            step++;
        } while (x_range < prevRangeX && y_range < prevRangeY);

        min_x = Integer.MAX_VALUE;
        min_y = Integer.MAX_VALUE;
        max_x = Integer.MIN_VALUE;
        max_y = Integer.MIN_VALUE;
        for (Point point : points) {
            point.undoUpdate();
            if (min_x > point.x) min_x = point.x;
            if (max_x < point.x) max_x = point.x;
            if (min_y > point.y) min_y = point.y;
            if (max_y < point.y) max_y = point.y;
        }
        step--;
        System.out.println(step);
    }


    private static void print(ArrayList<Point> points) {
        HashSet<String> pointSet = new HashSet<>();

        for (Point point : points) {
            pointSet.add(point.x + "#" + point.y);
        }

        for (int i = min_x; i <= max_x; i++) {
            for (int j = min_y; j <= max_y; j++) {
                if (j == max_y) {
                    String s = i + "#" + j;
                    if (pointSet.contains(s)) {
                        System.out.println("#");
                    } else {
                        System.out.println(".");
                    }
                } else {
                    String s = i + "#" + j;
                    if (pointSet.contains(s)) {
                        System.out.print("#");
                    } else {
                        System.out.print(".");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Point> points = readData();
        run(points);
        print(points);
    }

}
