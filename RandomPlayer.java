import java.util.Random;

/**
 * class to implement a randomly playing player
 */
public class RandomPlayer implements IPlayer{
	Connect4Problem prob;
	int cuttedTree = 0;
	/**
	 * Constructor to generate a random player
	 * @param prob Connect4Problem
	 */
	public RandomPlayer( Connect4Problem prob) {
		this.prob = prob;
	}
	/**
	 * function to get the move of the player
	 * @return Integer move (1-7) which column
	 */
	public int move(Connect4Node state) {
		//generate number
		int input = new Random().nextInt(6) + 1;
		//if number is not valid, generate it again
		while (!prob.getMoves(state).contains(input)) {
			input = new Random().nextInt(6) + 1;
		}
		System.out.println(">>>" + input);
		return input;
	}
	/**
	 * function to get the type of player as a String
	 * @return String with the type of player
	 */
	public String getName() {
		return "Random";
	}
	/**
	 * function to get the count of cutted tree (is 0 for random player, since he is not using alpha-beta-pruning)
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
