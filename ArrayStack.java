import java.util.ArrayList;
import java.util.List;
import java.util.EmptyStackException;
/**
 *Simple stack using an ArrayList
 *@author Anushka Vijay
 * @since February 26, 2019
 */
public class ArrayStack<E> implements Stack<E>
{
	private List<E> elements;
	
	public ArrayStack() 
	{
		elements = new ArrayList<E>();
	}
	
	/** @return 	true if the stack is empty, false otherwise
	 */
	public boolean isEmpty()
	{ return elements.isEmpty(); }
	
	/** @return		top element on the stack
	 */
	public E peek()
	{
		if(elements.isEmpty())
			throw new EmptyStackException();
		return elements.get(elements.size()-1);
	}
	
	/** @param obj		object to push on the top of th stack
	 */
	 public void push(E obj) { elements.add(obj); }
	 
	 /** @return	the object removed from the top of the stack
	  */
	  public E pop()
	  {
		if(elements.isEmpty())
			throw new EmptyStackException();
		return elements.remove(elements.size()-1);
	  }
	  
	  /**
	   * finds number of elements in stack
	   * @return	number of elements in stack
	   */
	  public int size()
	  {
		return elements.size();
	  }
}
