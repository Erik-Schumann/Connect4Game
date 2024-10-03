import java.util.Arrays;

/**
 * class to encode the state of a connect4 state
 */
public class Connect4Node {
	String[][] board = null;
	int move; 
	/**
	 * Constructor to generate node with empty board
	 */
	public Connect4Node() {
		// create board
		board = new String[6][7];
		// initialize board
		initializeState();
	}
	/**
	 * Constructor to generate a successor node based on current node and move +
	 * sign
	 * 
	 * @param state current state (predecessor node)
	 * @param move  made move
	 * @param sign  sign of the move
	 */
	public Connect4Node(Connect4Node state, int move, String sign) {
		this.move = move;
		setBoard(copy(state.getBoard()));
		set(move, sign);
	}
	/**
	 * function to copy string matrix
	 * 
	 * @param src string matrix to copy
	 * @return copied matrix
	 */
	public static String[][] copy(String[][] src) {
		// source:
		// https://www.techiedelight.com/create-copy-of-2d-array-java/#:~:text=A%20simple%20solution%20is%20to,method%20to%20copy%20each%20row.
		// check for nullpointer
		if (src == null) {
			return null;
		}
		// return copied array
		return Arrays.stream(src).map(String[]::clone).toArray(String[][]::new);
	}
	/**
	 * function to get the move made to get this node
	 * 
	 * @return move
	 */
	public int getAction() {
		return move;
	}
	/**
	 * function to initialize the state
	 */
	public void initializeState() {
		// iterate through every element and set " "
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = " ";
			}
		}
	}
	/**
	 * function to get the state of a node
	 * @return state
	 */
	public String[][] getBoard() {
		return board;
	}
	/**
	 * function to set the state of a node
	 * @param newBoard new State
	 */
	public void setBoard(String[][] newBoard) {
		this.board = newBoard;
	}
	/**
	 * function to print the state of a node
	 */
	public void printState() {
		// iterate through every element
		for (int i = 0; i < board.length; i++) {
			// create horizontal border
			System.out.println("|-||-||-||-||-||-||-|");
			for (int j = 0; j < board[i].length; j++) {
				// print element & vertical border
				System.out.print("|" + board[i][j] + "|");
			}
			// switch lines
			System.out.println();
		}
		// print button border
		System.out.println("|-||-||-||-||-||-||-|");
		System.out.println("|1||2||3||4||5||6||7|");
	}
	/**
	 * function to set the sign in the specific column (do a move)
	 * 
	 * @param column in which the marble should be put it
	 * @param sign   of the marble
	 */
	public void set(int column, String sign) {
		// normalize column to board (1-7 to 0-6)
		column--;
		// get the right row (last empty row)
		if (column < 7 && column >= 0) {
			int place = 7;
			for (int r = 0; r <= 5; r++) {
				if (board[r][column].equals(" ")) {
					place = r;
				}
			}
			// set sign
			board[place][column] = sign + "";
		}
	}
}
