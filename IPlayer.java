/**
 * Interface to define requirements on the player
 */
public interface IPlayer {
	/**
	 * function to get the next move of the player
	 * @param state current state of game
	 * @return Integer move
	 */
	int move(Connect4Node state);
	/**
	 * function to  get the type of a player (as a String)
	 * @return String name
	 */
	String getName();
	/**
	 * function to get the cutted Tree metric
	 * @return integer metric
	 */
	int getCuttedTree();
	/**
	 * function to reset the cutted Tree metric
	 */
	void resetCuttedTree();
}
