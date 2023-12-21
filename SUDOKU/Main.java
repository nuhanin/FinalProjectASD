package SUDOKU;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * The main Sudoku program
 */
public class Main extends JFrame {
    private static final long serialVersionUID = 1L; // to prevent serial warning
    private GameBoardPanel board = new GameBoardPanel();
    private JButton btnNewGame = new JButton("New Game");
    private JLabel timerLabel = new JLabel("Time: 0 seconds");
    private JLabel playerLabel = new JLabel("Player: ");

    private Timer timer;
    private int seconds;
    private String playerName;

    // Constructor
    public Main() {
        // Ask for player name before starting the game
        playerName = JOptionPane.showInputDialog(this, "Enter Your Name:", "Player Name", JOptionPane.PLAIN_MESSAGE);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(board, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.add(timerLabel);
        cp.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnNewGame);
        cp.add(bottomPanel, BorderLayout.SOUTH);

        JLabel playerNameLabel = new JLabel("Player: " + playerName);
        bottomPanel.add(playerNameLabel);

        btnNewGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });

        // Initialize the game board to start the game
        board.newGame();
        initializeTimer();
        startTimer();

        pack(); // Pack the UI components, instead of using setSize()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to handle window-closing
        setTitle("Sudoku");
        setVisible(true);
    }

    private void initializeTimer() {
        seconds = 0;
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                seconds++;
                updateTimerLabel();
            }
        });
    }

    private void startTimer() {
        timer.start();
    }

    private void updateTimerLabel() {
        timerLabel.setText("Timer: " + seconds + " seconds");
    }

    private void newGame() {
        playerName = JOptionPane.showInputDialog(this, "Enter Your Name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
        playerLabel.setText("Player: " + playerName);
        seconds = 0;
        updateTimerLabel();
        board.newGame();
    }

    /** The entry main() entry method */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main(); // Let the constructor do the job
            }
        });
    }
}
