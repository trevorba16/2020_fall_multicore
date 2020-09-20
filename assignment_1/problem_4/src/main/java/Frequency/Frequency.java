package Frequency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Frequency {
    /**
     * Given an array and a number of threads, slices the array into a piece for each thread
     * by calculating the minimum and maximum indexes for each slice.
     * Creates a list of integer Futures, then creates a thread for each slice,
     * each of which will return the frequency of x in its slice.
     * Sums up all future integers, returns total.
     *
     * @param x the value which we will count and return its frequency
     * @param A the Array which contains random values
     * @param numThreads the number of threads to use to divide the array
     * @return The frequency of the value x in the array A
     */
    public static int parallelFreq(int x, int[] A, int numThreads)
    {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            // return variable to sum up thread returns
            int count = 0;

            int itemCount = A.length / numThreads;

            // List to store return values of each thread
            List<Future<Integer>> countList = new ArrayList<Future<Integer>>();

            // Determine minimum and maximum indexes and create a thread for each slice
            for (int i = 0; i < numThreads; i++) {
                int min = itemCount * i;

                // In the thread we will never actually access this index, but will access max - 1
                int max = (itemCount * (i + 1));

                countList.add(threadPool.submit(new FrequencyThread(x, min, max, A)));
            }

            // Sum up the results of each thread
            for (int i = 0; i < numThreads; i++)
            {
                count += countList.get(i).get();
            }
            return count;
        }
        catch (Exception e)
        {
            System.err.println(e);
            return -1;
        }
    }
}

/**
 *  Helper class for a Frequency.Frequency Calculation. Defines the properties for each thread
 */
class FrequencyThread implements Callable<Integer>
{
    int x;
    int min;
    int max;
    int[] A;
    int count;

    /**
     * Thread class constructor. Initializes variables, including count
     *
     *
     * @param x Value to search for
     * @param min Minimum index of array slice
     * @param max Maximum index of array slice
     * @param A Array to search
     */
    public FrequencyThread(int x, int min, int max, int[] A)
    {
        this.x = x;
        this.min = min;
        this.max = max;
        this.A = A;
        count = 0;
    }

    public Integer call()
    {
        try
        {
            // Iterate through slice, if value found, increment count
            for (int i = min; i < max; i++)
            {
                if (A[i] == x)
                    count++;
            }
            return count;
        }
        catch (Exception e)
        {
            System.err.println(e);
            return -1;
        }
    }

    /**
     * Initializes large array, chooses a random number of threads,
     * a random value to search for, and then calls parallelFreq
     *
     * Times the parallel operation, then does same calculation on a
     * single thread and displays the comparative times
     * @param args
     */
    public static void main(String[] args)
    {
        Random random = new Random();

        // Larger arrays required to see a difference in the time between multi and single thread on my hardware
        int arrayCount = 500000000;
        int[] A = new int[arrayCount];

        // bounding the possible values to 500 ensures a decent density of values
        for (int i = 0; i < arrayCount; i++)
        {
            A[i] = Math.abs(random.nextInt(50000));
        }

        int x = random.nextInt(50000);

        // Ensures number of threads greater than 2, less than 9
        int numThreads = Math.abs(random.nextInt(9) + 2);

        System.out.println("Array Size is: " + arrayCount);
        System.out.println("Random value is: " + x);
        System.out.println("Number of threads is: " + numThreads);

        System.out.println("Counting (Multi thread)...");

        long threadStartTime = System.currentTimeMillis();

        int threadCount = Frequency.parallelFreq(x, A, numThreads);

        long threadEndTime = System.currentTimeMillis();

        System.out.println("Thread count is: " + threadCount);
        System.out.println("Time: " + (threadEndTime - threadStartTime));

        System.out.println("Counting (Single thread)");
        long singleStartTime = System.currentTimeMillis();

        int counter = 0;
        for (int j = 0; j < arrayCount; j++)
        {
            if (A[j] == x)
                counter++;
        }

        System.out.println("Single thread count is: " + counter);
        long singleEndTime = System.currentTimeMillis();

        System.out.println("Time: " + (singleEndTime - singleStartTime));
    }
}