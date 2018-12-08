import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** Advent of Code DAy 4. */
public class Day04 {
    private static final String INPUT_FILE_PATH =
            "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day4.txt";
    private static final int DAY_IN_MINUTES = 60 * 24;


    public static void main(String[] args) {
        ArrayList<Record> data = getData();
        Collections.sort(data);
        HashMap<Integer, Guard> guardMap = getAllGuards(data);
        System.out.println(partOne(guardMap));
        System.out.println(partTwo(guardMap));
    }

    /**
     * --- Day 4: Repose Record ---
     * You've sneaked into another supply closet - this time, it's across from the prototype suit
     * manufacturing lab. You need to sneak inside and fix the issues with the suit, but there's a
     * guard stationed outside the lab, so this is as close as you can safely get.
     *
     * As you search the closet for anything that might help, you discover that you're not the first
     * person to want to sneak in. Covering the walls, someone has spent an hour starting every
     * midnight for the past few months secretly observing this guard post! They've been writing
     * down the ID of the one guard on duty that night - the Elves seem to have decided that one
     * guard was enough for the overnight shift - as well as when they fall asleep or wake up while
     * at their post (your puzzle input).
     *
     * For example, consider the following records, which have already been organized into
     * chronological order:
     *
     * [1518-11-01 00:00] Guard #10 begins shift
     * [1518-11-01 00:05] falls asleep
     * [1518-11-01 00:25] wakes up
     * [1518-11-01 00:30] falls asleep
     * [1518-11-01 00:55] wakes up
     * [1518-11-01 23:58] Guard #99 begins shift
     * [1518-11-02 00:40] falls asleep
     * [1518-11-02 00:50] wakes up
     * [1518-11-03 00:05] Guard #10 begins shift
     * [1518-11-03 00:24] falls asleep
     * [1518-11-03 00:29] wakes up
     * [1518-11-04 00:02] Guard #99 begins shift
     * [1518-11-04 00:36] falls asleep
     * [1518-11-04 00:46] wakes up
     * [1518-11-05 00:03] Guard #99 begins shift
     * [1518-11-05 00:45] falls asleep
     * [1518-11-05 00:55] wakes up
     * Timestamps are written using year-month-day hour:minute format. The guard falling asleep or
     * waking up is always the one whose shift most recently started. Because all asleep/awake times
     * are during the midnight hour (00:00 - 00:59), only the minute portion (00 - 59) is relevant
     * for those events.
     *
     * Visually, these records show that the guards are asleep at these times:
     *
     * Date   ID   Minute
     * 000000000011111111112222222222333333333344444444445555555555
     * 012345678901234567890123456789012345678901234567890123456789
     * 11-01  #10  .....####################.....#########################.....
     * 11-02  #99  ........................................##########..........
     * 11-03  #10  ........................#####...............................
     * 11-04  #99  ....................................##########..............
     * 11-05  #99  .............................................##########.....
     * The columns are Date, which shows the month-day portion of the relevant day; ID, which shows
     * the guard on duty that day; and Minute, which shows the minutes during which the guard was
     * asleep within the midnight hour. (The Minute column's header shows the minute's ten's digit
     * in the first row and the one's digit in the second row.) Awake is shown as ., and asleep is
     * shown as #.
     *
     * Note that guards count as asleep on the minute they fall asleep, and they count as awake on
     * the minute they wake up. For example, because Guard #10 wakes up at 00:25 on 1518-11-01,
     * minute 25 is marked as awake.
     *
     * If you can figure out the guard most likely to be asleep at a specific time, you might be
     * able to trick that guard into working tonight so you can have the best chance of sneaking in.
     * You have two strategies for choosing the best guard/minute combination.
     *
     * Strategy 1: Find the guard that has the most minutes asleep. What minute does that guard
     * spend asleep the most?
     *
     * In the example above, Guard #10 spent the most minutes asleep, a total of 50 minutes
     * (20+25+5), while Guard #99 only slept for a total of 30 minutes (10+10+10). Guard #10 was
     * asleep most during minute 24 (on two days, whereas any other minute the guard was asleep was
     * only seen on one day).
     *
     * While this example listed the entries in chronological order, your entries are in the order
     * you found them. You'll need to organize them before they can be analyzed.
     *
     * What is the ID of the guard you chose multiplied by the minute you chose? (In the above
     * example, the answer would be 10 * 24 = 240.)
     */
    private static int partOne(HashMap<Integer, Guard> guardMap) {
        if (guardMap.isEmpty()) {
            return -1;
        }

        long maxTimeSlept = -1;
        int guardWithMaxTimeSleptId = 0;

        // Find the guard with the most slept time.
        for (Map.Entry entry : guardMap.entrySet()) {
            Guard currentGuard = ((Guard) entry.getValue());
            if (currentGuard.totalTimeSlept > maxTimeSlept) {
                maxTimeSlept = currentGuard.totalTimeSlept;
                guardWithMaxTimeSleptId = currentGuard.id;
            }
        }

        // For the guard with the most slept time find the minute they slept more often.
        Guard guardWithMostSleptTime = guardMap.get(guardWithMaxTimeSleptId);
        int maxMinute = -1;
        int maxMinuteOccurrence = guardWithMostSleptTime.minutesArray[0];
        for (int i = 1; i < DAY_IN_MINUTES; i++) {
            if (maxMinuteOccurrence < guardWithMostSleptTime.minutesArray[i]) {
                maxMinuteOccurrence = guardWithMostSleptTime.minutesArray[i];
                maxMinute = i;
            }
        }

        return guardWithMaxTimeSleptId * maxMinute;
    }

