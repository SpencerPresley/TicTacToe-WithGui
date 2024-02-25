import javax.swing.JOptionPane;
import javax.swing.*;

// game controller is a mediator between GameModel (logic) and GameView (UI)
// it handles user interactions and updates game state accordingly
public class GameController {
    private GameModel model; // reference to game's model
    private GameView view; // reference to game's view

    // constructor intializes the game by creating the model and view and updating board display
    public GameController() {
        model = new GameModel();
        view = new GameView(this);
        view.updateBoard(model.getBoard());
    }

    // called when a button on the game board is clicked
    // attempts to place mark on the board and process the move if the space is available
    public void buttonClicked(int row, int col) {
        if (model.placeMark(row, col, model.getCurrentPlayerMark())) {
            model.changePlayer();
            processMove();
        }
    }

    /*
     * process a move by updating the board
     * checks for a win or tie
     * continues the game if not in a win or tie state
     */
    public void processMove() {
        view.updateBoard(model.getBoard()); // update board view to match current state

        // if it's the computers turn execute the computer's move and update the board state
        if (model.getCurrentPlayerMark() == 'O') {
            model.computerMove();
            view.updateBoard(model.getBoard());
        }

        // check win conditions for X, check win conditions for O, check for tie
        // for X and O show the appropriate winner
        // for tie show it's a tie
        // for all: prompt the user to ask if they want to play again
        if (model.checkForWin('X')) {
            view.showWinner('X');
            model.setGameEnded(true);
            promptForNewGame();
        } else if (model.checkForWin('O')) {
            view.showWinner('O');
            model.setGameEnded(true);
            promptForNewGame();
        } else if (model.isBoardFull()) {
            view.showTie();
            promptForNewGame();
        }
    }

    // prompts user with a dialog to ask if they'd like to play again or exit the game
    // if they want to play again it resets the game
    // if they wish to exit the game exits
    private void promptForNewGame() {
        SwingUtilities.invokeLater(() -> {
            int playAgainResponse = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (playAgainResponse == JOptionPane.YES_NO_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        });
    }

    // resets game to inital state by reinitializing model and view and clearing board
    private void resetGame() {
        //Thread.currentThread().interrupt();

        model = new GameModel();
        view.resetGame();
        view.updateBoard(model.getBoard());
    }

    // main method tos tart the game
    public static void main(String[] args) {
        new GameController();
    }
}
