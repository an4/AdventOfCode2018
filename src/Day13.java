import java.io.File;
import java.io.IOException;
import java.util.*;

public class Day13 {
    private static final String INPUT_FILE_PATH =
            "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day13.txt";

    private enum Turn {UNKNOWN, LEFT, STRAIGHT, RIGHT}

    private static class Cart {
        private int x;
        private int y;
        private char currentDirection;
        private char pathUnderTheCart;
        private Turn lastTurn = Turn.UNKNOWN;
        private boolean isRemoved = false;

        Cart(int x, int y, char currentDirection, char pathUnderTheCart) {
            this.x = x;
            this.y = y;
            this.currentDirection = currentDirection;
            this.pathUnderTheCart = pathUnderTheCart;
        }
    }

    private static ArrayList<String> readFile() {
        ArrayList<String> list = new ArrayList<>();
        File file = new File(INPUT_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                list.add(sc.nextLine());
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return list;
    }

    private static char[][] buildMap(ArrayList<String> list) {
        char[][] map = new char[list.size()][list.get(0).length()];
        for (int i = 0; i < list.size(); i++) {
            String line = list.get(i);
            for (int j = 0; j < list.get(i).length(); j++) {
                map[i][j] = line.charAt(j);
            }
        }
        return map;
    }

    private static boolean isCart(char symbol) {
        switch (symbol) {
            case '<':
            case '>':
            case '^':
            case 'v':
                return true;
            default:
                return false;
        }
    }

    private static boolean hasHorizontalPath(char c) {
        return c == '-' || c == '+' || c == '\\' || c == '/';
    }

    private static boolean hasVerticalPath(char c) {
        return c == '|' || c == '+' || c == '\\' || c == '/';
    }

    private static char findPathUnderTheCart(char[][] map, int x, int y) {
        // Intersection '+'
        if (x > 0 && x < map.length - 1 && y > 1 && y < map[0].length - 1 &&
                hasHorizontalPath(map[x][y - 1]) &&
                hasHorizontalPath(map[x][y + 1]) &&
                hasVerticalPath(map[x - 1][y]) &&
                hasVerticalPath(map[x + 1][y])) {
            return '+';
        }
        if (x > 0 && x < map.length - 1 && hasVerticalPath(map[x - 1][y]) &&
                hasVerticalPath(map[x + 1][y])) {
            return '|';
        }
        if (y > 0 && y < map[0].length - 1 && hasHorizontalPath(map[x][y - 1]) &&
                hasHorizontalPath(map[x][y + 1])) {
            return '-';
        }
        if (x > 0 && y < map[0].length - 1 && (map[x - 1][y] == '|' || map[x - 1][y] == '+') &&
                (map[x][y + 1] == '-' || map[x][y + 1] == '+')) {
            return '\\';
        }
        if (x > 0 && y > 0 && (map[x - 1][y] == '|' || map[x - 1][y] == '+') &&
                (map[x][y - 1] == '-' || map[x][y - 1] == '+')) {
            return '/';
        }
        if (y > 0 && x < map.length - 1 && (map[x][y - 1] == '-' || map[x][y - 1] == '+') &&
                (map[x + 1][y] == '|' || map[x + 1][y] == '+')) {
            return '\\';
        }
        if (x < map.length - 1 && y < map[0].length - 1 &&
                (map[x][y + 1] == '-' || map[x][y + 1] == '+') &&
                (map[x + 1][y] == '|' || map[x + 1][y] == '+')) {
            return '/';
        }
        System.err.println("Couldn't find the path under the cart." + x + " " + y);
        return '.';
    }

    private static TreeMap<Integer, TreeMap<Integer, Cart>> findCarts(char[][] map) {
        TreeMap<Integer, TreeMap<Integer, Cart>> cartsMap = new TreeMap<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (isCart(map[i][j])) {
                    Cart cart = new Cart(i, j, map[i][j], findPathUnderTheCart(map, i, j));
                    TreeMap<Integer, Cart> rowMap = cartsMap.getOrDefault(i, new TreeMap<>());
                    rowMap.put(j, cart);
                    cartsMap.put(i, rowMap);
                }
            }
        }
        return cartsMap;
    }

    private static Turn findNextTurnAtIntersection(Cart cart) {
        switch (cart.lastTurn) {
            case UNKNOWN:
                return Turn.LEFT;
            case LEFT:
                return Turn.STRAIGHT;
            case STRAIGHT:
                return Turn.RIGHT;
            case RIGHT:
                return Turn.LEFT;
            default:
                return Turn.UNKNOWN;
        }
    }

    private static char getCartsNewDirectionAtIntersection(Cart cart, Turn newDirection) {
        switch (cart.currentDirection) {
            case '>':
                switch (newDirection) {
                    case LEFT:
                        return '^';
                    case RIGHT:
                        return 'v';
                    default:
                        return cart.currentDirection;
                }
            case 'v':
                switch (newDirection) {
                    case LEFT:
                        return '>';
                    case RIGHT:
                        return '<';
                    default:
                        return cart.currentDirection;
                }
            case '<':
                switch (newDirection) {
                    case LEFT:
                        return 'v';
                    case RIGHT:
                        return '^';
                    default:
                        return cart.currentDirection;
                }
            case '^':
                switch (newDirection) {
                    case LEFT:
                        return '<';
                    case RIGHT:
                        return '>';
                    default:
                        return cart.currentDirection;
                }
            default:
                return cart.currentDirection;
        }
    }

    private static char getCartsNewDirectionAtTurn(Cart cart, char turn) {
        if (turn == '/') {
            switch (cart.currentDirection) {
                case '>':
                    return '^';
                case '^':
                    return '>';
                case '<':
                    return 'v';
                case 'v':
                    return '<';
            }
        }
        // '\'
        switch (cart.currentDirection) {
            case '>':
                return 'v';
            case '^':
                return '<';
            case '<':
                return '^';
            case 'v':
                return '>';
            default:
                return cart.currentDirection;
        }
    }

    private static Map.Entry<Integer, Integer> getCartsNewCoordinates(Cart cart) {
        switch (cart.currentDirection) {
            case '>':
                return new AbstractMap.SimpleEntry(cart.x, cart.y + 1);
            case '<':
                return new AbstractMap.SimpleEntry<>(cart.x, cart.y - 1);
            case '^':
                return new AbstractMap.SimpleEntry<>(cart.x - 1, cart.y);
            case 'v':
                return new AbstractMap.SimpleEntry<>(cart.x + 1, cart.y);
            default:
                System.err.println("Failed to get carts new Coordinates");
                return new AbstractMap.SimpleEntry<>(cart.x, cart.y);
        }
    }

    private static void findFirstCollision(char[][] map,
                                           TreeMap<Integer, TreeMap<Integer, Cart>> carts) {
        TreeMap<Integer, TreeMap<Integer, Cart>> newCarts = new TreeMap<>();

        while (true) {
            System.out.println("Tick start");
            newCarts = new TreeMap<>();
            for (TreeMap<Integer, Cart> row : carts.values()) {
                for (Cart cart : row.values()) {
                    Map.Entry<Integer, Integer> futureCoordinates = getCartsNewCoordinates(cart);
                    int newX = futureCoordinates.getKey();
                    int newY = futureCoordinates.getValue();
                    char newCartOrientation = cart.currentDirection;
                    Turn nextTurn = cart.lastTurn;
                    char futureRoadSegment = map[newX][newY];

                    // Check if there is another cart already there.
                    if (isCart(map[newX][newY])) {
                        System.out.println("Collision found at X:" + newY + " Y:" + newX);
                        return;
                    }

                    switch (futureRoadSegment) {
                        case '+':
                            nextTurn = findNextTurnAtIntersection(cart);
                            newCartOrientation = getCartsNewDirectionAtIntersection(cart, nextTurn);
                            break;
                        case '-':
                        case '|':
                            break;
                        case '/':
                        case '\\':
                            newCartOrientation =
                                    getCartsNewDirectionAtTurn(cart, futureRoadSegment);
                            break;

                    }

                    // Update map with new cart position.
                    map[cart.x][cart.y] = cart.pathUnderTheCart;
                    map[newX][newY] = newCartOrientation;

                    // Update cart.
                    cart.x = newX;
                    cart.y = newY;
                    cart.lastTurn = nextTurn;
                    cart.pathUnderTheCart = futureRoadSegment;
                    cart.currentDirection = newCartOrientation;

                    // Add to new tree map;
                    TreeMap<Integer, Cart> rowMap =
                            newCarts.getOrDefault(newX, new TreeMap<>());
                    rowMap.put(newY, cart);
                    newCarts.put(newX, rowMap);
                }
            }
            carts = newCarts;
            System.out.println("Tick end");
        }
    }

    private static int getCartsCount(TreeMap<Integer, TreeMap<Integer, Cart>> carts) {
        int count = 0;
        for (TreeMap<Integer, Cart> row : carts.values()) {
            count += row.size();
        }
        return count;
    }

    private static void findLastRemainingCartsPosition(char[][] map,
                                                       TreeMap<Integer, TreeMap<Integer, Cart>> carts) {
        TreeMap<Integer, TreeMap<Integer, Cart>> newCarts;
        int cartsCount = getCartsCount(carts);

        while (cartsCount > 1) {
//            System.out.println("Tick start");
            newCarts = new TreeMap<>();
            for (TreeMap<Integer, Cart> row : carts.values()) {
                for (Cart cart : row.values()) {
                    if(cart.isRemoved) {
                        continue;
                    }
                    boolean hasCollided = false;
                    Map.Entry<Integer, Integer> futureCoordinates = getCartsNewCoordinates(cart);
                    int newX = futureCoordinates.getKey();
                    int newY = futureCoordinates.getValue();
                    char newCartOrientation = cart.currentDirection;
                    Turn nextTurn = cart.lastTurn;
                    char futureRoadSegment = map[newX][newY];

                    // Check if there is another cart already there.
                    if (isCart(map[newX][newY])) {
                        hasCollided = true;
                        System.out.println("Carts collided!");

                        // Remove previous cart from the map.
                        if (newCarts.get(newX) != null && newCarts.get(newX).get(newY) != null) {
                            Cart cartToRemove = newCarts.get(newX).get(newY);
                            newCarts.get(newX).remove(newY);
                            if (newCarts.get(newX).isEmpty()) {
                                newCarts.remove(newX);
                            }
                            // Update map.
                            map[cartToRemove.x][cartToRemove.y] = cartToRemove.pathUnderTheCart;
                            cartsCount--;
                        } else if (carts.get(newX) != null && carts.get(newX).get(newY) != null) {
                            Cart cartToRemove = carts.get(newX).get(newY);
                            cartToRemove.isRemoved = true;
                            // Update map.
                            map[cartToRemove.x][cartToRemove.y] = cartToRemove.pathUnderTheCart;
                            cartsCount--;
                        } else {
                            System.out.println("Failed to remove cart.");
                        }
                    }

                    if (!hasCollided) {
                        switch (futureRoadSegment) {
                            case '+':
                                nextTurn = findNextTurnAtIntersection(cart);
                                newCartOrientation =
                                        getCartsNewDirectionAtIntersection(cart, nextTurn);
                                break;
                            case '-':
                            case '|':
                                break;
                            case '/':
                            case '\\':
                                newCartOrientation =
                                        getCartsNewDirectionAtTurn(cart, futureRoadSegment);
                                break;

                        }

                        // Update map with new cart position.
                        map[cart.x][cart.y] = cart.pathUnderTheCart;
                        map[newX][newY] = newCartOrientation;

                        // Update cart.
                        cart.x = newX;
                        cart.y = newY;
                        cart.lastTurn = nextTurn;
                        cart.pathUnderTheCart = futureRoadSegment;
                        cart.currentDirection = newCartOrientation;

                        // Add to new tree map;
                        TreeMap<Integer, Cart> rowMap =
                                newCarts.getOrDefault(newX, new TreeMap<>());
                        rowMap.put(newY, cart);
                        newCarts.put(newX, rowMap);
                    } else {
                        map[cart.x][cart.y] = cart.pathUnderTheCart;
                        cartsCount--;
                    }
                }
            }
            carts = newCarts;
//            System.out.println("Tick end");
        }

        for (TreeMap<Integer, Cart> row : carts.values()) {
            for (Cart cart : row.values()) {
                System.out.println("Last cart position X:" + cart.y + ", Y:" + cart.x);
            }
        }
    }

    public static void main(String[] args) {
        char[][] map = buildMap(readFile());
        TreeMap<Integer, TreeMap<Integer, Cart>> cartsCollection = findCarts(map);

        //        findFirstCollision(map, cartsCollection);
        findLastRemainingCartsPosition(map, cartsCollection);
    }
}
