import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * class to encode a Connect4 game
 */
public class Connect4Game {
	static int negInfinity = -1000;
	static int posInfinity = 1000;
	static long cuttedTree = 0;
	static int depth = 3;
	/**
	 * main function to start game
	 * @param args not used
	 */
	public static void main(String[] args) {
		int win = 0;
		int draw = 0;
		int loose = 0;
		int playertype1 = 0;
		int playertype2 = 2;
		int rounds = 1;
		int printAlpha = 1;
		//read file
		try (InputStream input = new FileInputStream(
			System.getProperty("user.dir") + "/config.properties")) {
			// create Properties object
			Properties prop = new Properties();
			// load a properties file
			prop.load(input);
			// get the property value of depth
			depth = Integer.valueOf(prop.getProperty("depth"));
			// get the property value of player 1
			playertype1 = Integer.valueOf(prop.getProperty("player1"));
			// get the property value of player 2 
			playertype2 = Integer.valueOf(prop.getProperty("player2"));
			// get the property value of rounds
			rounds = Integer.valueOf(prop.getProperty("rounds"));
			// get the property value of rounds
			printAlpha = Integer.valueOf(prop.getProperty("printAlpha"));
			System.out.println("");
		} catch (IOException ex) {
			//print error message
			System.out.println("Config not found, using default values");
			ex.printStackTrace();
		}
		//generate problem instance
		Connect4Problem prob = new Connect4Problem();
		//generate scanner
		Scanner sc = new Scanner(System.in);
		// create Player Instances
		IPlayer Player1 = null;
		if (playertype1 == 0) {
			Player1 = new HumanPlayer(sc, prob);

		} else if (playertype1 == 1) {
			Player1 = new RandomPlayer(prob);

		} else if (playertype1 == 2) {
			Player1 = new AIPlayer(prob, depth,-1,printAlpha);
		}
		IPlayer Player2 = null;
		if (playertype2 == 0) {
			Player2 = new HumanPlayer(sc, prob);

		} else if (playertype2 == 1) {
			Player2 = new RandomPlayer(prob);

		} else if (playertype2 == 2) {
			Player2 = new AIPlayer(prob, depth,1,printAlpha);
		}
		int input = 1;
		//start games with n rounds
		for (int i = 0; i < rounds; i++) {
			System.out.println("Start Game:");
			//create starting node
			Connect4Node currState = new Connect4Node();
			//as long as the node is not a end state
			//print state
			currState.printState();
			while (!TerminalTest(currState)) {
				//print info
				System.out.println("Move of Player 1 ("+Player1.getName()+"):[O]");
				//get move
				input = Player1.move(currState);
				//create successor node
				currState = new Connect4Node(currState, input, "O");
				//print successor node
				currState.printState();
				//test, if node is in a end state
				if (TerminalTest(currState)) {
					break;
				}
				//print info
				System.out.println("Move of Player 2 ("+Player2.getName()+"):[X]");
				//get move
				input = Player2.move(currState);
				//create successor node
				currState = new Connect4Node(currState, input, "X");
				//print state
				currState.printState();
			}
			//get who won, print info and increment metrics
			if (Utility(currState) == -100) {
				System.out.println("**************************");
				System.out.println("Player 1 (" + Player1.getName() + ") wins:");
				System.out.println("**************************");
				currState.printState();
				win++;
			} else if (Utility(currState) == 100) {
				System.out.println("**************************");
				System.out.println("Player 2 (" + Player2.getName() + ") wins:");
				currState.printState();
				System.out.println("**************************");
				loose++;
			} else {
				System.out.println("**************************");
				System.out.println("Draw!");
				currState.printState();
				System.out.println("**************************");
				draw++;
			}
			//get metric from both players and reset counter
			cuttedTree += Player1.getCuttedTree();
			Player1.resetCuttedTree();
			cuttedTree += Player2.getCuttedTree();
			Player2.resetCuttedTree();
		}
		sc.close();
		//print overall statistics
		System.out.println("");
		System.out.println("Overall Statistics: ");
		System.out.println("Player 1 (" + Player1.getName() + "): " + win);
		System.out.println("Player 2 (" + Player2.getName() + "): " + loose);
		System.out.println("Draw: " + draw);
		System.out.println("Cutted: " + cuttedTree);
	}
	/**
	 * function to test if node is in a end state
	 * @param node node which should be evaluated
	 * @return Boolean if node is in end state
	 */
	public static Boolean TerminalTest(Connect4Node node) {
		//get state
		String[][] board = node.getBoard();
		//check if board is completely full
		if ((!board[0][0].equals(" ")) && (!board[0][1].equals(" ")) && (!board[0][2].equals(" "))
				&& (!board[0][3].equals(" ")) && (!board[0][4].equals(" ")) && (!board[0][5].equals(" "))
				&& (!board[0][6].equals(" "))) {
			return true;
		}
		// check 4s for X
		// check horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals("X")) {
					return true;
				}
				if (board[countOne][countTwo].equals("X") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals("X")) {
					return true;
				}
			}
		}
		// check vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo].equals("X")
						&& board[countOne - 2][countTwo].equals("X") && board[countOne - 3][countTwo].equals("X")) {
					return true;
				}
			}
		}
		// check diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
						&& board[countOne - 2][countTwo + 2].equals("X")
						&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")) {
					return true;
				}
			}
		}
		// check diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
						&& board[countOne - 2][countTwo - 2].equals("X")
						&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")) {
					return true;
				}
			}
		}
		// check 4s for O
		// check horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals("O")) {
					return true;
				}
				if (board[countOne][countTwo].equals("O") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals("O")) {
					return true;
				}
			}
		}
		// check vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo].equals("O")
						&& board[countOne - 2][countTwo].equals("O") && board[countOne - 3][countTwo].equals("O")) {
					return true;
				}
			}
		}
		// check diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
						&& board[countOne - 2][countTwo + 2].equals("O")
						&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")) {
					return true;
				}
			}
		}
		// check diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
						&& board[countOne - 2][countTwo - 2].equals("O")
						&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")) {
					return true;
				}
			}
		}
		//if checks don't return a true, return false
		return false;

	}
	/**
	 * function to get the utility of a state
	 * @param node which should be evaluated
	 * @return Integer evaluation
	 */
	public static int Utility(Connect4Node node) {
		//get state
		String[][] board = node.getBoard();
		// check 4s for X
		// check horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals("X")) {
					return 100;
				}
				if (board[countOne][countTwo].equals("X") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals("X")) {
					return 100;
				}
			}
		}
		// check vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo].equals("X")
						&& board[countOne - 2][countTwo].equals("X") && board[countOne - 3][countTwo].equals("X")) {
					return 100;
				}
			}
		}
		// check diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
						&& board[countOne - 2][countTwo + 2].equals("X")
						&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")) {
					return 100;
				}
			}
		}
		// check diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
						&& board[countOne - 2][countTwo - 2].equals("X")
						&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")) {
					return 100;
				}
			}
		}
		// check 4s for O
		// check horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals("O")) {
					return -100;
				}
				if (board[countOne][countTwo].equals("O") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals("O")) {
					return -100;
				}
			}
		}
		// check vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo].equals("O")
						&& board[countOne - 2][countTwo].equals("O") && board[countOne - 3][countTwo].equals("O")) {
					return -100;
				}
			}
		}
		// check diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
						&& board[countOne - 2][countTwo + 2].equals("O")
						&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")) {
					return -100;
				}
			}
		}
		// check diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
						&& board[countOne - 2][countTwo - 2].equals("O")
						&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")) {
					return -100;
				}
			}
		}
		return 0;
	}

}
