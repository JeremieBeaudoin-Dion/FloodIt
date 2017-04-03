import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
* A custom messagePane to show the settings of the game
*
* @author Jérémie Beaudoin-Dion
*/
public class MessagePane extends JOptionPane {
	
	private GameView gameView;
	private GameController gameController;
	
	// What to draw on screen
	private JPanel message;
	
	// Radio buttons
	private JRadioButton planeButton;
	private JRadioButton torusButton;
	private JRadioButton orthogonalButton;
	private JRadioButton diagonalButton;
	
	/**
	 * Constructor
	 * 
	 * @param gameController : the current gameController
	 * @param gameView : the current gameView
	 */
	public MessagePane(GameController gameController, GameView gameView) {
		super();
		this.gameController = gameController;
		this.gameView = gameView;
		
		message = createCustomMessage();
	}
	
	/**
	 * Creates the object message in a JPanel to show on the OptionPane
	 * 
	 * @return
	 */
	private JPanel createCustomMessage() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		
		panel.add(planeOrTorus());
		panel.add(orthogonalOrDiagonal());
		
		return panel;
	}
	
	/**
	 * The first option is to play on Plane or Torus
	 * 
	 * @return the panel to add
	 */
	private JPanel planeOrTorus() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		
		JLabel text = new JLabel("Play on plane or torus?");
		
		planeButton = new JRadioButton("Plane");
		planeButton.addActionListener(gameController);
		
		torusButton = new JRadioButton("Torus");
		torusButton.addActionListener(gameController);
		
		panel.add(text);
		panel.add(planeButton);
		panel.add(torusButton);
		
		return panel;
	}
	
	/**
	 * The second option is to play while conquering diagonal or orthogonal
	 * adjacent buttons
	 * 
	 * @return the panel to add
	 */
	private JPanel orthogonalOrDiagonal() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1));
		
		JLabel text = new JLabel("Diagonal moves?");
		
		orthogonalButton = new JRadioButton("Orthogonal");
		orthogonalButton.addActionListener(gameController);
		
		diagonalButton = new JRadioButton("Diagonal");
		diagonalButton.addActionListener(gameController);
		
		panel.add(text);
		panel.add(orthogonalButton);
		panel.add(diagonalButton);
		
		return panel;
		
	}
	
	/**
	 * Call this method to draw on screen
	 */
	public void showMessage() {
		showMessageDialog(gameView, message);
	}
	
	/**
	 * Sets the value for plane or torus buttons
	 */
	public void setPlaneButtonValue(boolean isPlane){
		planeButton.setSelected(isPlane);
		torusButton.setSelected(!isPlane);
	}
	
	/**
	 * Sets the value for orthogonal or diagonal buttons
	 */
	public void setOrthogonalButtonValue(boolean isOrthogonal){
		orthogonalButton.setSelected(isOrthogonal);
		diagonalButton.setSelected(!isOrthogonal);
	}

}
