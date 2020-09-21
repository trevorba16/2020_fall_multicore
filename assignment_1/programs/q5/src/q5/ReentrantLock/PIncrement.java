package q5.ReentrantLock;

import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class PIncrement{

    private final static int m = 1200000;

      private static class PIncrementThread implements Runnable  {
    	  int id; // id number for the thread
          int c;
          int numThreads;
          ReentrantLock re;
          
          public PIncrementThread(int id, int c, int numThreads, ReentrantLock re) {
	          this.id = id;
	          this.c = c;
	          this.numThreads = numThreads;
	          this.re = re;
          }
          
	      public void run() {
	           incrementNumber(1,m); // increments c with synchronized access for each thread
	        }  
	        
	        /**
	         * Here we increment the value of the shared variable c, m/numThreads times
	         */
	      private void incrementNumber(int min, int max) {
              if (numThreads != 7) {
                 for (int i = min; i<= m / numThreads; i++) // i am going to increment c this many times
                 {
                	 if(re.tryLock())
                	 {
                		c++;
                		re.unlock();
                	 }
                 }
              }
              else {
                 int top = m / numThreads;
                 for (int i = min; i <= top; i++)
                 {
                	 if(re.tryLock())
                	 {
                		c++;
                		re.unlock();
                	 }
                 }
              }
	        }
      }
        public static void incrementWithThreads(int numThreads) {
           long startTime = System.currentTimeMillis();
           PIncrementThread[] worker = new PIncrementThread[numThreads];
          
           ReentrantLock rel = new ReentrantLock();
           ExecutorService pool = Executors.newFixedThreadPool(numThreads);
           
           int c = 0;

           for (int i = 0; i < numThreads; i++)
           {
        	   PIncrementThread p = new PIncrementThread(i+1, c, numThreads, rel);
               worker[i] = p;
           }
           for (int i = 0; i < numThreads; i++) {
              pool.execute(worker[i]);
           }
           
           long elapsedTime = System.currentTimeMillis() - startTime;
           System.out.println("\nTotal elapsed time: " + (elapsedTime/1000.0) + " seconds.\n");
        }
          
        public static void main(String[] args) {
            for (int i = 1; i < 9; i ++)
            {
         	   System.out.println("\nCreating " + i + " increment number threads...");
            	   incrementWithThreads(i);
            }
         }

}
