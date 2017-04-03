import java.io.Serializable;
/**
 * A coordinate is a point on a plane that stores 2 int values
 * 
 * @author Jérémie Beaudoin-Dion
 *
 */
public class Coordinate implements Serializable{
	
	private int x;
	private int y;
	
	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 */
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getters
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

}
