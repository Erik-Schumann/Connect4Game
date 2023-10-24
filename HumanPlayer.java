import java.util.Scanner;

/**
 * class to implement a human player
 */
public class HumanPlayer implements IPlayer{
	Scanner sc;
	Connect4Problem prob;
	int cuttedTree = 0;
	/**
	 * Constructor to generate a human player
	 * @param sc Scanner object
	 * @param prob Connect4Problem
	 */
	public HumanPlayer(Scanner sc, Connect4Problem prob) {
		this.sc = sc;
		this.prob = prob;
	}
	/**
	 * function to get the move of the player
	 * @return Integer move (1-7) which column
	 */
	public int move(Connect4Node state) {
		System.out.println(">>>");
		//get Input of User
		int input=0;
		try {
			input = sc.nextInt();
		}
		catch(Exception e){
			input = 0;
		}
		//if input is invalid, get input again (as long as its invalid)
		while ((input < 1 || input > 7) || (!prob.getMoves(state).contains(input))) {
			System.out.println("Invalid Input (select valid number between 1 and 7");
			System.out.print(">>>");
			try {
				input = sc.nextInt();
			}
			catch(Exception e){
				input = 0;
			}
		}
		return input;
	}
	/**
	 * function to get the type of player as a String
	 * @return String with the type of player
	 */
	public String getName() {
		return "Human";
	}
	/**
	 * function to get the count of cutted tree (is 0 for human player, since the player is not using alpha-beta-pruning)
	 * @return Integer count
	 */
	public int getCuttedTree() {
		return cuttedTree;
	}
	/**
	 * function to reset the count of cutted tree
	 */
	public void resetCuttedTree() {
		cuttedTree = 0;
	}
}
