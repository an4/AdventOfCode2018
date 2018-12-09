import java.io.File;
import java.io.IOException;
import java.util.*;

/** Advent of Code Day 7. */
public class Day07 {

    private static final String INPUT_FILE_PATH =
            "C:\\Users\\dev\\IdeaProjects\\AdventOfCode\\input\\day7.txt";
    private static final int NUM_WORKERS = 5;

    public static void main(String[] args) {
        System.out.println(partOne(getSteps()));
        System.out.println(partTwo(getSteps()));
    }

    /**
     * --- Day 7: The Sum of Its Parts ---
     * You find yourself standing on a snow-covered coastline; apparently, you landed a little off
     * course. The region is too hilly to see the North Pole from here, but you do spot some Elves
     * that seem to be trying to unpack something that washed ashore. It's quite cold out, so you
     * decide to risk creating a paradox by asking them for directions.
     *
     * "Oh, are you the search party?" Somehow, you can understand whatever Elves from the year 1018
     * speak; you assume it's Ancient Nordic Elvish. Could the device on your wrist also be a
     * translator? "Those clothes don't look very warm; take this." They hand you a heavy coat.
     *
     * "We do need to find our way back to the North Pole, but we have higher priorities at the
     * moment. You see, believe it or not, this box contains something that will solve all of
     * Santa's transportation problems - at least, that's what it looks like from the pictures in
     * the instructions." It doesn't seem like they can read whatever language it's in, but you can:
     * "Sleigh kit. Some assembly required."
     *
     * "'Sleigh'? What a wonderful name! You must help us assemble this 'sleigh' at once!" They
     * start excitedly pulling more parts out of the box.
     *
     * The instructions specify a series of steps and requirements about which steps must be
     * finished before others can begin (your puzzle input). Each step is designated by a single
     * letter. For example, suppose you have the following instructions:
     *
     * Step C must be finished before step A can begin.
     * Step C must be finished before step F can begin.
     * Step A must be finished before step B can begin.
     * Step A must be finished before step D can begin.
     * Step B must be finished before step E can begin.
     * Step D must be finished before step E can begin.
     * Step F must be finished before step E can begin.
     * Visually, these requirements look like this:
     *
     *
     * -->A--->B--
     * /    \      \
     * C      -->D----->E
     * \           /
     * ---->F-----
     * Your first goal is to determine the order in which the steps should be completed. If more
     * than one step is ready, choose the step which is first alphabetically. In this example, the
     * steps would be completed as follows:
     *
     * Only C is available, and so it is done first.
     * Next, both A and F are available. A is first alphabetically, so it is done next.
     * Then, even though F was available earlier, steps B and D are now also available, and B is the
     * first alphabetically of the three.
     * After that, only D and F are available. E is not available because only some of its
     * prerequisites are complete. Therefore, D is completed next.
     * F is the only choice, so it is done next.
     * Finally, E is completed.
     * So, in this example, the correct order is CABDFE.
     *
     * In what order should the steps in your instructions be completed?
     */
    private static String partOne(HashMap<String, Step> map) {
        String sequence = "";
        ArrayList<Step> firstStep = new ArrayList<>();
        Step lastStep = null;
        for (Map.Entry entry : map.entrySet()) {
            if (((Step) entry.getValue()).previous.isEmpty()) {
                firstStep.add((Step) entry.getValue());
            }
            if (((Step) entry.getValue()).next.isEmpty()) {
                lastStep = (Step) entry.getValue();
                lastStep.state = State.DONE;
            }
        }
        if (firstStep == null || lastStep == null) {
            return "";
        }
        // DFS
        Stack<Step> stack = new Stack();
        for (Step step : firstStep) {
            stack.push(step);
        }
        Collections.sort(stack);
        while (!stack.isEmpty()) {
            Step currentStep = stack.pop();
            if (currentStep.state != State.DONE && allPreviousStepsCompleted(currentStep)) {
                sequence += currentStep.id;
                currentStep.state = State.DONE;
            } else {

            }
            for (int i = 0; i < currentStep.next.size(); i++) {
                Step step = currentStep.next.get(i);
                if (step.state != State.DONE) {
                    stack.push(step);
                }
            }
            Collections.sort(stack);
        }
        sequence += lastStep.id;
        System.out.println(sequence.length());
        return sequence;
    }

