package TTT3;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO in one class
 */
public class TTTGraphics extends JFrame {
    private static final long serialVersionUID = 13L;

    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CELL_SIZE = 120;
    public static final int BOARD_WIDTH = CELL_SIZE * COLS;
    public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 10;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final int CELL_PADDING = CELL_SIZE / 5;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
    public static final int SYMBOL_STROKE_WIDTH = 8;
    public static final Color COLOR_BG = new Color(51, 153, 255);  // Light Blue
    public static final Color COLOR_BG_STATUS = new Color(255, 255, 153);  // Light Yellow
    public static final Color COLOR_GRID = Color.WHITE;  // White
    public static final Color COLOR_CROSS = new Color(255, 0, 0);  // Red
    public static final Color COLOR_NOUGHT = new Color(0, 128, 0);  // Green
    public static final Color COLOR_WIN_LINE = new Color(255, 255, 51);  // Yellow
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.BOLD, 14);

    // Add player names as instance variables
    private String playerXName;
    private String playerOName;

    // This enum (inner class) contains the various states of the game
    public enum State {
        PLAYING, DRAW, CROSS_WON, NOUGHT_WON
    }
    private State currentState;  // the current game state

    private int winLineStartX, winLineStartY, winLineEndX, winLineEndY;

    // This enum (inner class) is used for:
    // 1. Player: CROSS, NOUGHT
    // 2. Cell's content: CROSS, NOUGHT and NO_SEED
    public enum Seed {
        CROSS, NOUGHT, NO_SEED
    }
    private Seed currentPlayer; // the current player
    private Seed[][] board;     // Game board of ROWS-by-COLS cells

    // UI Components
    private GamePanel gamePanel; // Drawing canvas (JPanel) for the game board
    private JLabel statusBar;  // Status Bar

    private void getPlayerNames() {
        // Input dialog for player X's name
        playerXName = JOptionPane.showInputDialog(this, "Enter Player X's Name:", "Player X", JOptionPane.PLAIN_MESSAGE);

        // Input dialog for player O's name
        playerOName = JOptionPane.showInputDialog(this, "Enter Player O's Name:", "Player O", JOptionPane.PLAIN_MESSAGE);

        // If players don't enter names, set default names
        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Player X";
        }

        if (playerOName == null || playerOName.trim().isEmpty()) {
            playerOName = "Player O";
        }
    }

    /** Constructor to setup the game and the GUI components */
    public TTTGraphics() {
        // Initialize the game objects
        initGame();
        getPlayerNames();


        // Set up GUI components
        gamePanel = new GamePanel();  // Construct a drawing canvas (a JPanel)
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        // The canvas (JPanel) fires a MouseEvent upon mouse-click
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int row = mouseY / CELL_SIZE;
                int col = mouseX / CELL_SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < ROWS && col >= 0
                            && col < COLS && board[row][col] == Seed.NO_SEED) {
                        // Update board[][] and return the new game state after the move
                        currentState = stepGame(currentPlayer, row, col);
                        // Switch player
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                    }
                } else {       // game over
                    newGame(); // restart the game
                }
                // Refresh the drawing canvas
                repaint();  // Callback paintComponent()
                checkForWin();
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(COLOR_BG_STATUS);

        // Set up content pane
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();  // pack all the components in this JFrame
        setTitle("Tic Tac Toe");
        setVisible(true);  // show this JFrame

        newGame();
    }

    /** Initialize the Game (run once) */
    public void initGame() {
        board = new Seed[ROWS][COLS]; // allocate array
    }

    /** Reset the game-board contents and the status, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED; // all cells empty
            }
        }
        currentPlayer = Seed.CROSS;    // cross plays first
        currentState  = State.PLAYING; // ready to play
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        board[selectedRow][selectedCol] = player;

        if (board[selectedRow][0] == player
                && board[selectedRow][1] == player
                && board[selectedRow][2] == player
                || board[0][selectedCol] == player
                && board[1][selectedCol] == player
                && board[2][selectedCol] == player
                || selectedRow == selectedCol
                && board[0][0] == player
                && board[1][1] == player
                && board[2][2] == player
                || selectedRow + selectedCol == 2
                && board[0][2] == player
                && board[1][1] == player
                && board[2][0] == player) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (board[row][col] == Seed.NO_SEED) {
                        return State.PLAYING;
                    }
                }
            }
            return State.DRAW;
        }
    }

    public void checkForWin() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] != Seed.NO_SEED) {
                    if (checkWin(row, col)) {
                        currentState = (currentPlayer == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
                        repaint();
                        return;
                    }
                }
            }
        }

        if (currentState == State.PLAYING) {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (board[row][col] == Seed.NO_SEED) {
                        return;
                    }
                }
            }
            currentState = State.DRAW;
            repaint();
        }
    }

    private boolean checkWin(int row, int col) {
        if (col + 2 < COLS &&
                board[row][col] == board[row][col + 1] &&
                board[row][col] == board[row][col + 2]) {
            winLineStartX = col * CELL_SIZE;
            winLineStartY = row * CELL_SIZE + CELL_SIZE / 2;
            winLineEndX = (col + 2) * CELL_SIZE + CELL_SIZE;
            winLineEndY = row * CELL_SIZE + CELL_SIZE / 2;
            return true;
        }
        if (row + 2 < ROWS &&
                board[row][col] == board[row + 1][col] &&
                board[row][col] == board[row + 2][col]) {
            winLineStartX = col * CELL_SIZE + CELL_SIZE / 2;
            winLineStartY = row * CELL_SIZE;
            winLineEndX = col * CELL_SIZE + CELL_SIZE / 2;
            winLineEndY = (row + 2) * CELL_SIZE + CELL_SIZE;
            return true;
        }
        if (row + 2 < ROWS && col + 2 < COLS &&
                board[row][col] == board[row + 1][col + 1] &&
                board[row][col] == board[row + 2][col + 2]) {
            winLineStartX = col * CELL_SIZE;
            winLineStartY = row * CELL_SIZE;
            winLineEndX = (col + 2) * CELL_SIZE + CELL_SIZE;
            winLineEndY = (row + 2) * CELL_SIZE + CELL_SIZE;
            return true;
        }
        if (row - 2 >= 0 && col + 2 < COLS &&
                board[row][col] == board[row - 1][col + 1] &&
                board[row][col] == board[row - 2][col + 2]) {
            winLineStartX = col * CELL_SIZE;
            winLineStartY = row * CELL_SIZE + CELL_SIZE;
            winLineEndX = (col + 2) * CELL_SIZE + CELL_SIZE;
            winLineEndY = (row - 2) * CELL_SIZE;
            return true;
        }
        return false;
    }

    /**
     *  Inner class DrawCanvas (extends JPanel) used for custom graphics drawing.
     */
    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COLOR_BG);

            g.setColor(COLOR_GRID);
            for (int row = 1; row < ROWS; ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF,
                        BOARD_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH_HALF, 0,
                        GRID_WIDTH, BOARD_HEIGHT-1, GRID_WIDTH, GRID_WIDTH);
            }

            Graphics2D g2d = (Graphics2D)g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH));
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (board[row][col] == Seed.CROSS) {
                        g2d.setColor(COLOR_CROSS);
                        int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
                        int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
                        g2d.drawLine(x1, y1, x2, y2);
                        g2d.drawLine(x2, y1, x1, y2);
                    } else if (board[row][col] == Seed.NOUGHT) {
                        g2d.setColor(COLOR_NOUGHT);
                        g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                }
            }

            if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                g.setColor(COLOR_WIN_LINE);
                g.drawLine(winLineStartX, winLineStartY, winLineEndX, winLineEndY);
            }

            if (currentState == State.PLAYING) {
                statusBar.setForeground(Color.BLACK);
                statusBar.setText((currentPlayer == Seed.CROSS) ? playerXName + "'s Turn" : playerOName + "'s Turn");
            } else if (currentState == State.DRAW) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to play again");
            } else if (currentState == State.CROSS_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText(""+ playerXName + " Won! Click to play again");
            } else if (currentState == State.NOUGHT_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText(""+ playerOName + " Won! Click to play again");
            }
        }
    }

    /** The entry main() method */
    public static void main(String[] args) {
        // Run GUI codes in the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TTTGraphics(); // Let the constructor do the job
            }
        });
    }
}
