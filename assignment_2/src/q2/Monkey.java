package q2;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {
	
	// declare the variables here 
	final int max_monkeys = 3;
	final ReentrantLock monitorLock = new ReentrantLock();
	final Condition waitingZero  = monitorLock.newCondition(); 
	final Condition waitingOne = monitorLock.newCondition();
	final Condition waitingKong = monitorLock.newCondition();
	
	int monkeysOnRope, currentDirection, 
		waitingMonkeysZero, waitingMonkeysOne, waitingMonkeyKong = 0;
	Random r = new Random();
	
	// A monkey calls the method when it arrives at the river bank and wants to climb 
	// the rope in the specified direction (0 or 1); Kong’s direction is -1. 
	// The method blocks a monkey until it is allowed to climb the rope. 
	public void ClimbRope(int direction) throws InterruptedException {
		
		monitorLock.lock();
		try {
			while (monkeysOnRope == max_monkeys || (monkeysOnRope > 0 && currentDirection != direction))
			{
				if (direction == -1) {
					waitingMonkeyKong++;
					waitingKong.await();
					waitingMonkeyKong--;
				}
				else if (direction == 0) {
					waitingMonkeysZero++;
					waitingZero.await();
					waitingMonkeysZero--;
				}
				else if (direction == 1) {
					waitingMonkeysOne++;
					waitingOne.await();
					waitingMonkeysOne--;
				}
			}
			monkeysOnRope++;
			currentDirection = direction;
//			System.out.println("Crossing: " + direction);
		} finally {
			monitorLock.unlock();
		}
	}
	
	// After crossing the river, every monkey calls this method which 
	// allows other monkeys to climb the rope. 
	public void LeaveRope() { 
		
		monitorLock.lock();
		try {
			monkeysOnRope--;
//			System.out.println("Getting off rope: " + currentDirection);
			if (waitingMonkeyKong > 0) {
				waitingKong.signal();
			}
			else {
				
				if (currentDirection == -1)
				{
					if (r.nextBoolean()) {
						if (waitingMonkeysZero > 0)
							waitingZero.signal();
						else if(waitingMonkeysOne > 0)
							waitingOne.signal();
					}
					else {
						if(waitingMonkeysOne > 0)
							waitingOne.signal();
						else if (waitingMonkeysZero > 0)
							waitingZero.signal();
					}
						
				}
				
				else if (currentDirection == 0) {
					
					if (waitingMonkeysZero > 0)
						waitingZero.signal();
					
					else if (monkeysOnRope == 0) {
						
						if (waitingMonkeysOne > 0) {
							waitingOne.signal();
						}
					}
				}
				else if (currentDirection == 1) {
					
					if (waitingMonkeysOne > 0)
						waitingOne.signal();
					
					else if (monkeysOnRope == 0) {
						
						if (waitingMonkeysZero > 0) {
							waitingZero.signal();
						}
					}
				}
			}
		 } finally {
			 monitorLock.unlock();
		 }
	} 
	/** 
	 * * Returns the number of monkeys on the rope currently for test purpose. 
	 * *
	 * * @return the number of monkeys on the rope 
	 * * 
	 * * Positive Test Cases: 
	 * * case 1: normal monkey (0 and 1)on the rope, this value should <= 3, >= 0 
	 * * case 2: when Kong is on the rope, this value should be 1 
	 * */ 
	public int getNumMonkeysOnRope() { 
//		if (monkeysOnRope > 3)
//			System.out.println("=================================Monkeys on rope: " + monkeysOnRope);
//		if (monkeysOnRope > 3)
//			System.out.println("Too many monkeys");
		return monkeysOnRope;
	}
}
