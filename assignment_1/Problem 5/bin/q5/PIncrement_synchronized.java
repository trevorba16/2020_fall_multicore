package q5;

import java.io.*;

public class PIncrement_synchronized {

    private final static int m = 1200000;

    public static class PIncrementThread extends Thread {
    	  
          int id; // id number for the thread
          int c;
          int numThreads;
          
          public PIncrementThread(int id, int c, int numThreads) {
	          this.id = id;
	          this.c = c;
	          this.numThreads = numThreads;
          }
          
	      public void run() {
	           incrementNumber(1,m); // increments c with synchronized access for each thread
	        }  
	        
	        /**
	         * Here we increment the value of the shared variable c, m/numThreads times
	         */
	        synchronized private void incrementNumber(int min, int max) {
	              if (numThreads != 7) {
	                 for (int i = min; i<= m / numThreads; i++) // i am going to increment c this many times
	                 {
	                	 c++;
	                 }
	              }
	              else {
	                 int top = m / numThreads;
	                 for (int i = min; i <= top; i++)
	                   c++;
	              }
	        }
      }
      
        public static void incrementWithThreads(int numThreads) {
           long startTime = System.currentTimeMillis();
           PIncrementThread[] worker = new PIncrementThread[numThreads];
           
           int c = 0;
           
           for (int i = 0; i < numThreads; i++)
           {
        	   PIncrementThread p = new PIncrementThread(i+1, c, numThreads);

               worker[i] = p;
           }
           for (int i = 0; i < numThreads; i++)
               worker[i].start();
           for (int i = 0; i < numThreads; i++) {
               try {
                  worker[i].join();
               }
               catch (InterruptedException e) {
               }
           }
           long elapsedTime = System.currentTimeMillis() - startTime;
           System.out.println("\nTotal elapsed time: " + (elapsedTime/1000.0) + " seconds.\n");
        }
          

        public static void main(String[] args) {
           int numThreads = 0;
           for (int i = 1; i < 9; i ++)
           {
        	   System.out.println("\nCreating " + i + " increment number threads...");
           	   incrementWithThreads(i);
           }
        }


}
