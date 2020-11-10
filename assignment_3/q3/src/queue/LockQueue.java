package queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
    // you are free to add members
	private ReentrantLock enqLock = new ReentrantLock();
	private ReentrantLock deqLock = new ReentrantLock();
	Node head;
	Node tail;
	AtomicInteger count = new AtomicInteger(0);
	
    public LockQueue() {
        head = new Node(null);
        tail = head;
    	
    }

    public boolean enq(Integer value) {
    	boolean success = false;
        enqLock.lock();
        try {
        	Node e = new Node(value);
        	tail.next = e;
        	tail = e;
        	success = true;
        } finally {
        	enqLock.unlock();
        }
        return success;
    }

    public Integer deq() {
        Integer result;
        deqLock.lock();
        try {
        	if (head.next == null)
        		return null;
        	result = head.next.value;
        	head = head.next;
        } finally {
        	deqLock.unlock();
        }
        return result;
    }

    protected class Node {
        public Integer value;
        public Node next;

        public Node(Integer x) {
            value = x;
            next = null;
        }
    }
}
