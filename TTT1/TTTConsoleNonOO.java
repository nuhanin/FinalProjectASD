package TTT1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class tictactoeConsoleNonOO {
    public static final int CROSS = 0; //jika
    public static final int NOUGHT = 1;
    public static final int NO_SEED = 2;

    public static final int ROWS = 3, COLS = 3;
    public static int[][] board = new int[ROWS][COLS];
    public static Queue<Integer> playerQueue = new LinkedList<>();

    public static final int PLAYING = 0;
    public static final int DRAW = 1;
    public static final int CROSS_WON = 2;
    public static final int NOUGHT_WON = 3;
    public static int currentState;

    public static Scanner in = new Scanner(System.in);
    public static void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = NO_SEED; // membuat board yang berisi nilai no_seed atau 0
            }
        }
        currentState = PLAYING; // deklarasi awal bahwa dimulai ketika currentstate nya 0
        playerQueue.add(NOUGHT); // pemain yang jalan "O" duluan dan dimasukkan ke queue
        playerQueue.add(CROSS);

    }

    public static void stepGame() { // untuk mendeklarasi giliran bermain
        boolean validInput = false;

        do {
            int currentPlayer = playerQueue.poll(); // untuk mengambil dan menampilkan nilai dari queue yang masuk
            System.out.print("Player '" + (currentPlayer == CROSS ? 'X' : 'O') +
                    "', enter your move (row[1-3] column[1-3]): "); // jika cross maka dimunculkan nilai X jika tidak maka O
            int row = in.nextInt() - 1; // jika dimasukkan nilai 1 maka akan
            int col = in.nextInt() - 1; // dikurangi 1 karena untuk dimasukkan ke index row/col ke 0

            if (row >= 0 && row < ROWS && col >= 0 && col < COLS
                    && board[row][col] == NO_SEED) {
                currentState = stepGameUpdate(currentPlayer, row, col);
                validInput = true;
            } else {
                System.out.println("This move at (" + (row + 1) + "," + (col + 1)
                        + ") is not valid. Try again...");
            }

            playerQueue.add(currentPlayer); // Return the player to the queue
        } while (!validInput);
    }

    
    public static int stepGameUpdate(int player, int selectedRow, int selectedCol)  { // menambahkan value kedalam cell cross atau nought dan mementukan status permainan
        board[selectedRow][selectedCol] = player;
        
        //menentukan status permainan playing,win,dan lose
        if (board[selectedRow][0] == player && board[selectedRow][1] == player &&
                board[selectedRow][2] == player ||
                board[0][selectedCol] == player && board[1][selectedCol] == player &&
                        board[2][selectedCol] == player ||
                selectedRow == selectedCol && board[0][0] == player && board[1][1] == player &&
                        board[2][2] == player ||
                selectedRow + selectedCol == 2 && board[0][2] == player &&
                        board[1][1] == player && board[2][0] == player) {
            return (player == CROSS) ? CROSS_WON : NOUGHT_WON;
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (board[row][col] == NO_SEED) {
                        return PLAYING;
                    }
                }
            }
            return DRAW;
        }
    }

    //membuat board tic tac toe
    public static void paintBoard() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                paintCell(board[row][col]);
                if (col != COLS - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (row != ROWS - 1) {
                System.out.println("-----------");
            }
        }
        System.out.println();
    }

    public static void paintCell(int content) { //mengganti value cell dengan icon cross atau noguht
        switch (content) {
            case CROSS:
                System.out.print(" X ");
                break;
            case NOUGHT:
                System.out.print(" O ");
                break;
            case NO_SEED:
                System.out.print("   ");
                break;
        }
    }
    public static void main(String[] args) {
        initGame(); // dipanggil untuk dimasukkan boardnya dengan currentstate = playing atau 0

        do {
            stepGame();
            paintBoard();
            if (currentState == CROSS_WON) {
                System.out.println("'X' won!\nBye!");
            } else if (currentState == NOUGHT_WON) {
                System.out.println("'O' won!\nBye!");
            } else if (currentState == DRAW) {
                System.out.println("It's a Draw!\nBye!");
            }
        } while (currentState == PLAYING); // dimulai dengan deklarasi awal currensate = playing atau 0
    }
}
