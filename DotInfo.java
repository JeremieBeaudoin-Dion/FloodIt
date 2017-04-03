import java.io.Serializable;

/**
 * The class <b>DotInfo</b> is a simple helper class to store the initial color and state
 * (captured or not) at the dot position (x,y)
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 */

public class DotInfo implements Cloneable, Serializable {

    /**
	 * Used for the serializable version
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * The coordinate of this DotInfo.
     */
    private Coordinate point;

    /**
     * The initial color at (x,y)
     */
    private int color;
	
    /**
     * Is that locatio captured ?
     */
    private boolean captured;

    /**
     * Constructor 
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param color
     *            the initial color
     */
    public DotInfo(int x, int y, int color){
		point = new Coordinate(x, y);
        this.color = color;
    }
	
	/**
	* Constructor for clonable
	* @param x
    *            the x coordinate
    * @param y
    *            the y coordinate
    * @param color
    *            the initial color
	* @param captured
	*			true if the DotInfo is captured
	*/
	public DotInfo(int x, int y, int color, boolean captured){
        point = new Coordinate(x, y);
        this.color = color;
		this.captured = captured;
    }
	
	/**
	* Returns a DeepCopy of this object
	*/
	public DotInfo clone() {
		return new DotInfo(point.getX(), point.getY(), color, captured);
	}

    /**
     * Getter method for the attribute x.
     * 
     * @return the value of the attribute x
     */
    public int getX(){
        return point.getX();
    }
    
    /**
     * Getter method for the attribute y.
     * 
     * @return the value of the attribute y
     */
    public int getY(){
        return point.getY();
    }
 
    /**
     * Setter for captured
     * @param captured
     *            the new value for captured
     */
    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

    /**
     * Get for captured
     *
     * @return captured
     */
    public boolean isCaptured(){
        return captured;
    }

    /**
     * Get for color
     *
     * @return color
     */
    public int getColor() {
        return color;
    }

 }
