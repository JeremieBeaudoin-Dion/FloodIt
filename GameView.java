import java.awt.*;

import javax.swing.*;

/**
 * The class <b>GameView</b> provides the current view of the entire Game. It extends
 * <b>JFrame</b> and lays out an instance of  <b>BoardView</b> (the actual game) and 
 * two instances of JButton. The action listener for the buttons is the controller.
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Jérémie Beaudoin-Dion
 */

public class GameView extends JFrame {


    /**
     * The board is a two dimensional array of DotButtons instances
     */
    private DotButton[][] board;

 
    /**
     * Reference to the model of the game
     */
    private GameModel gameModel;
 
    private GameController gameController;

    private JLabel scoreLabel;
	
    /**
     * Constructor used for initializing the Frame
     * 
     * @param model
     *            the model of the game (already initialized)
     * @param gameController
     *            the controller
     */
    public GameView(GameModel model, GameController gameController) {
        super("Flood it");

        this.gameModel = model;
        this.gameController = gameController;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	// setBackground(Color.WHITE);
    	
    	drawOptions();
    	
    	drawBoard();

    	drawControl();

    	pack();
    	//setResizable(false);
    	setVisible(true);

    }
    
    /**
     * Draws the option panel at the NORTH of the screen
     */
    private void drawOptions() {
    	JButton buttonUndo = new JButton("Undo");
    	buttonUndo.setFocusPainted(false);
    	buttonUndo.addActionListener(gameController);

    	JButton buttonRedo = new JButton("Redo");
    	buttonRedo.setFocusPainted(false);
    	buttonRedo.addActionListener(gameController);
    	
    	JButton buttonSettings = new JButton("Settings");
    	buttonSettings.setFocusPainted(false);
    	buttonSettings.addActionListener(gameController);

        JPanel control = new JPanel();
        control.add(buttonUndo);
        control.add(buttonRedo);
        control.add(buttonSettings);
        add(control, BorderLayout.NORTH);
    }
    
    /**
     * Draws the game board at the CENTER of the screen
     */
    private void drawBoard() {
    	JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(gameModel.getSize(), gameModel.getSize()));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        board = new DotButton[gameModel.getSize()][gameModel.getSize()];

        for (int row = 0; row < gameModel.getSize(); row++) {
            for (int column = 0; column < gameModel.getSize(); column++) {
                board[row][column] = new DotButton(row, column, gameModel.getColor(row,column), 
                    (gameModel.getSize() < 26 ? DotButton.MEDIUM_SIZE : DotButton.SMALL_SIZE));
                board[row][column].addActionListener(gameController);
                panel.add(board[row][column]);
            }
        }
    	add(panel, BorderLayout.CENTER);
    }
    
    /**
     * Draws the control board at the SOUTH of the screen
     */
    private void drawControl() {
    	JButton buttonReset = new JButton("Reset");
        buttonReset.setFocusPainted(false);
        buttonReset.addActionListener(gameController);

        JButton buttonExit = new JButton("Quit");
        buttonExit.setFocusPainted(false);
        buttonExit.addActionListener(gameController);

        JPanel control = new JPanel();
        scoreLabel = new JLabel();
        control.add(scoreLabel);
        control.add(buttonReset);
        control.add(buttonExit);
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2,1));
        southPanel.add(control);
        southPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * update the status of the board's DotButton instances based on the current game model
     */
    public void update(GameModel newGameModel){
    	gameModel = newGameModel;
    	
    	updateBoard();
        
        updateText();
        
        // repaint();
    }
    
    /**
     * Reset all buttons color according to the game model
     */
    private void updateBoard() {
    	// does this for the whole board (gameSize X gameSize)
    	for(int i = 0; i < gameModel.getSize(); i++){
            for(int j = 0; j < gameModel.getSize(); j++){
                board[i][j].setColor(gameModel.getColor(i,j));
            }
        }
    }
    
    /**
     * Reset the text area to the correct String according to the game model
     */
    private void updateText() {
    	int numberOfSteps = gameModel.getNumberOfSteps();
    	
    	if (numberOfSteps >= 0) {
    		scoreLabel.setText("Number of steps: " + gameModel.getNumberOfSteps());
    	} else {
    		scoreLabel.setText("Select initial dot");
    	}
    	
    }

}
