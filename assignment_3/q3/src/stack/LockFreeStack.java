package stack;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
    // you are free to add members
	

	AtomicReference<Node> top = new AtomicReference<Node>();   // pointer to the top of the Stack

    public LockFreeStack() {
        // implement your constructor here
    }

	/** Add x to the top of the Stack
	 * 
	 * @param x
	 */
	public boolean push(Integer x) {
		Node newTop;
		newTop = new Node(x);
		newTop.value = x;  // we create the new top and store the value x
		while (true) {
		   Node oldTop = top.get();  // we read the old top and set up the new one with a CAS instruction
		   newTop.next = oldTop;
		   if (top.compareAndSet(oldTop, newTop)) return true;
		   else Thread.yield();
		}
	}

	public Integer pop() throws EmptyStack {
		while (true) {
	      Node oldTop = top.get(); 	  // we read the old top and throw an exception is the Stack is empty
		  if (top == null)
			  throw new EmptyStack ();
		  Integer topItem = oldTop.value; // the value that is being popped
		  Node newTop = oldTop.next; // the previous second item is now on top
		  if (top.compareAndSet(oldTop,newTop)) return topItem;  // return the popped value with a CAS instruction
		  else Thread.yield();  
		}	  
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
