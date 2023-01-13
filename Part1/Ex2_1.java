package Part1;

import org.junit.jupiter.api.Test;
// File handling:
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// Random number generation:
import java.util.Random;
// Threads:
import java.util.concurrent.*;

public class Ex2_1 {
    /**
     * Creates n new text files with a random number of lines.
     * @param n number of files to be created.
     * @param seed  determines random number of lines in each file.
     * @param bound determines the upper bound of the number of lines in each file.
     * @return  names of created files.
     */
    public static String[] createTextFiles(int n, int seed, int bound) throws IOException {
        String[] file_names = new String[n];
        Random rand = new Random(seed);

        for (int i = 0; i < n; i++) {
            String file_name = "f-%d.txt".formatted(i);

            file_names[i] = file_name;
            FileWriter fw = new FileWriter(file_name);

            int number_of_lines = rand.nextInt(bound);
            for (int j = 0; j < number_of_lines; j++) {
                fw.write("Hello World!\n");
            }

            fw.close();
        }

        return file_names;
    }

    /**
     * Deletes the given files.
     * @param fileNames contains the names of the files to be deleted.
     */
    public static void deleteFiles(String[] fileNames) {
        for (String file_name: fileNames) {
            (new File(file_name)).delete();
        }
    }

    public static int getNumOfLines(String file_name) throws FileNotFoundException {
        int sum = 0;

        FileReader input_file = new FileReader(file_name);
        Scanner scanner = new Scanner(input_file);

        while(scanner.hasNextLine()) {
            scanner.nextLine();
            sum++;
        }
        scanner.close();

        return sum;
    }

    /**
     * Returns the sum of the number of lines in the given files.
     * @param fileNames array of files to be used.
     * @return  sum of the number of lines in all files.
     */
    public static int getNumOfLines(String[] fileNames) throws FileNotFoundException {
        int sum = 0;

        for (String file_name: fileNames) {
            sum += getNumOfLines(file_name);
        }

        return sum;
    }

    /**
     * Returns the sum of the number of lines in the given files utilizing multiple threads.
     * @param fileNames array of files to be used.
     * @return  sum of the number of lines in all files.
     */
    public static int getNumOfLinesThreads(String[] fileNames) throws InterruptedException {
        int sum = 0;

        int i = 0;
        for (String file_name: fileNames) {
            LineCounter_Threads thread = new LineCounter_Threads(file_name);
            thread.run();
            sum += thread.getNumberOfLines();
        }

        return sum;
    }

    /**
     * Returns the sum of the number of lines in the given files utilizing a thread pool.
     * @param fileNames array of files to be used.
     * @return  -1, in case of timeout.
     *          otherwise, sum of the number of lines in all files.
     */
    public static int getNumOfLinesThreadPool(String[] fileNames) throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(fileNames.length);
        List<Future> scheduled_futures = new ArrayList<Future>();

        for (String file_name: fileNames) {
            scheduled_futures.add( scheduledExecutorService.submit(new LineCounter_Callable(file_name)) );
        }

        scheduledExecutorService.shutdown();

        int sum = 0;

        for (Future scheduled_future: scheduled_futures) {
            try {
                sum += (int) scheduled_future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return sum;
    }

    @Test
    public static void simpleTest() throws IOException, InterruptedException, ExecutionException {
        String[] files = createTextFiles(10000, 345656745, 10000);

        long start = System.nanoTime();
        System.out.printf("[NORMAL]\t\treturned %d,\ttime %f\n", getNumOfLines(files), (System.nanoTime()-start)/1000000000.0);

        long start1 = System.nanoTime();
        System.out.printf("[THREADS]\t\treturned %d,\ttime %f\n", getNumOfLinesThreads(files), (System.nanoTime()-start1)/1000000000.0);

        long start2 = System.nanoTime();
        System.out.printf("[THREADPOOL]\treturned %d,\ttime %f\n", getNumOfLinesThreadPool(files), (System.nanoTime()-start2)/1000000000.0);

        Ex2_1.deleteFiles(files);
    }
}
