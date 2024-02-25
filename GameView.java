import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView {
    private JFrame frame; // frame to display game's UI
    private JButton[][] buttons; // grid of buttons representing game board
    private JLabel statusLabel; // label to display the current player's turn or game status
    private GameController controller; // reference to the game controller to handle game logic

    // constructor that sets up game view with a reference to game controller
    public GameView(GameController controller) {
        this.controller = controller;
        initialize();
    }

    // initializes game window, setting up layout, game board, and status label
    private void initialize() {
        // set up main frame with title, default close operation, size, and layout
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new BorderLayout());

        /*
         * Create a panel that will hold the grid of game buttons
         * Set the layout of the board panel to a 3x3 grid
         * Intialize buttons array to store references to the button components
         */
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        // loop to create buttons, set button properties, and add buttons to board panel
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton(); // create new button without text
                button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60)); // set font of button to make it look better
                
                // record row and column indices for use in button's action listener
                final int row = i;
                final int col = j;

                // add action listener to handle clicks on this button
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.buttonClicked(row, col); // controller handles action
                    }
                });
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }

        // intialize status label with default message indicating it's X's turn, X will be user symbol
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER); // center label

        frame.add(boardPanel, BorderLayout.CENTER); // add board panel to center
        frame.add(statusLabel, BorderLayout.SOUTH); // add status label to bottom

        frame.setVisible(true); // make game window visible
    }

    // update the display of game board based on state of game
    public void updateBoard(char[][] board) {
        // iterate over each cell in 3x3 board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // set text of each button to reflect state (X, O, -)
                buttons[i][j].setText(String.valueOf(board[i][j]));
                // disable buttons that have been clicked as they now have an X or O
                // enable buttons that are still open (-)
                if (board[i][j] != '-') {
                    buttons[i][j].setEnabled(false);
                } else {
                    buttons[i][j].setEnabled(true);
                }
            }
        }
    }

    // update status label to indicate which player's turn it is
    public void setPlayerTurn(char playerMark) {
        statusLabel.setText("Player " + playerMark + "'s turn");
    }

    // displays dialog box indicating that the game has ended and which player has won
    public void showWinner(char playerMark) {
        JOptionPane.showMessageDialog(frame, "Player " + playerMark + " wins!");
    }

    // displays dialog box indicating game is a tie
    public void showTie() {
        JOptionPane.showMessageDialog(frame, "It's a tie!");
    }

    // resets game to its initial state, clear board and sets player to X
    public void resetGame() {
        // iterate over each cell in the 3x3 board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // reset each button's text to '-' and enable them
                buttons[i][j].setText("-");
                buttons[i][j].setEnabled(true);
            }
        }
        // reset status label to indicate it's player X's turn (user takes first turn)
        statusLabel.setText("Player X's turn");
    }
}
