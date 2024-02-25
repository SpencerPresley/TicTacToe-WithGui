## tic-tac-toe game implementaion using a simple GUI and AI for computer moves

### Model (GameModel.java)
- Manages game state and logic.
    - game board
    - current player
    - game status
    - move validity
- Independent of user interface. Doesn't contain logic related to how game state is displayed or how input is received.  

### View (GameView.java)
- Handles graphics of game. Includes:
    - Game window
    - Game board buttons
    - Satus labels
- Designed to display game state to user. Views reads from the model to present current game status without modifying model directly.
- Informs controller about user actions (such as button clicks)

### Controller (GameController.java)
- Mediator between model and view.
- Processes user input from view, updates model, updates view to reflect new state
- Intializes the game by creating instances of both model and view.
- updates game state based on user interactions, changes view accordingly

### Computer Move AI
- Uses minimax algorithm to explore all possible game outcomes from different move combinations
- 20% of the time it will not use the minimax algorithm to determine the best move and will instead make a random move