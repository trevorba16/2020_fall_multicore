package q2;

import java.util.Random;

public class q2 {
	
	public static void main(String[] args) {
		
		Random r = new Random();
		int[] monkeyArray = new int[500000];
		
		for (int i = 0; i < 500000; i++) {
			if (r.nextInt() % 20 == 0)
				monkeyArray[i] = -1;
			monkeyArray[i] = r.nextInt() % 2;
		}
		
		Monkey monkey = new Monkey();
		MonkeyScheduler ms1 = new MonkeyScheduler(monkey, monkeyArray);
		MonkeyScheduler ms2 = new MonkeyScheduler(monkey, monkeyArray);
		MonkeyScheduler ms3 = new MonkeyScheduler(monkey, monkeyArray);
		MonkeyScheduler ms4 = new MonkeyScheduler(monkey, monkeyArray);
		
	}

}
