import java.util.ArrayList;
import java.util.List;

/**
 * Problem class to get Problem specific functions
 */
public class Connect4Problem {
	/**
	 * Constructor to generate the Connect4 Problem
	 */ 
	public Connect4Problem() {
	}

	/**
	 * function to get the successors of a node
	 * 
	 * @param node current node
	 * @param sign the successor should have
	 * @return list of successor nodes
	 */
	public List<Connect4Node> getSuccessors(Connect4Node node, String sign) {
		// generate list of successors
		List<Connect4Node> successors = new ArrayList<Connect4Node>();
		// check if step is valid, if, generate new state and add it to the successors
		// list
		if (node.getBoard()[0][0].equals(" ")) {
			Connect4Node node1 = new Connect4Node(node, 1, sign);
			successors.add(node1);
		}
		// check if step is valid, if, generate new state and add it to the successors
		// list
		if (node.getBoard()[0][1].equals(" ")) {
			Connect4Node node2 = new Connect4Node(node, 2, sign);
			successors.add(node2);
		}
		// check if step is valid, if, generate new state and add it to the successors
		// list
		if (node.getBoard()[0][2].equals(" ")) {
			Connect4Node node3 = new Connect4Node(node, 3, sign);
			successors.add(node3);
		}
		// check if step is valid, if, generate new state and add it to the successors
		// list
		if (node.getBoard()[0][3].equals(" ")) {
			Connect4Node node4 = new Connect4Node(node, 4, sign);
			successors.add(node4);
		}
		// check if step is valid, if, generate new state and add it to the successors
		// list
		if (node.getBoard()[0][4].equals(" ")) {
			Connect4Node node5 = new Connect4Node(node, 5, sign);
			successors.add(node5);
		}
		// check if step is valid, if, generate new state and add it to the successors
		// list
		if (node.getBoard()[0][5].equals(" ")) {
			Connect4Node node6 = new Connect4Node(node, 6, sign);
			successors.add(node6);
		}
		// check if step is valid, if, generate new state and add it to the successors
		// list
		if (node.getBoard()[0][6].equals(" ")) {
			Connect4Node node7 = new Connect4Node(node, 7, sign);
			successors.add(node7);
		}
		// return list
		return successors;
	}

	/**
	 * function to get the potential moves of a node
	 * 
	 * @param node current node
	 * @return list of potential moves
	 */
	public List<Integer> getMoves(Connect4Node node) {
		// Generate list of potential successor moves
		List<Integer> successors = new ArrayList<Integer>();
		// check if step is valid, if, add moves to the successors list
		if (node.getBoard()[0][0].equals(" ")) {
			successors.add(1);
		}
		// check if step is valid, if, add moves to the successors list
		if (node.getBoard()[0][1].equals(" ")) {
			successors.add(2);
		}
		// check if step is valid, if, add moves to the successors list
		if (node.getBoard()[0][2].equals(" ")) {
			successors.add(3);
		}
		// check if step is valid, if, add moves to the successors list
		if (node.getBoard()[0][3].equals(" ")) {
			successors.add(4);
		}
		// check if step is valid, if, add moves to the successors list
		if (node.getBoard()[0][4].equals(" ")) {
			successors.add(5);
		}
		// check if step is valid, if, add moves to the successors list
		if (node.getBoard()[0][5].equals(" ")) {
			successors.add(6);
		}
		// check if step is valid, if, add moves to the successors list
		if (node.getBoard()[0][6].equals(" ")) {
			successors.add(7);
		}
		// return list of possible moves
		return successors;
	}
}
