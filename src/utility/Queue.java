package utility;

/* Queue.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */
 
import java.util.ArrayList;
import java.util.List;
 
public class Queue {
	private ArrayList<Object> v;
	
	/** Queue()
	 * 
	 * creates a new queue
	 */
	public Queue() {
		v = new ArrayList<>();
	}
	
	public Object next() {
		return v.remove(0);
	}

	public void add(Object o) {
		v.add(o);
	}
	
	public boolean hasMoreElements() {
		return !v.isEmpty();
	}

	public List<Object> asVector() {
		return v;
	}
	
}
