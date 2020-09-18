package q5.TournamentLock;

import java.io.*;

public class PIncrement {
    public static int parallelIncrement(int c, int numThreads) {

    private final static int m = 1200000;

      private static class PIncrementThread extends Thread {
          int id; // id number for the thread
          public PIncrementThread(int id) {
          this.id = id;
          }
        public void run() {
           private volatile int c;
           long startTime = System.currentTimeMillis();
           c = incrementNumber(1,m); // increments c 
           long elapsedTime = System.currentTimeMillis() - startTime;
           System.out.println("Thread " + id + " incremented " + c + " in " + (elapsedTime/1000.0) + " seconds. "); 
        }  
      }
        public static void main(String[] args) {
           int numThreads = 0;
           while (numThreads < 1 || numThreads > 8) {
              System.out.print("How many threads do you want to use (1 to 8)? ");
              numThreads = in.readLine();
              if (numThreads < 1 || numThreads > 8)
                 System.outprintln("Please enter a number between 1 and 8!");
           }
           System.out.println("\nCreating " + numThreads + " increment threads...");
           PIncrementThread[]  worker = new PIncrementThread[numThread];
           for (int i = 0; i < numThread; i++)
               worker[i] = new PIncrementThread( i );
           for (int i = 0; i < numThread; i++)
               worker[i].start();
               TournamentLock enterCS = new TournamentLock[i];
           System.out.println("Threads have been created and started");
        }

        /**
         * Here we increment the value of the shared variable c, m/numThreads times
         */
        private static int incrementNumber(int min, int m) {
              int c = 0;
              if (numThreads != 7) {
                 for (int i=min; i<=m/numThreads; i++) {
                    while (enterCS.lock) {
                      c++;
                    } 
                 }
              }
              else {
                 int max = int(m/numThreads);
                 for (int i=min; i<=max; i++)  {
                   while (enterCS.lock) {
                     c++;
                   }  
                 }
              }
              return c; 
        }

    }
}
