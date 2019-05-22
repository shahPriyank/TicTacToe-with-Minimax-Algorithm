import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    enum Turn {
        HUMAN, COMPUTER
    }

    public static int GRID_SIZE = 3;
    public static char[][] TIC_TAC_TOE_BOARD = new char[GRID_SIZE][GRID_SIZE];
    public static char HUMAN_SYMBOL = 'O';
    public static char COMPUTER_SYMBOL = 'X';

    public static void main(String[] args) {
        printNumbers();

        Turn turn = whoStartsTheGame();

        initializeBoard();
        System.out.println("Initial Board State");
        printBoard();

        System.out.println(turn.toString() + " will start the game");
        playGame(turn,1);

    }

    private static void playGame(Turn turn, int moves) {
        if (moves>9) {
            System.out.println("DRAW");
            return;
        }

        if (moves>5) {
            int score = evaluateIfWinning(TIC_TAC_TOE_BOARD, 0);
            if (score > 0) {
                System.out.println("COMPUTER WINS");
                return;
            } else if (score < 0) {
                System.out.println("HUMAN WINS");
                return;
            }
        }
        
        if (turn.toString().equals("HUMAN")) {
            enterOAtPosition();
            printBoard();
            playGame(Turn.COMPUTER, moves+1);
        } else {
            enterXAtPosition();
            printBoard();
            playGame(Turn.HUMAN, moves+1);
        }
    }

    private static int evaluateIfWinning(char[][] tempBoard, int depth) {
        for (int i=0;i<3;i++){
            if (tempBoard[i][0] == tempBoard[i][1] &&
                    tempBoard[i][1] == tempBoard[i][2]) {
                if (tempBoard[i][0] == COMPUTER_SYMBOL) {
                    return 10-depth;
                } else if (tempBoard[i][0] == HUMAN_SYMBOL){
                    return depth-10;
                }
            }
        }

        for (int i=0;i<3;i++){
            if (tempBoard[0][i] == tempBoard[1][i] &&
                    tempBoard[1][i] == tempBoard[2][i]) {
                if (tempBoard[0][i] == COMPUTER_SYMBOL) {
                    return 10-depth;
                } else if (tempBoard[0][i] == HUMAN_SYMBOL){
                    return depth-10;
                }
            }
        }

        if (tempBoard[0][0] == tempBoard[1][1] &&
                tempBoard[1][1] == tempBoard[2][2]) {
            if (tempBoard[0][0] == COMPUTER_SYMBOL) {
                return 10-depth;
            } else if (tempBoard[0][0] == HUMAN_SYMBOL) {
                return depth-10;
            }
        }

        if (tempBoard[0][2] == tempBoard[1][1] &&
                tempBoard[1][1] == tempBoard[2][0]) {
            if (tempBoard[0][2] == COMPUTER_SYMBOL) {
                return 10-depth;
            } else if (tempBoard[0][2] == HUMAN_SYMBOL) {
                return depth-10;
            }
        }
        return 0;
    }

    private static void enterXAtPosition() {
        int bestValue = Integer.MIN_VALUE;
        int bestX = -1;
        int bestY = -1;
        char[][] tempBoard = Arrays.stream(TIC_TAC_TOE_BOARD).map(
                                r->r.clone()).toArray(char[][]::new);

        for (int i=0;i<GRID_SIZE;i++) {
            for (int j=0;j<GRID_SIZE;j++) {
                if (tempBoard[i][j] == '_') {
                    tempBoard[i][j] = COMPUTER_SYMBOL;
                    int valueAtPosition = minimaxAlgo(tempBoard, 0, false);
                    tempBoard[i][j] = '_';

                    if (valueAtPosition > bestValue) {
                        bestX = i;
                        bestY = j;
                        bestValue = valueAtPosition;
                    }
                }
            }
        }
        TIC_TAC_TOE_BOARD[bestX][bestY] = COMPUTER_SYMBOL;
    }

    private static int minimaxAlgo(char[][] tempBoard, int depth, boolean isMax) {
        int score = evaluateIfWinning(tempBoard, depth);
        if (score == 10-depth)
            return score;
        if (score == depth-10)
            return score;
        if (!canPlay(tempBoard))
            return 0;
        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i=0;i<3;i++) {
                for (int j=0;j<3;j++) {
                    if (tempBoard[i][j] == '_') {
                        tempBoard[i][j] = COMPUTER_SYMBOL;
                        best = Math.max(best, minimaxAlgo(tempBoard, depth+1,!isMax));
                        tempBoard[i][j] = '_';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i=0;i<3;i++) {
                for (int j=0;j<3;j++) {
                    if (tempBoard[i][j] == '_') {
                        tempBoard[i][j] = HUMAN_SYMBOL;
                        best = Math.min(best, minimaxAlgo(tempBoard, depth+1,!isMax));
                        tempBoard[i][j] = '_';
                    }
                }
            }
            return best;
        }
    }

    private static boolean canPlay(char[][] tempBoard) {
        for (int i=0;i<GRID_SIZE;i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tempBoard[i][j] == '_') {
                    return true;
                }
            }
        }
        return false;
    }

    private static void enterOAtPosition() {
        System.out.println("Enter the cell number");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        int xC = (input-1)/3;
        int yC = (input-1)%3;
        if (TIC_TAC_TOE_BOARD[xC][yC] == '_') {
            TIC_TAC_TOE_BOARD[xC][yC] = HUMAN_SYMBOL;
        } else {
            System.out.println("Incorrect cell number entered");
            enterOAtPosition();
        }
    }

    private static void printRows(int i) {
        int j;
        for (j=0;j<GRID_SIZE-1;j++) {
            System.out.print(TIC_TAC_TOE_BOARD[i][j]+" | ");
        }
        System.out.println(TIC_TAC_TOE_BOARD[i][j]);
    }

    private static void printBoard() {
        int i;
        for (i=0;i<GRID_SIZE-1;i++) {
            printRows(i);
            System.out.println("--|---|--");
        }
        printRows(i);
        System.out.println();
    }

    private static void initializeBoard() {
        for (int i=0;i<GRID_SIZE;i++) {
            for (int j=0;j<GRID_SIZE;j++) {
                TIC_TAC_TOE_BOARD[i][j] = '_';
            }
        }
    }

    private static Turn whoStartsTheGame() {
        Random random = new Random();
        int ans = random.nextInt(2);
        if (ans == 0) {
            return Turn.HUMAN;
        }
        return Turn.COMPUTER;
    }

    private static void printNumbers() {
        System.out.println("Enter the correct number for the given cell");
        System.out.println("1 | 2 | 3");
        System.out.println("--|---|--");
        System.out.println("4 | 5 | 6");
        System.out.println("--|---|--");
        System.out.println("7 | 8 | 9");
        System.out.println();
    }
}