    /**
     * --- Part Two ---
     * As you're about to begin construction, four of the Elves offer to help. "The sun will set
     * soon; it'll go faster if we work together." Now, you need to account for multiple people
     * working on steps simultaneously. If multiple steps are available, workers should still begin
     * them in alphabetical order.
     *
     * Each step takes 60 seconds plus an amount corresponding to its letter: A=1, B=2, C=3, and so
     * on. So, step A takes 60+1=61 seconds, while step Z takes 60+26=86 seconds. No time is
     * required between steps.
     *
     * To simplify things for the example, however, suppose you only have help from one Elf (a total
     * of two workers) and that each step takes 60 fewer seconds (so that step A takes 1 second and
     * step Z takes 26 seconds). Then, using the same instructions as above, this is how each second
     * would be spent:
     *
     * Second   Worker 1   Worker 2   Done
     * 0        C          .
     * 1        C          .
     * 2        C          .
     * 3        A          F       C
     * 4        B          F       CA
     * 5        B          F       CA
     * 6        D          F       CAB
     * 7        D          F       CAB
     * 8        D          F       CAB
     * 9        D          .       CABF
     * 10        E          .       CABFD
     * 11        E          .       CABFD
     * 12        E          .       CABFD
     * 13        E          .       CABFD
     * 14        E          .       CABFD
     * 15        .          .       CABFDE
     * Each row represents one second of time. The Second column identifies how many seconds have
     * passed as of the beginning of that second. Each worker column shows the step that worker is
     * currently doing (or . if they are idle). The Done column shows completed steps.
     *
     * Note that the order of the steps has changed; this is because steps now take time to finish
     * and multiple workers can begin multiple steps simultaneously.
     *
     * In this example, it would take 15 seconds for two workers to complete these steps.
     *
     * With 5 workers and the 60+ second step durations described above, how long will it take to
     * complete all of the steps?
     */
    private static int partTwo(HashMap<String, Step> map) {
        int[] workersTime = new int[NUM_WORKERS];
        ArrayList<Step> firstSteps = new ArrayList<>();
        int currentTime = 0;
        HashMap<Integer, ArrayList<Step>> doneMap = new HashMap<>();
        int maxFutureTime = -1;

        Step lastStep = null;
        for (Map.Entry entry : map.entrySet()) {
            if (((Step) entry.getValue()).previous.isEmpty()) {
                firstSteps.add((Step) entry.getValue());
            }
            if (((Step) entry.getValue()).next.isEmpty()) {
                lastStep = (Step) entry.getValue();
            }
        }
        if (lastStep == null) {
            return 0;
        }

        Stack<Step> stack = new Stack();
        for (Step step : firstSteps) {
            step.state = State.READY;
            stack.push(step);
        }
        Collections.sort(stack);

        Step currentStep;
        while (!allPreviousStepsCompleted(lastStep)) {
            while (stack.isEmpty() && currentTime <= maxFutureTime) {
                currentTime++;
                ArrayList<Step> completedSteps =
                        doneMap.getOrDefault(currentTime, new ArrayList<>());
                for (Step step : completedSteps) {
                    step.state = State.DONE;
                    for (Step next : step.next) {
                        if (next.state == State.READY || next.state == State.WAITING_FOR_DEPS) {
                            stack.push(next);
                        }
                    }
                }
                doneMap.remove(currentTime);
            }
            Collections.sort(stack);
            if (stack.isEmpty()) {
                break;
            }

            currentStep = stack.pop();
            System.out.println("Current step " + currentStep.id);

            ArrayList<Step> completedSteps = doneMap.getOrDefault(currentTime, new ArrayList<>());
            for (Step step : completedSteps) {
                step.state = State.DONE;
                for (Step next : step.next) {
                    stack.push(next);
                }
            }
            Collections.sort(stack);

            if (currentStep.state == State.WAITING_FOR_DEPS &&
                    allPreviousStepsCompleted(currentStep)) {
                currentStep.state = State.READY;
            }

            if (currentStep.state == State.READY) {
                // Find free worker
                int availableWorker = -1;
                while (availableWorker == -1) {
                    for (int i = 0; i < NUM_WORKERS; i++) {
                        if (currentTime >= workersTime[i]) {
                            availableWorker = i;
                            break;
                        }
                    }
                    if (availableWorker == -1) {
                        currentTime++;
                    }
                }
                System.out.println(
                        "Task " + currentStep.id + " started by worker " + availableWorker +
                                " lasting " + currentStep.duration + " at current time " +
                                currentTime);
                currentStep.state = State.IN_PROGRESS;
                ArrayList<Step> stepsInProgress =
                        doneMap.getOrDefault(currentTime + currentStep.duration, new ArrayList<>());
                stepsInProgress.add(currentStep);
                doneMap.put(currentTime + currentStep.duration, stepsInProgress);
                if (maxFutureTime < currentTime + currentStep.duration) {
                    maxFutureTime = currentTime + currentStep.duration;
                }
                workersTime[availableWorker] = currentTime + currentStep.duration;
            }
        }
        // get max time;
        int maxTime = 0;
        for (int i = 0; i < NUM_WORKERS; i++) {
            if (maxTime < workersTime[i]) {
                maxTime = workersTime[i];
            }
        }
        return maxTime;
    }

    private static boolean allPreviousStepsCompleted(Step step) {
        for (Step prev : step.previous) {
            if (prev.state != State.DONE) {
                return false;
            }
        }
        return true;
    }

    private static HashMap<String, Step> getSteps() {
        HashMap<String, Step> map = new HashMap<>();
        File file = new File(INPUT_FILE_PATH);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String firstStepId = line.substring(5, 6);
                String secondStepId = line.substring(36, 37);

                Step firstStep = map.getOrDefault(firstStepId, new Step(firstStepId));
                Step secondStep = map.getOrDefault(secondStepId, new Step(secondStepId));
                firstStep.next.add(secondStep);
                secondStep.previous.add(firstStep);

                map.put(firstStepId, firstStep);
                map.put(secondStepId, secondStep);
            }
        } catch (IOException e) {
            System.out.println("File I/O error!");
        }
        return map;
    }

    private enum State {
        WAITING_FOR_DEPS, READY, IN_PROGRESS, DONE
    }

    private static class Step implements Comparable<Step> {
        String id;
        ArrayList<Step> previous = new ArrayList<>();
        ArrayList<Step> next = new ArrayList<>();
        State state = State.WAITING_FOR_DEPS;
        int duration;

        Step(String id) {
            this.id = id;
            this.duration = 60 + (id.charAt(0) - 'A' + 1);
        }

        @Override
        public int compareTo(Step o) {
            return o.id.compareTo(this.id);
        }
    }
}
