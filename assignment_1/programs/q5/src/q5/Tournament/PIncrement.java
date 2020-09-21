package q5.Tournament;

import java.io.*;

public class PIncrement {

    private final static int m = 1200000;

      private static class PIncrementThread extends Thread {
    	  int id; // id number for the thread
          int c;
          int numThreads;
          TournamentLock enterCS;
          
          public PIncrementThread(int id, int c, int numThreads, TournamentLock enterCS) {
	          this.id = id;
	          this.c = c;
	          this.numThreads = numThreads;
	          this.enterCS = enterCS;
          }
          
	      public void run() {
	           incrementNumber(1,m); // increments c with synchronized access for each thread
	        }  
        /**
         Here we increment the value of the shared variable c, m/numThreads times
           **/
        private void incrementNumber(int min, int max) {
              if (numThreads != 7) {
                 for (int i=min; i<=m/numThreads; i++) {
                    enterCS.lock(id);
                    c++;
                    enterCS.unlock(id);
                 }
              }
              else {
                 int top = m/numThreads;
                 for (int i=min; i<=max; i++)  {
	            	 enterCS.lock(id);
	                 c++;
	                 enterCS.unlock(id);
                 }
              }
        } 
      }
        public static void main(String[] args) {
        	for (int numThreads = 1; numThreads < 9; numThreads ++)
            {
         	   System.out.println("\nCreating " + numThreads + " increment number threads...");
         	   	long startTime = System.currentTimeMillis();
           
		           PIncrementThread[]  worker = new PIncrementThread[numThreads];
		           TournamentLock enterCS = new TournamentLock();
		           int c = 0;
		
		           for (int i = 0; i < numThreads; i++)
		               worker[i] = new PIncrementThread( i, 0, numThreads, enterCS );
		           for (int i = 0; i < numThreads; i++)
		               worker[i].start();
		           
		           long elapsedTime = System.currentTimeMillis() - startTime;
		           System.out.println("\nTotal elapsed time: " + (elapsedTime/1000.0) + " seconds.\n");
            }
        }

}
