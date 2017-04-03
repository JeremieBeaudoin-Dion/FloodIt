import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;


/**
 * The class <b>GameController</b> is the controller of the game. It has a method
 * <b>selectColor</b> which is called by the view when the player selects the next
 * color. It then computesthe next step of the game, and  updates model and view.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Jérémie Beaudoin-Dion
 */


public class GameController implements ActionListener {

	/**
	 * Reference to the view of the board
	 */
	private GameView gameView;
	/**
	 * Reference to the model of the game
	 */
	private GameModel gameModel;

	private MessagePane messagePane;

	private Stack<GameModel> undoStack;
	private Stack<GameModel> redoStack;

	/**
	 * Constructor used for initializing the controller. It creates the game's view 
	 * and the game's model instances
	 * 
	 * @param size
	 *            the size of the board on which the game will be played
	 */
	public GameController(int size) {
		if(!loadGameModel()){
			gameModel = new GameModel(size);
		}
		gameView = new GameView(gameModel, this);
		messagePane = new MessagePane(this, gameView);

		undoStack = new GenericLinkedStack<GameModel>();
		redoStack = new GenericLinkedStack<GameModel>();

		flood();
		gameView.update(gameModel);
	}

	/**
	 * Reads the gameModel from a file
	 */
	private boolean loadGameModel() {
		try {
			FileInputStream file = new FileInputStream("savedGame.ser");
			ObjectInputStream saved = new ObjectInputStream(file);
			gameModel = (GameModel) saved.readObject();
			saved.close();
			file.close();
			return true;
		}catch(IOException i) {
			// The file is not found
			return false;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("GameModel class not found");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * resets the game
	 */
	public void reset(){
		gameModel.reset();
		undoStack = new GenericLinkedStack<GameModel>();
		redoStack = new GenericLinkedStack<GameModel>();
		flood();
		gameView.update(gameModel);
	}

	/**
	 * Callback used when the user clicks a button (reset or quit)
	 *
	 * @param e
	 *            the ActionEvent
	 */

	public void actionPerformed(ActionEvent e) {

		// A dot button changes the color
		if (e.getSource() instanceof DotButton) {
			handleDotButtonAction((DotButton) e.getSource());
			

		} else if (e.getSource() instanceof JRadioButton) {
			// A radio button changes the messagePane
			handleJRadioButtonAction((JRadioButton) e.getSource());

		} else if (e.getSource() instanceof JButton) {
			// A JButton handles the "quit" and "reset" actions
			handleJButtonAction((JButton) e.getSource());
			
		}

	}
	
	/**
	* Handles a JRadioButton action
	*
	* @param the button on which the action was made
	*/
	private void handleJRadioButtonAction(JRadioButton clicked){
		if (clicked.getText().equals("Plane")){
			gameModel.setIsPlane(true);
			messagePane.setPlaneButtonValue(true);

		} else if (clicked.getText().equals("Torus")){
			gameModel.setIsPlane(false);
			messagePane.setPlaneButtonValue(false);

		} else if (clicked.getText().equals("Diagonal")){
			gameModel.setIsOrthogonal(false);
			messagePane.setOrthogonalButtonValue(false);

		} else if (clicked.getText().equals("Orthogonal")){
			gameModel.setIsOrthogonal(true);
			messagePane.setOrthogonalButtonValue(true);
		}
	}
	
	/**
	* Handles a JButton action
	*
	* @param the button on which the action was made
	*/
	private void handleJButtonAction(JButton clicked){
		if (clicked.getText().equals("Quit")) {
			saveGameModel();
			System.exit(0);
		} else if (clicked.getText().equals("Reset")){
			reset();
		} else if (clicked.getText().equals("Undo")){
			if(!undoStack.isEmpty()){
				redoStack.push(gameModel.clone());
				gameModel = undoStack.pop();
				gameView.update(gameModel);
			}
		} else if (clicked.getText().equals("Redo")){
			if(!redoStack.isEmpty()){
				undoStack.push(gameModel.clone());
				gameModel = redoStack.pop();
				gameView.update(gameModel);
			}
		} else if (clicked.getText().equals("Settings")){
			gameView.update(gameModel);
			messagePane.setPlaneButtonValue(gameModel.getIsPlane());
			messagePane.setOrthogonalButtonValue(gameModel.getIsOrthogonal());
			messagePane.showMessage();
		}
	}
	
	/**
	 * Saves the gameModel in a file
	 */
	private void saveGameModel() {
		try {
        	FileOutputStream fileOut = new FileOutputStream("savedGame.ser");
        	ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gameModel);
            out.close();
            fileOut.close();
        } catch(IOException i) {
        	i.printStackTrace();
        }
	}

	/**
	* Handles a DotButton action
	*
	* @param the button on which the action was made
	*/
	private void handleDotButtonAction(DotButton button){
		redoStack = new GenericLinkedStack<GameModel>();
		captureFirstDot(button); // handles condition and capture first dot if necessary
		selectColor(button.getColor());
		gameView.update(gameModel);
	}
	
	/**
	 * If necessary, capture the first dot of the gameModel
	 *
	 * @param: the button to capture
	 */
	private void captureFirstDot(DotButton button) {
		// Condition to capture first dot: numberOfSteps <= -1
		if (gameModel.getNumberOfSteps() < 0) {
			undoStack.push(gameModel.clone());
			gameModel.capture(button.getRow(), button.getColumn());
		}
	}
	
	/**
	 * <b>selectColor</b> is the method called when the user selects a new color.
	 * If that color is not the currently selected one, then it applies the logic
	 * of the game to capture possible locations. It then checks if the game
	 * is finished, and if so, congratulates the player, showing the number of
	 * moves, and gives to options: start a new game, or exit
	 * @param color
	 *            the newly selected color
	 */
	public void selectColor(int color){
		if(color != gameModel.getCurrentSelectedColor()) {
			// If captureFirstDot, don't remember gameModel
			if(gameModel.getNumberOfSteps() > 0){
				undoStack.push(gameModel.clone());
			}
			
			gameModel.setCurrentSelectedColor(color);
			flood();
			gameModel.step();

			// Finish the game
			if(gameModel.isFinished()) {
				showWinOptionPane();
			}
		}        
	}

	/**
	 * Creates an option pane dialog if the game is finished
	 */
	private void showWinOptionPane() {
		// Update before showing the win condition
		gameView.update(gameModel);
		// Create the JOptionPane
		Object[] options = {"Play Again",
		"Quit"};
		int n = JOptionPane.showOptionDialog(gameView,
				"Congratulations, you won in " + gameModel.getNumberOfSteps() 
				+" steps!\n Would you like to play again?",
				"Won",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		// Options
		if(n == 0){
			reset();
		} else{
			System.exit(0);
		}  
	}

	/**
	 * <b>flood</b> is the method that computes which new dots should be ``captured'' 
	 * when a new color has been selected. The Model is updated accordingly
	 */
	private void flood() {

		Stack<DotInfo> floodStack = findAllCapturedDots();
		Stack<DotInfo> adjacentDots;

		DotInfo currentDot;
		while(!floodStack.isEmpty()){
			currentDot = floodStack.pop();

			adjacentDots = findAllAdjacentDots(currentDot);

			while(!adjacentDots.isEmpty()){
				floodStack.push(adjacentDots.pop());
			}

		}
	}

	/**
	 * Finds all the captured dots of the game model and returns them in a stack
	 * 
	 * @return
	 */
	private Stack<DotInfo> findAllCapturedDots() {

		Stack<DotInfo> floodStack = new GenericLinkedStack<DotInfo>();

		for(int i =0; i < gameModel.getSize(); i++) {
			for(int j =0; j < gameModel.getSize(); j++) {
				if(gameModel.isCaptured(i,j)) {
					floodStack.push(gameModel.get(i,j));
				}
			}
		}

		return floodStack;
	}

	/**
	 * Finds all the adjacent dots from the parameter
	 * 
	 * @param dotInfo
	 * @return: a stack with all the dots
	 */
	private Stack<DotInfo> findAllAdjacentDots(DotInfo dotInfo) {
		Stack<DotInfo> stack = new GenericLinkedStack<DotInfo>();

		Stack<Coordinate> adjacent = findAdjacentCoordonates(dotInfo.getX(), dotInfo.getY());
		Coordinate coordonates;

		while(!adjacent.isEmpty()){
			coordonates = adjacent.pop();

			if(shouldBeCaptured (coordonates.getX(), coordonates.getY())) {
				gameModel.capture(coordonates.getX(), coordonates.getY());
				stack.push(gameModel.get(coordonates.getX(), coordonates.getY()));
			}
		}

		return stack;
	}

	/**
	 * Finds all the coordinates that should be considered as adjacent
	 * 
	 * @return
	 */
	private Stack<Coordinate> findAdjacentCoordonates(int x, int y) {
		Stack<Coordinate> adjacent = new GenericLinkedStack<Coordinate>();

		if (gameModel.getIsPlane()){

			// Orthogonal is always counted
			if (x + 1 < gameModel.getSize()){
				adjacent.push(new Coordinate(x + 1, y));
			}
			if (y + 1 < gameModel.getSize()){
				adjacent.push(new Coordinate(x, y + 1));
			}
			if (x - 1 >= 0){
				adjacent.push(new Coordinate(x - 1, y));
			}
			if (y - 1 >= 0){
				adjacent.push(new Coordinate(x, y - 1));
			}

			// Checks the diagonals
			if (!gameModel.getIsOrthogonal()){

				if (x + 1 < gameModel.getSize()){
					if (y + 1 < gameModel.getSize()){
						adjacent.push(new Coordinate(x + 1, y + 1));
					}
					if (y - 1 >= 0){
						adjacent.push(new Coordinate(x + 1, y - 1));
					}
				}

				if (x - 1 >= 0){
					if (y + 1 < gameModel.getSize()){
						adjacent.push(new Coordinate(x - 1, y + 1));
					}
					if (y - 1 >= 0){
						adjacent.push(new Coordinate(x - 1, y - 1));
					}
				}
			}

		} else {
			// We are on a Torus mode
			// Orthogonal is always counted
			adjacent.push(new Coordinate((x + 1) % gameModel.getSize(), y));
			adjacent.push(new Coordinate(positiveModulo((x - 1), gameModel.getSize()), y));
			adjacent.push(new Coordinate(x, (y + 1) % gameModel.getSize()));
			adjacent.push(new Coordinate(x, positiveModulo((y - 1), gameModel.getSize())));

			// Checks the diagonals
			if (!gameModel.getIsOrthogonal()){
				adjacent.push(new Coordinate((x + 1) % gameModel.getSize(), (y + 1) % gameModel.getSize()));
				adjacent.push(new Coordinate((x + 1) % gameModel.getSize(), positiveModulo((y - 1), gameModel.getSize())));
				adjacent.push(new Coordinate(positiveModulo((x - 1), gameModel.getSize()), (y + 1) % gameModel.getSize()));
				adjacent.push(new Coordinate(positiveModulo((x - 1), gameModel.getSize()), positiveModulo((y - 1), gameModel.getSize())));
			}
		}
		return adjacent;
	}

	/**
	 * Java interestingly doesn't do modulo with negative numbers... 
	 * to avoid writing too much code, here is a modulo that always returns 
	 * a positive number.
	 */
	private int positiveModulo(int number, int modulo){
		return (((number % modulo) + modulo) % modulo);
	}

	/**
	 * <b>shouldBeCaptured</b> is a helper method that decides if the dot
	 * located at position (i,j), which is next to a captured dot, should
	 * itself be captured
	 * @param i
	 *            row of the dot
	 * @param j
	 *            column of the dot
	 */
	private boolean shouldBeCaptured(int i, int j) {
		if(!gameModel.isCaptured(i, j) &&
				(gameModel.getColor(i, j) == gameModel.getCurrentSelectedColor())) {
			return true;
		} else {
			return false;
		}
	}


}
