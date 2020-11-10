package queue;

import java.util.concurrent.atomic.*;

public class LockFreeQueue implements MyQueue {
    // you are free to add members
	private Node emptyNode = new Node(null);
	private AtomicReference<Node> head = new AtomicReference<Node>(emptyNode);
	private AtomicReference<Node> tail = new AtomicReference<Node>(emptyNode);
	private AtomicInteger count = new AtomicInteger(0);
	
    public LockFreeQueue() {
    }

    public boolean enq(Integer value) {
        Node insertNode = new Node(value);
        
        while(true) {
        	Node currentTail = tail.get();
        	Node nextNode = currentTail.next.get();
        	if (currentTail == tail.get()) {
        		if (nextNode == null) {
        			if (currentTail.next.compareAndSet(null, insertNode)) {
        				tail.compareAndSet(currentTail, insertNode);
        				return true;
        			}
        		}
        		else {
        			tail.compareAndSet(currentTail, nextNode);
        		}
        	}
        }
    }

    public Integer deq() {
        while (true) {
        	Node headNode = head.get();
        	Node tailNode = tail.get();
        	Node nextNode = headNode.next.get();
        	
        	if (headNode == head.get()) {
        		if (headNode.next == tailNode.next) {
        			if (nextNode.next == null) {
        				return null;
        			}
        			tail.compareAndSet(tailNode, nextNode);
        		}
        		else {
        			Integer value = nextNode.value;
        			if (head.compareAndSet(headNode, nextNode)) {
        				return value;
        			}
        		}
        	}
        }
    }

    protected class Node {
        public Integer value;
        public AtomicReference<Node> next;

        public Node(Integer x) {
            value = x;
            next = null;
        }
    }
}
