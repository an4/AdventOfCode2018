import java.util.AbstractMap;
import java.util.Map;

/**
 * --- Day 11: Chronal Charge ---
 * You watch the Elves and their sleigh fade into the distance as they head toward the North Pole.
 *
 * Actually, you're the one fading. The falling sensation returns.
 *
 * The low fuel warning light is illuminated on your wrist-mounted device. Tapping it once causes it
 * to project a hologram of the situation: a 300x300 grid of fuel cells and their current power
 * levels, some negative. You're not sure what negative power means in the context of time travel,
 * but it can't be good.
 *
 * Each fuel cell has a coordinate ranging from 1 to 300 in both the X (horizontal) and Y (vertical)
 * direction. In X,Y notation, the top-left cell is 1,1, and the top-right cell is 300,1.
 *
 * The interface lets you select any 3x3 square of fuel cells. To increase your chances of getting
 * to your destination, you decide to choose the 3x3 square with the largest total power.
 *
 * The power level in a given fuel cell can be found through the following process:
 *
 * Find the fuel cell's rack ID, which is its X coordinate plus 10.
 * Begin with a power level of the rack ID times the Y coordinate.
 * Increase the power level by the value of the grid serial number (your puzzle input).
 * Set the power level to itself multiplied by the rack ID.
 * Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds
 * digit become 0).
 * Subtract 5 from the power level.
 * For example, to find the power level of the fuel cell at 3,5 in a grid with serial number 8:
 *
 * The rack ID is 3 + 10 = 13.
 * The power level starts at 13 * 5 = 65.
 * Adding the serial number produces 65 + 8 = 73.
 * Multiplying by the rack ID produces 73 * 13 = 949.
 * The hundreds digit of 949 is 9.
 * Subtracting 5 produces 9 - 5 = 4.
 * So, the power level of this fuel cell is 4.
 *
 * Here are some more example power levels:
 *
 * Fuel cell at  122,79, grid serial number 57: power level -5.
 * Fuel cell at 217,196, grid serial number 39: power level  0.
 * Fuel cell at 101,153, grid serial number 71: power level  4.
 * Your goal is to find the 3x3 square which has the largest total power. The square must be
 * entirely within the 300x300 grid. Identify this square using the X,Y coordinate of its top-left
 * fuel cell. For example:
 *
 * For grid serial number 18, the largest total 3x3 square has a top-left corner of 33,45 (with a
 * total power of 29); these fuel cells appear in the middle of this 5x5 region:
 *
 * -2  -4   4   4   4
 * -4   4   4   4  -5
 * 4   3   3   4  -4
 * 1   1   2   4  -3
 * -1   0   2  -5  -2
 * For grid serial number 42, the largest 3x3 square's top-left is 21,61 (with a total power of 30);
 * they are in the middle of this region:
 *
 * -3   4   2   2   2
 * -4   4   3   3   4
 * -5   3   3   4  -4
 * 4   3   3   4  -3
 * 3   3   3  -5  -1
 * What is the X,Y coordinate of the top-left fuel cell of the 3x3 square with the largest total
 * power?
 *
 * --- Part Two ---
 * You discover a dial on the side of the device; it seems to let you select a square of any size,
 * not just 3x3. Sizes from 1x1 to 300x300 are supported.
 *
 * Realizing this, you now must find the square of any size with the largest total power. Identify
 * this square by including its size as a third parameter after the top-left coordinate: a 9x9
 * square with a top-left corner of 3,5 is identified as 3,5,9.
 *
 * For example:
 *
 * For grid serial number 18, the largest total square (with a total power of 113) is 16x16 and has
 * a top-left corner of 90,269, so its identifier is 90,269,16.
 * For grid serial number 42, the largest total square (with a total power of 119) is 12x12 and has
 * a top-left corner of 232,251, so its identifier is 232,251,12.
 * What is the X,Y,size identifier of the square with the largest total power?
 */
public class Day11 {

    private static final int SIZE = 300;
    private static final int GRID_SERIAL_NUMBER = 5791;

    private static int getPowerLevel(int x, int y) {
        int rackId = x + 10;
        int powerLevel = rackId * y;
        powerLevel += GRID_SERIAL_NUMBER;
        powerLevel *= rackId;
        powerLevel %= 1000;
        powerLevel /= 100;
        powerLevel -= 5;
        return powerLevel;
    }

    private static int[][] computeGridPowerLevel() {
        int[][] grid = new int[SIZE][SIZE];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = getPowerLevel(i + 1, j + 1);
            }
        }
        return grid;
    }

    private static Map.Entry<Integer, String> getCellWithLargestTotalPowerForCellSize(int[][] grid,
                                                                                      int cellSize) {
        int max = Integer.MIN_VALUE;
        int maxX = 0;
        int maxY = 0;
        for (int i = 0; i < grid.length - cellSize; i++) {
            for (int j = 0; j < grid[0].length - cellSize; j++) {
                int currentCellSum = 0;
                for (int k = i; k < i + cellSize; k++) {
                    for (int l = j; l < j + cellSize; l++) {
                        currentCellSum += grid[k][l];
                    }
                }
                if (currentCellSum > max) {
                    max = currentCellSum;
                    maxX = i + 1;
                    maxY = j + 1;
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(max, maxX + "," + maxY + "," + cellSize);
    }

    private static void getCellWithLargestTotalPower() {
        int[][] grid = computeGridPowerLevel();
        int max = Integer.MIN_VALUE;
        String output = "";
        for (int i = 1; i < SIZE; i++) {
            Map.Entry<Integer, String> entry = getCellWithLargestTotalPowerForCellSize(grid, i);
            if (max < entry.getKey()) {
                max = entry.getKey();
                output = entry.getValue();
            }
        }
        System.out.println(output);
    }

    private static void getCellWithLargestTotalPower_DP() {
        int[][] grid = computeGridPowerLevel();
        int max = grid[0][0];
        int maxX = 1;
        int maxY = 1;
        int maxSize = 1;
        for (int size = 1; size < SIZE; size++) {
            for (int i=0; i<SIZE-size; i++) {
                for (int j=0; j<SIZE-size; j++) {
                    int lastRowAndColumnSum = 0;
                    for (int t=j; t<j+size-1;t++) {
                        lastRowAndColumnSum += grid[i+size-1][t];
                    }
                    for (int t=i; t<i+size-2;t++) {
                        lastRowAndColumnSum += grid[t][j+size-1];
                    }
                    grid[i][j] += lastRowAndColumnSum;
                    if (max < grid[i][j]) {
                        max = grid[i][j];
                        maxX = i+1;
                        maxY = j+1;
                        maxSize = size;
                    }
                }
            }
        }
        System.out.println(maxX+","+maxY+","+maxSize);
    }


    public static void main(String[] args) {
        long startTime = System.nanoTime();
        getCellWithLargestTotalPower();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000000);

        startTime = System.nanoTime();
        getCellWithLargestTotalPower_DP();
        endTime   = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println(totalTime/1000000);
    }

}
