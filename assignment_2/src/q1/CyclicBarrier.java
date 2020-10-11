package q1_assignment2;

public class CyclicBarrier {
	private int n;
	private int pid;
	private CountingSemaphore mutex;
	private CountingSemaphore threadWait;
	private CountingSemaphore threadAdvance;
	private int count;
	private int partiesleft;
	public CyclicBarrier(int parties) {
	      /** Creates a new CyclicBarrier that will release threads only when the given number of threads are
	 *        waiting upon it */
	     int n; 
	     n = parties; 
	     partiesleft = parties; /* initially the number of threads yet to arrive is the total number of parties */
	     this.n = n; 
	     this.mutex = new CountingSemaphore(0); 
	     this.threadWait = new CountingSemaphore(0); 
	     this.threadAdvance = new CountingSemaphore(n); 
	}     

	public void down() throws InterruptedException {
	            this.step1(); /* we wait for n threads to arrive */
	            this.step2(); /* we wait for n threads to execute or be released */
	}

	public void step1() throws InterruptedException {
	            this.mutex.P();
	            this.count++;
	            if(this.count == this.n) {
	                for (int i = 0; i < this.n; i++) {
	                    this.threadWait.V(); /* n threads have been received and are ready to move on */
	                }
	            }
	            this.mutex.V();
	            this.await();  /* we wait until we get n threads */
	 }
	     
	public void step2() throws InterruptedException {
	            this.mutex.P();
	            this.count--;
	            if(this.count == 0) {
	                for (int i = 0; i < this.n; i++) {
	                    this.threadAdvance.V();  /* the barrier can be reused */
	                }
	            }
	            this.mutex.V();
	            this.threadAdvance.P(); /* we keep waiting until n threads get executed or released */
	 }

	int await() throws InterruptedException {
	      /** Waits until all parties have invoked await on this CyclicBarrier, if the current thread is not the last
	 * to arrive then it is disabled for thread scheduling purposes and lies dormant until the last thread arrives
	 * Returns: the arrival index of the current thread, where index (parties -1) indicates the first to arrive and 
	 * zero indicates the last to arrive */


	         
				partiesleft--; /* every time await() is called, there is one less party to arrive */

	            if (partiesleft > 0) {
	               this.wait();  /* if the current thread is not the last to arrive, it waits */
	               this.pid = partiesleft;

	               return pid;
	            }             /* we return the arrival index of the current thread */

	            else {

	               this.pid = partiesleft;

	               partiesleft = n; /* if all parties have arrived, we go back so the CyclicBarrier can be truly cyclic */

	               notifyAll(); /* we notify all the waiting threads */

	               return pid;  /* index of the last thread to arrive */

	            }

	   }
}
