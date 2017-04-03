/**
* An exception thrown when a Stack is being used when empty.
*
* @author Jérémie Beaudoin-Dion
*/
public class EmptyStackException extends RuntimeException {
	
	public EmptyStackException(String message) {
		super(message);
	}

}
