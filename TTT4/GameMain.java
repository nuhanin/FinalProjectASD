package TTT4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameMain extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final String TITLE = "Tic-Tac-Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);
    public static final Color COLOR_WIN_LINE = Color.RED;


    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private int winLineStartX, winLineStartY, winLineEndX, winLineEndY;

    public GameMain() {
            super.addMouseListener(new MouseAdapter() {
                  @Override
      public void mouseClicked(MouseEvent e) {
         int mouseX = e.getX();
         int mouseY = e.getY();
         int row = mouseY / Cell.SIZE;
         int col = mouseX / Cell.SIZE;

         if (currentState == State.PLAYING) {
            if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS
                     && board.cells[row][col].content == Seed.NO_SEED) {
                  currentState = board.stepGame(currentPlayer, row, col);
                  currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                  // Check for win after each move
                  checkForWin();
            }
         } else {
            newGame();
         }
         repaint();
      }

        });

        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END);
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        initGame();
        newGame();
        getPlayerNames();
    }

    public void initGame() {
        board = new Board();
    }

    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    private String playerXName;
    private String playerOName;

    private void getPlayerNames() {
        playerXName = JOptionPane.showInputDialog(this, "Enter Player X's Name:", "Player X", JOptionPane.PLAIN_MESSAGE);
        playerOName = JOptionPane.showInputDialog(this, "Enter Player O's Name:", "Player O", JOptionPane.PLAIN_MESSAGE);

        if (playerXName == null || playerXName.trim().isEmpty()) {
            playerXName = "Player X";
        }

        if (playerOName == null || playerOName.trim().isEmpty()) {
            playerOName = "Player O";
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);

        board.paint(g);

        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? playerXName + "'s Turn" : playerOName + "'s Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerXName + " Won! Click to play again");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(playerOName + " Won! Click to play again");
        }

        // Draw the win line if applicable
        if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
            g.setColor(COLOR_WIN_LINE);
            g.drawLine(winLineStartX, winLineStartY, winLineEndX, winLineEndY);
        }
    }

    public void checkForWin() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                if (board.cells[row][col].content != Seed.NO_SEED) {
                    if (checkWin(row, col)) {
                        currentState = (currentPlayer == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
                        repaint();
                        return;
                    }
                }
            }
        }

        if (currentState == State.PLAYING) {
            for (int row = 0; row < Board.ROWS; ++row) {
                for (int col = 0; col < Board.COLS; ++col) {
                    if (board.cells[row][col].content == Seed.NO_SEED) {
                        return;
                    }
                }
            }
            currentState = State.DRAW;
            repaint();
        }
    }

    private boolean checkWin(int row, int col) {
        if (col + 2 < Board.COLS &&
                board.cells[row][col].content == board.cells[row][col + 1].content &&
                board.cells[row][col].content == board.cells[row][col + 2].content) {
            winLineStartX = col * Cell.SIZE;
            winLineStartY = row * Cell.SIZE + Cell.SIZE / 2;
            winLineEndX = (col + 2) * Cell.SIZE + Cell.SIZE;
            winLineEndY = row * Cell.SIZE + Cell.SIZE / 2;
            return true;
        }
        if (row + 2 < Board.ROWS &&
                board.cells[row][col].content == board.cells[row + 1][col].content &&
                board.cells[row][col].content == board.cells[row + 2][col].content) {
            winLineStartX = col * Cell.SIZE + Cell.SIZE / 2;
            winLineStartY = row * Cell.SIZE;
            winLineEndX = col * Cell.SIZE + Cell.SIZE / 2;
            winLineEndY = (row + 2) * Cell.SIZE + Cell.SIZE;
            return true;
        }
        if (row + 2 < Board.ROWS && col + 2 < Board.COLS &&
                board.cells[row][col].content == board.cells[row + 1][col + 1].content &&
                board.cells[row][col].content == board.cells[row + 2][col + 2].content) {
            winLineStartX = col * Cell.SIZE;
            winLineStartY = row * Cell.SIZE;
            winLineEndX = (col + 2) * Cell.SIZE + Cell.SIZE;
            winLineEndY = (row + 2) * Cell.SIZE + Cell.SIZE;
            return true;
        }
        if (row - 2 >= 0 && col + 2 < Board.COLS &&
                board.cells[row][col].content == board.cells[row - 1][col + 1].content &&
                board.cells[row][col].content == board.cells[row - 2][col + 2].content) {
            winLineStartX = col * Cell.SIZE + Cell.SIZE;
            winLineStartY = row * Cell.SIZE;
            winLineEndX = (col + 2) * Cell.SIZE;
            winLineEndY = (row - 2) * Cell.SIZE + Cell.SIZE;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}

           
