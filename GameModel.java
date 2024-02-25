import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// GameModel handles state and logic of game
public class GameModel {
    private char[][] board; // board represented as 2D array
    private char currentPlayerMark; // current players mark 'X' or 'O'
    private boolean gameEnded; // flag to indicate if game has ended

    // constructor initializes game with an empty board and sets starting player as 'X' (user)
    public GameModel() {
        this.board = new char[3][3];
        this.currentPlayerMark = 'X';
        this.gameEnded = false;
        initializeBoard();
    }

    // initializes game board with '-' indicating neither player has made a move to place a mark there
    public void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    // check if board has no empty spaces left
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }  

    /*
     * Determines and executes player move
     * Utilizes minimax algorithm to find the best move
     * 20% of the time it will make a random move rather than the best move in order
     * to ensure computer doesn't always win
     */
    public void computerMove() {
        Random rand = new Random(); // create a random object
        boolean makeRandomMove = rand.nextInt(100) < 20; // 20% chance of a random move

        // if making a random move
        if (makeRandomMove) {
            // get list of all available moves (empty spots on board)
            List<int[]> availableMoves = getAvailableMoves();

            // if there are still available moves (board isn't full)
            if (!availableMoves.isEmpty()) {
                // select a random move from the list of available moves
                int[] move = availableMoves.get(rand.nextInt(availableMoves.size()));
                // place computer's mark ('O') on the selected board position (selected move)
                placeMark(move[0], move[1], 'O');
            }
        } 
        
        // if not making a random move, use minimax algorithm to find best move
        else {
            // initializes the bestVal (best value) with lowest possible value to ensure any actual score will be higher
            int bestVal = Integer.MIN_VALUE;
            // intializes bestMove to hold the row and column of the best move, which will be determined later
            int[] bestMove = {-1, -1};
    
            // iterate through all board positions
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // check if the current position is empty
                    if (board[i][j] == '-') {
                        // make a temp move at the current position
                        board[i][j] = 'O'; // comp move
                        // evaluate the move using minimax algorithm
                        int moveVal = minimax(false, 'X'); // eval users next move
                        board[i][j] = '-'; // undo move
    
                        // Update bestVal and bestMove with the best value move and the coordinates of that move on the grid  
                        if (moveVal > bestVal) {
                            bestMove[0] = i;
                            bestMove[1] = j;
                            bestVal = moveVal;
                        }
                    }
                }
            }

            // if a move was found make the move
            if (bestMove[0] != -1 && bestMove[1] != -1) {
                placeMark(bestMove[0], bestMove[1], 'O');
            }
        }
       changePlayer();
    }

    // returns a list of all empty positions on the board, these are available to mark
    public List<int[]> getAvailableMoves() {
        List<int[]> availableMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    availableMoves.add(new int[]{i, j});
                }
            }
        }
        return availableMoves;
    }

    // Evaluate board from computers perspective to determine the game state
    private int evaluateBoard() {
        if (checkForWin('O')) {
            return +10;
        }

        else if (checkForWin('X')) {
            return -10;
        }

        // no one has won yet
        return 0;
    }

    /*
     * minimax algorithm recursively determines the best move for the computer
     * simulates all possible moves in the gmae tree and evaluates the boards at leaf nodes
     */
    private int minimax(boolean isMaximizing, char playerSymbol) {
        // evaluate current board state, if a terminal state is reached (win, lose, tie),
        // return the evaluation score
        int score = evaluateBoard();

        // if the score isn't 0 (win or lose) or the board is full (tie), return the score
        // BASE CASE
        if (score != 0 ||  isBoardFull()) {
            return score;
        }

        // initalize bestScore to the worst possible scenario for both max and min player
        int bestScore;
        if(isMaximizing) {
            bestScore = Integer.MIN_VALUE; // for max player, worst value is the lowest possible score
        } else {
            bestScore = Integer.MAX_VALUE; // for min, worst value is the highest possible score
        }

        // iterate through all cells of board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // check if cell is empty and can be played
                if (board[i][j] == '-') {
                    // make a temp move on board with the current player's symbol
                    board[i][j] = playerSymbol;

                    // recursively call minimax for the next player (switching between max and min)
                    // simulates playing out the game after making the temp move
                    int currentScore;
                    if (playerSymbol == 'O') {
                        currentScore = minimax(!isMaximizing, 'X'); // if current player is 'O', next is 'X'
                    } else {
                        currentScore = minimax(!isMaximizing, 'O'); // if current player is 'X', next is 'O'
                    }

                    // undo temp move to backtrack and try other moves
                    board[i][j] = '-';

                    // update bestScore based on whether we are maximizing or minimizing
                    if(isMaximizing) {
                        bestScore = Math.max(bestScore, currentScore); // max player choses highest score
                    } else {
                        bestScore = Math.min(bestScore, currentScore); // min player choses lowest score
                    }
                }
            }
        }
        // return the best score after exploring all possible moves available from current board state
        return bestScore;
    }

    // checks if a win conditin is met for a given mark ('X' or 'O')
    public boolean checkForWin(char mark) {
        return (checkRowsForWin(mark) || checkColumnsForWin(mark) || checkDiagonalsForWin(mark));
    }

    // helper method to check if 3 given cells match a specific mark
    private boolean checkRowCol(char c1, char c2, char c3, char mark) {
        return ((c1 == mark) && (c1 == c2) && (c2 == c3));
    }

    // checks each row for a win condition for the given mark
    private boolean checkRowsForWin(char mark) {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[i][0], board[i][1], board[i][2], mark)) {
                return true;
            }
        }
        return false;
    }

    // checks each column for a win condition for the given mark
    private boolean checkColumnsForWin(char mark) {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[0][i], board[1][i], board[2][i], mark)) {
                return true;
            }
        }
        return false;
    }

    // checks both diagonals for a win condition for a given mark
    private boolean checkDiagonalsForWin(char mark) {
        return ((checkRowCol(board[0][0], board[1][1], board[2][2], mark)) || (checkRowCol(board[0][2], board[1][1], board[2][0], mark)));
    }

    // returns curernt player's mark
    public char getCurrentPlayerMark() {
        return currentPlayerMark;
    }

    // attempts to place a mark on the board at the provided row:col position
    public boolean placeMark(int row, int col, char mark) {
        if ((row >= 0) && (row < 3)) {
            if ((col >= 0) && (col < 3)) {
                if (board[row][col] == '-') {
                    board[row][col] = mark;
                    return true;
                }
            }
        }
        return false;
    }

    // changes current playre to the other player
    public void changePlayer() {
        if (currentPlayerMark == 'X') {
            currentPlayerMark = 'O';
        } else {
            currentPlayerMark = 'X';
        }
    }

    // returns current state of board
    public char[][] getBoard() {
        return board;
    }

    // checks if game ended
    public boolean isGameEnded() {
        return gameEnded;
    }

    // sets game ended flag to true or false
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }
}
