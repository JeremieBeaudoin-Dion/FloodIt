import java.io.*;

/**
 * The class <b>FloodIt</b> launches the game
 *
 * @author Guy-Vincent Jourdan, University of Ottawa
 * @author Jérémie Beaudoin-Dion
 */
public class FloodIt {


   /**
     * <b>main</b> of the application. Creates the instance of  GameController 
     * and starts the game. If a game size (>10) is passed as parameter, it is 
     * used as the board size. Otherwise, a default value is passed
     * 
     * @param args
     *            command line parameters
     */
     public static void main(String[] args) {
    	StudentInfo.display();
    	 
		// The size of the game can be passed as an argument. Handles invalid argument
        int size = 10;
        if (args.length == 1) {
            try{
                size = Integer.parseInt(args[0]);
                if(size<10){
                    System.out.println("Invalid argument, using default...");
                }
            } catch(NumberFormatException e){
                System.out.println("Invalid argument, using default...");
            }
        }
		
		// Sets up the MVC and starts the game
        GameController game = new GameController(size);
        
    }


}
