package q1_assignment2;


public class CountingSemaphore {
	private int value;

    public CountingSemaphore(int initValue) {
        this.value = initValue;
    }
    public synchronized void P() throws InterruptedException {
        while (this.value == 0) wait();
        this.value--;
    }
    public synchronized void V() {
        this.value++;
        notifyAll();
    }     
}