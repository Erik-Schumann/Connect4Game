



Assignment Report 3: Implementation of the Alpha-Beta-Pruning Algorithm with Connect4
CSCI 485: Advanced Topics in General Computer Science: Foundations of AI
Erik Schumann
667072862 
____________________________________________________________________________________________________________________________________________________________________________
1.How to compile and execute your program? If your source code files need to be placed into subfolders in order to use the makefile, explain how the files should be organized.

In the beginning the following files should be in the folder:
 
AIPlayer.java		Connect4Node.java	HumanPlayer.java	README.txt		config.properties
Connect4Game.java	Connect4Problem.java	IPlayer.java		RandomPlayer.java

Get into the folder assignment1 using cmd and run following command to compile the program:

	>javac *.java

Now the following files should be there (.class files are the compiled ones):

AIPlayer.class		Connect4Game.class	Connect4Node.class	Connect4Problem.class	HumanPlayer.class	IPlayer.class		README.txt		RandomPlayer.java
AIPlayer.java		Connect4Game.java	Connect4Node.java	Connect4Problem.java	HumanPlayer.java	IPlayer.java		RandomPlayer.class	config.properties

Run following command to run the program:

	>java Connect4Game
	
To clean up the program, run the following command

	>rm *.class


____________________________________________________________________________________________________________________________________________________________________________	
2.Identify the sections of your code that implement the Minimax algorithm and Alpha-Beta Pruning algorithm respectively.

In line 42 of the AIPlayer.class the move() function calls the algorithm:
int input = alphaBetaSearch(state, prob);

Since in the constructor the AIPlayer already gets the information whether is the first player (important for the evaluation function), it does not need that information in the move () function
The alphaBetaSearch is fundamentally implemented as stated in class, except one difference:

	public int alphaBetaSearch(Connect4Node node, Connect4Problem prob) {} -> line 74 - 77
	public int MaxValue(Connect4Node node, int alpha, int beta, int depth, Connect4Problem prob) {} -> line  87 - 116
	public int MinValue(Connect4Node node, int alpha, int beta, int depth, Connect4Problem prob) {} -> line 126 - 155
	public Connect4Node FirstMaxValue(Connect4Node node, int alpha, int beta, int depth, Connect4Problem prob) {} -> line 165 - 201

-It gets the problem class as a parameter in order to get the successors
-It gets the search depth to control the depth of the search graph
-The first call of the MaxValue function (by alphaBetaSearch) is calling FirstMaxValue instead of MaxValue, because this function determinates the best successor node and its action
-This only needs to be done once, since after that the nodes are not important, so this implementation saves a lot of ressources


____________________________________________________________________________________________________________________________________________________________________________	
3.Identify how different Game Modi should be implemented

In the config.properties file there are parameters to configure:

depth = 3
#depth of search of the alpha-beta-pruning algorithm
player1 = 2
#Player type:
#0: You
#1: Random
#2: AI
player2 = 2
#Player type:
#0: You
#1: Random
#2: AI
rounds = 1
#rounds of game (set higher for automated games)
printAlpha = 0
#print alpha Value of first MaxValue function (indicator on how sure to win the AI is)
#1: print Alpha Value
#0: Do not print Alpha Value

____________________________________________________________________________________________________________________________________________________________________________
4.Explain how your Evaluation function is designed and why.

The evaluation function is implemented in the AIPlayer.java class from line 207 to 519.
The evaluation function does not need to check for 4s since this would already be watched by the TerminalTest-function.
It first checks for X:
-It stores the count of 3s in threeX
-It stores the count of 2s in twoX

1.It first checks for vertical 3s with an empty spot:
(X-X-X-" "), (X-X-" "-X), (X-" "-X-X) or (" "-X-X-X)
2.Then it checks for horizontal 3s with an empty spot: 
(" ")
( X )
( X )
( X )
3.Then it checks for the diagonal 3s with an empty spot (from left to right):

         ( )
      (X)
   (X)
(X)
         (X)
      ( )
   (X)
(X)
         (X)
      (X)
   ( )
(X)
         (X)
      (X)
   (X)
( )
4.Then it checks for the diagonal 3s with an empty spot (from right to left):

(X)
   (X)
      (X)
         ( )
(X)
   (X)
      ( )
         (X)
(X)
   ( )
      (X)
         (X)
( )
   (X)
      (X)
         (X)
5.It first checks for vertical 2s with 2 empty spots:
(X-X-" "-" "), (X-" "-" "-X), (" "-" "-X-X), (X-" "-X-" "), (" "-X-" "-X)  or (" "-X-X-" ")
6.Then it checks for horizontal ss with 2 empty spots: 
(" ")
(" ")
( X )
( X )
7.Then it checks for the diagonal 2s with an 2 spots(from left to right):

         ( )
      ( )
   (X)
(X)
         (X)
      ( )
   ( )
(X)
         (X)
      (X)
   ( )
( )
         ( )
      (X)
   (X)
( )
         (X)
      ( )
   (X)
( )
         ( )
      (X)
   ( )
(X)
8.Then it checks for the diagonal 2s with an empty spot (from right to left):

(X)
   (X)
      ( )
         ( )
(X)
   ( )
      ( )
         (X)
( )
   ( )
      (X)
         (X)
( )
   (X)
      (X)
         ( )
( )
   (X)
      ( )
         (X)
(X)
   ( )
      (X)
         ( )

It does the same checks for O:
-It stores the count of 3s in threeY
-It stores the count of 2s in twoY

In the end it calculates the value using the following formula:
10 * (threeX - threeY) + (twoX - twoY)

The evaluation function only handles the evaluating for X, for O the Agent is multiplying the result with -1 (isX variable).
____________________________________________________________________________________________________________________________________________________________________________
5.Any known bugs

None


____________________________________________________________________________________________________________________________________________________________________________
6.Any comment you would like the marker to know.

None












