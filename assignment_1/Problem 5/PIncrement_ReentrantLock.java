package q5.ReentrantLock;

import java.io.*;
import java.util.concurrent.*;

public class PIncrement implements Runnable {
    public static int parallelIncrement(int c, int numThreads) {
    private final static int m = 1200000;

      private static class PIncrementThread {
          int id; // id number for the thread
          public PIncrementThread(int id) {
          this.id = id;
          }
        public void run() {
           int c = incrementNumber(1,m); // increments c with reentrant lock access for each thread
           System.out.println("Thread " + id + " incremented " + c + " in " + (elapsedTime/1000.0) + " seconds. ");
        }  
      }
        public static void incrementWithThreads(int numThreads) {
           long startTime = System.currentTimeMillis();
           ReentrantLock rel = new ReentrantLock();
           PIncrementThread[] worker = new PIncrementThread[numThread];
           for (int i = 0; i < numThread; i++)
               worker[i] = new PIncrementThread( i );
           for (int i = 0; i < numThread; i++) {
               rel.lock();
               try {
                  worker[i].start();
               }
               catch (InterruptedException e) {
               } rel.unlock();
           }
           long elapsedTime = System.currentTimeMillis() - startTime;
           System.out.println("\nTotal elapsed time: " + (elapsedTime/1000.0) + " seconds.\n");
        }
          

        public static void main(String[] args) {
           int numThreads = 0;
           while (numThreads < 1 || numThreads > 8) {
              System.out.print("How many threads do you want to use (1 to 8)? ");
              numThreads = in.readLine();
              if (numThreads < 1 || numThreads > 8)
                 System.outprintln("Please enter a number between 1 and 8!");
           }
           System.out.println("\nCreating " + numThreads + " increment number threads...");
           incrementWithThreads(numThreads)
        }

        /**
         * Here we increment the value of the shared variable c, m/numThreads times
         */
        private static void incrementNumber(int min, int m) {
              int c = 0;
              if (numThreads != 7) {
                 for (int i=min; i<=m/numThreads; i++)
                   c++;
              }
              else {
                 int max = int(m/numThreads);
                 for (int i=min; i<=max; i++)
                   c++;
              }
              return c;
        }

    }

}