    /**
     * --- Part Two ---
     * Strategy 2: Of all guards, which guard is most frequently asleep on the same minute?
     *
     * In the example above, Guard #99 spent minute 45 asleep more than any other guard or minute -
     * three times in total. (In all other cases, any guard spent any minute asleep at most twice.)
     *
     * What is the ID of the guard you chose multiplied by the minute you chose? (In the above
     * example, the answer would be 99 * 45 = 4455.)
     */
    private static int partTwo(HashMap<Integer, Guard> guardMap) {
        int guardMostAsleepDuringTheSameMinuteId = -1;
        int maxOccurrencesSleptOnTheSameMinutes = -1;
        int minuteSpentTheMostAsleep = -1;
        for(Map.Entry entry : guardMap.entrySet()) {
            Guard guard = ((Guard) entry.getValue());
            if (guard.minutesArray[guard.getMostSleptMinute()] > maxOccurrencesSleptOnTheSameMinutes) {
                maxOccurrencesSleptOnTheSameMinutes = guard.minutesArray[guard.getMostSleptMinute()];
                minuteSpentTheMostAsleep = guard.getMostSleptMinute();
                guardMostAsleepDuringTheSameMinuteId = guard.id;
            }
        }
        return guardMostAsleepDuringTheSameMinuteId * minuteSpentTheMostAsleep;
    }


    private static class Record implements Comparable<Record> {
        long timestampMins;
        long minuteInADay;
        String action;

        Record(String timestamp, String action) {
            this.timestampMins = convertTimestampToMinutes(timestamp);
            this.minuteInADay = convertTimestampToMinuteInADay(timestamp);
            this.action = action;
        }

        private long convertTimestampToMinutes(String timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            try {
                Date date = sdf.parse(timestamp.substring(5));
                return date.getTime() / 60000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private long convertTimestampToMinuteInADay(String timestamp) {
            String[] times = timestamp.substring(11).split(":");
            return Integer.valueOf(times[0]) * 60 + Integer.valueOf(times[1]);
        }

        @Override
        public int compareTo(Record o) {
            if (this.timestampMins < o.timestampMins) {
                return -1;
            } else if (this.timestampMins > o.timestampMins) {
                return 1;
            }
            return 0;
        }
    }

    private static int getGuardId(String action) {
        String[] split = action.split(" ");
        return Integer.valueOf(split[1].substring(1));
    }

    private static ArrayList<Record> getData() {
        ArrayList<Record> data = new ArrayList<>();
        File file = new File(INPUT_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] split = sc.nextLine().split("] ");
                data.add(new Record(split[0].substring(1), split[1]));
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return data;
    }

    private static class Guard {
        int id;
        long totalTimeSlept;
        int[] minutesArray;

        Guard(int id) {
            this.id = id;
            this.totalTimeSlept = 0;
            this.minutesArray = new int[DAY_IN_MINUTES];
        }

        int getMostSleptMinute() {
            int maxOccurrences = -1;
            int maxMinute = -1;
            for (int i=0; i<minutesArray.length; i++) {
                if (maxOccurrences < minutesArray[i]) {
                    maxOccurrences = minutesArray[i];
                    maxMinute = i;
                }
            }
            return maxMinute;
        }
    }

    private static HashMap<Integer, Guard> getAllGuards(ArrayList<Record> data) {
        HashMap<Integer, Guard> map = new HashMap<>();
        Guard currentGuard = new Guard(0);
        long sleepStartTime = 0;
        long sleepStartMinute = 0;
        int currentId = 0;
        for (Record record : data) {
            if (record.action.contains("Guard")) {
                currentId = getGuardId(record.action);
                currentGuard = map.getOrDefault(currentId, new Guard(currentId));
            } else if (record.action.contains("wakes")) {
                currentGuard.totalTimeSlept += record.timestampMins - sleepStartTime;
                if (sleepStartMinute > record.minuteInADay) {
                    for (int i = (int) sleepStartMinute; i < DAY_IN_MINUTES; i++) {
                        currentGuard.minutesArray[i]++;
                    }
                    for (int i = 0; i < record.minuteInADay; i++) {
                        currentGuard.minutesArray[i]++;
                    }
                } else {
                    for (int i = (int) sleepStartMinute; i < record.minuteInADay; i++) {
                        currentGuard.minutesArray[i]++;
                    }
                }
            } else if (record.action.contains("falls")) {
                sleepStartTime = record.timestampMins;
                sleepStartMinute = record.minuteInADay;
            }
            map.put(currentId, currentGuard);
        }
        return map;
    }
}
