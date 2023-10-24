/**
 * class to encode an AI player
 */
public class AIPlayer implements IPlayer{
	Connect4Problem prob;
	static int negInfinity = -1000;
	static int posInfinity = 1000;
	int cuttedTree = 0;
	int depth = 6;
	int isX=1;
	String sign;
	String signOpponent;
	int printAlpha=0;
	/**
	 * Constructor to generate an AI player
	 * @param prob problem instance of a Connect4 Game
	 * @param depth search depth
	 * @param isX value, if the player plays X (1) or O (-1)
	 * @param printAlpha info, whether the alpha value should be printed
	 */
	public AIPlayer( Connect4Problem prob, int depth, int isX, int printAlpha) {
		this.prob = prob;
		this.depth = depth;
		this.isX = isX;
		//set signs
		if(isX==1) {
			sign = "X";
			signOpponent= "O";
		}
		else {
			sign = "O";
			signOpponent="X";
		}
		this.printAlpha= printAlpha;
	}
	/**
	 * function to get the move calculated by the alpha-beta-pruning search
	 * @return Integer the calculated move
	 */
	public int move(Connect4Node state) {
		//get move
		int input = alphaBetaSearch(state, prob);
		//print move
		System.out.println(">>>"+input);
		//return input
		return input;
	}
	/**
	 * function to get the type of player as a String
	 * @return String with the type of player
	 */
	public String getName() {
		return "AI";
	}
	/**
	 * function to get the count of cutted tree 
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
	/**
	 * function to get the alpha beta pruning algorithm calculated move
	 * @param node current node
	 * @param prob Connect4 problem instance
	 * @return calculated move
	 */
	public int alphaBetaSearch(Connect4Node node, Connect4Problem prob) {
		int desirableMove = FirstMaxValue(node, negInfinity, posInfinity, depth, prob).getAction();
		return desirableMove;
	}
	/**
	 * MaxValue function of the alpha-beta-pruning algorithm
	 * @param node current node
	 * @param alpha alpha value
	 * @param beta beta value
	 * @param depth search depth
	 * @param prob Connect4 problem instance
	 * @return Max Value
	 */
	public int MaxValue(Connect4Node node, int alpha, int beta, int depth, Connect4Problem prob) {
		//check if node is in end state
		if (Connect4Game.TerminalTest(node)) {
			//get utility
			return isX*Connect4Game.Utility(node);
		}
		//check if search depth is exceeded
		if (depth == 0) {
			//get evaluation
			return isX*Evaluation(node);
		}
		//set v
		int v = negInfinity;
		//for each successor node
		for (Connect4Node nod : prob.getSuccessors(node, sign)) {
			//get minVal of function
			int minVal = MinValue(nod, alpha, beta, depth, prob);
			//get Max of v and minVal
			v = getMax(v, minVal);
			//if v is bigger than beta, cut tree
			if (v >= beta) {
				cuttedTree++;
				return v;
			}
			//set alpha
			alpha = getMin(alpha, v);
		}
		//return v
		return v;
	}
	/**
	 * MinValue function of the alpha-beta-pruning algorithm
	 * @param node current node
	 * @param alpha alpha value
	 * @param beta beta value
	 * @param depth search depth
	 * @param prob Connect4 problem instance
	 * @return Min Value
	 */
	public int MinValue(Connect4Node node, int alpha, int beta, int depth, Connect4Problem prob) {
		//check if node is in end state
		if (Connect4Game.TerminalTest(node)) {
			//get utility
			return isX*Connect4Game.Utility(node);
		}
		//check if search depth is exceeded
		if (depth == 0) {
			//get evaluation
			return isX*Evaluation(node);
		}
		//set v
		int v = posInfinity;
		//for each successor node
		for (Connect4Node nod : prob.getSuccessors(node, signOpponent)) {
			//get minVal of function
			int maxVal = MaxValue(nod, alpha, beta, depth - 1, prob);
			//get Max of v and minVal
			v = getMin(v, maxVal);
			//if v is smaller than alpha, cut tree
			if (v < alpha) {
				cuttedTree++;
				return v;
			}
			//set beta
			beta = getMin(beta, v);
		}
		//return v
		return v;
	}
	/**
	 * MaxValue start function of the alpha-beta-pruning algorithm
	 * @param node current node
	 * @param alpha alpha value
	 * @param beta beta value
	 * @param depth search depth
	 * @param prob Connect4 problem instance
	 * @return best successor node
	 */
	public Connect4Node FirstMaxValue(Connect4Node node, int alpha, int beta, int depth, Connect4Problem prob) {
		//check if node is in end state
		if (Connect4Game.TerminalTest(node)) {
			//return node
			return node;
		}
		//set best node
		Connect4Node bestNode = node;
		//set v
		int v = negInfinity;
		//for each successor node
		for (Connect4Node nod : prob.getSuccessors(node, sign)) {
			//get minVal of function
			int val = MinValue(nod, alpha, beta, depth, prob);
			//set Max of v and val, if val>v also set best node
			if (val > v) {
				bestNode = nod;
				v = val;
			}
			//set Max of v and val, if val==v and if move is more in the middle (better starting move) set node
			if (val == v && Math.abs(bestNode.getAction() - 4) > Math.abs(nod.getAction() - 4)) {
				bestNode = nod;
				v = val;
			}
			//set v (max of val and v)
			v = getMax(val, v);
			//set alpha
			alpha = getMax(alpha, v);
			// System.out.println("val: " + val + "(of: " + nod.getAction() + ")");
		}
		//if printAlpha is true, print alpha value
		if(printAlpha==1) {
			System.out.println("Alpha: " + alpha);
		}
		//return bestNode
		return bestNode;
	}
	/**
	 * function to evaluate a node
	 * @param node which should be evaluated
	 * @return Integer evaluation
	 */
	public int Evaluation(Connect4Node node) {
		//get state
		String[][] board = node.getBoard();
		// count the 3s for X
		int threeX = 0;
		// count horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals(" ")) {
					threeX++;
				}
				if (board[countOne][countTwo].equals(" ") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals("X")) {
					threeX++;
				}
			}
		}
		// count vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo].equals("X")
						&& board[countOne - 2][countTwo].equals("X") && board[countOne - 3][countTwo].equals(" ")) {
					threeX++;
				}
			}
		}
		// count diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
						&& board[countOne - 2][countTwo + 2].equals("X")
						&& board[countOne - 3][countTwo + 3].equals(" ")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals(" ")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")) {
					threeX++;
				}
			}
		}
		// count diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
						&& board[countOne - 2][countTwo - 2].equals("X")
						&& board[countOne - 3][countTwo - 3].equals(" ")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals(" ")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")) {
					threeX++;
				}
			}
		}
		// count the 3s for O
		int threeY = 0;
		// count horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals(" ")) {
					threeY++;
				}
				if (board[countOne][countTwo].equals(" ") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals("O")) {
					threeY++;
				}
			}
		}
		// count vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo].equals("O")
						&& board[countOne - 2][countTwo].equals("O") && board[countOne - 3][countTwo].equals(" ")) {
					threeY++;
				}
			}
		}
		// count diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
						&& board[countOne - 2][countTwo + 2].equals("O")
						&& board[countOne - 3][countTwo + 3].equals(" ")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals(" ")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")) {
					threeY++;
				}
			}
		}
		// count diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
						&& board[countOne - 2][countTwo - 2].equals("O")
						&& board[countOne - 3][countTwo - 3].equals(" ")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals(" ")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")) {
					threeY++;
				}
			}
		}
		// count the 2s for X
		int twoX = 0;
		// count horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals(" ") && board[countOne][countTwo + 3].equals(" ")) {
					twoX++;
				}
				if (board[countOne][countTwo].equals(" ") && board[countOne][countTwo + 1].equals(" ")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals("X")) {
					twoX++;
				}
				if (board[countOne][countTwo].equals(" ") && board[countOne][countTwo + 1].equals("X")
						&& board[countOne][countTwo + 2].equals(" ") && board[countOne][countTwo + 3].equals("X")) {
					twoX++;
				}
				if (board[countOne][countTwo].equals("X") && board[countOne][countTwo + 1].equals(" ")
						&& board[countOne][countTwo + 2].equals("X") && board[countOne][countTwo + 3].equals(" ")) {
					twoX++;
				}
			}
		}
		// count vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo].equals("X")
						&& board[countOne - 2][countTwo].equals(" ") && board[countOne - 3][countTwo].equals(" ")) {
					twoX++;
				}
			}
		}
		// count diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals("X")
						&& board[countOne - 2][countTwo + 2].equals(" ")
						&& board[countOne - 3][countTwo + 3].equals(" ") || // 1100
						board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals(" ")
						|| // 1010
						board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals(" ")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| // 1001
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals(" ")
								&& board[countOne - 3][countTwo + 3].equals("X")
						|| // 0101
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals("X")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals(" ")
						|| // 0110
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals("X")
								&& board[countOne - 3][countTwo + 3].equals("X")// 0011
				) {
					twoX++;
				}
			}
		}
		// count diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals("X")
						&& board[countOne - 2][countTwo - 2].equals(" ")
						&& board[countOne - 3][countTwo - 3].equals(" ") || // 1100
						board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals(" ")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| // 1001
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals("X")
						|| // 0011
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals(" ")
						|| // 0110
						board[countOne][countTwo].equals("X") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals("X")
								&& board[countOne - 3][countTwo - 3].equals(" ")
						|| // 1010
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals("X")
								&& board[countOne - 2][countTwo - 2].equals(" ")
								&& board[countOne - 3][countTwo - 3].equals("X")// 0101
				) {
					twoX++;
				}
			}
		}
		// count the 2s for Y
		int twoY = 0;
		// count horizontally
		for (int countOne = 0; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals(" ") && board[countOne][countTwo + 3].equals(" ")) {
					twoY++;
				}
				if (board[countOne][countTwo].equals(" ") && board[countOne][countTwo + 1].equals(" ")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals("O")) {
					twoY++;
				}
				if (board[countOne][countTwo].equals(" ") && board[countOne][countTwo + 1].equals("O")
						&& board[countOne][countTwo + 2].equals(" ") && board[countOne][countTwo + 3].equals("O")) {
					twoY++;
				}
				if (board[countOne][countTwo].equals("O") && board[countOne][countTwo + 1].equals(" ")
						&& board[countOne][countTwo + 2].equals("O") && board[countOne][countTwo + 3].equals(" ")) {
					twoY++;
				}
			}
		}
		// count vertically
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo].equals("O")
						&& board[countOne - 2][countTwo].equals(" ") && board[countOne - 3][countTwo].equals(" ")) {
					twoY++;
				}
			}
		}
		// count diagonal to right
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 0; countTwo < board[countOne].length - 3; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals("O")
						&& board[countOne - 2][countTwo + 2].equals(" ")
						&& board[countOne - 3][countTwo + 3].equals(" ") || // 1100
						board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals(" ")
						|| // 1010
						board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals(" ")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| // 1001
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals(" ")
								&& board[countOne - 3][countTwo + 3].equals("O")
						|| // 0101
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals("O")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals(" ")
						|| // 0110
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo + 1].equals(" ")
								&& board[countOne - 2][countTwo + 2].equals("O")
								&& board[countOne - 3][countTwo + 3].equals("O")// 0011
				) {
					twoY++;
				}
			}
		}
		// count diagonal to left
		for (int countOne = 3; countOne < board.length; countOne++) {
			for (int countTwo = 3; countTwo < board[countOne].length; countTwo++) {
				if (board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals("O")
						&& board[countOne - 2][countTwo - 2].equals(" ")
						&& board[countOne - 3][countTwo - 3].equals(" ") || // 1100
						board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals(" ")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| // 1001
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals("O")
						|| // 0011
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals(" ")
						|| // 0110
						board[countOne][countTwo].equals("O") && board[countOne - 1][countTwo - 1].equals(" ")
								&& board[countOne - 2][countTwo - 2].equals("O")
								&& board[countOne - 3][countTwo - 3].equals(" ")
						|| // 1010
						board[countOne][countTwo].equals(" ") && board[countOne - 1][countTwo - 1].equals("O")
								&& board[countOne - 2][countTwo - 2].equals(" ")
								&& board[countOne - 3][countTwo - 3].equals("O")// 0101
				) {
					twoY++;
				}
			}
		}
		return 10 * (threeX - threeY) + (twoX - twoY);
	}
	/**
	 * get Max number of a and b
	 * @param a number1
	 * @param b number2
	 * @return return value of max number
	 */
	static int getMax(int a, int b) {
		if (a > b) {
			return a;
		} else if (b > a) {
			return b;
		} else {
			return a;
		}
	}
	/**
	 * get Min number of a and b
	 * @param a number1
	 * @param b number2
	 * @return value of the minimun number
	 */
	static int getMin(int a, int b) {
		if (a < b) {
			return a;
		} else if (b < a) {
			return b;
		} else {
			return a;
		}
	}
}
