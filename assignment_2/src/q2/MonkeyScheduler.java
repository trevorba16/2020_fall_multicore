package q2;

public class MonkeyScheduler implements Runnable {
	Monkey monkey = null;
	int[] monkeyArray = null;
	
	@Override
	public void run() {
		
		for (int i = 0; i < monkeyArray.length; i++) {
			int m = monkey.getNumMonkeysOnRope();
			try {
				monkey.ClimbRope(monkeyArray[i]);
				monkey.LeaveRope();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Finished");
	}
	
	public MonkeyScheduler(Monkey m, int[] m_a) {
		this.monkey = m;
		this.monkeyArray = m_a;
		new Thread(this).start();
	}

}
