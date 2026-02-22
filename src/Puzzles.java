import java.util.*;

/**
 * Generates random Sudoku puzzles at three difficulty levels.
 *
 * Each call to getXxxPuzzle() returns a two-element array:
 *   [0] = the puzzle  (with 0s for empty cells)
 *   [1] = the solution (fully filled)
 *
 * Returning both together avoids re-solving from a partial board in the
 * caller, which could produce a different valid solution than intended.
 */
public class Puzzles {

    public static int[][][] getEasyPuzzle()   { return generatePuzzle(35); }
    public static int[][][] getMediumPuzzle() { return generatePuzzle(45); }
    public static int[][][] getHardPuzzle()   { return generatePuzzle(55); }

    /**
     * Returns { puzzle, solution } where solution is the fully-solved board
     * that the puzzle was derived from.
     */
    private static int[][][] generatePuzzle(int cellsToRemove) {

        int[][] board = new int[9][9];
        Random rand = new Random();

        // Step 1: Randomly pre-fill up to 10 valid cells to seed randomness
        int filled = 0;
        while (filled < 10) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (board[row][col] == 0) {
                int num = rand.nextInt(9) + 1;
                if (isValid(board, row, col, num)) {
                    board[row][col] = num;
                    filled++;
                }
            }
        }

        // Step 2: Solve the board fully — this is the authoritative solution
        new SudokuSolver().solve(board);
        int[][] solution = deepCopy(board);

        // Step 3: Remove cells to produce the puzzle
        List<int[]> positions = new ArrayList<>();
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                positions.add(new int[]{row, col});

        Collections.shuffle(positions);

        int removed = 0;
        for (int[] pos : positions) {
            if (removed >= cellsToRemove) break;
            board[pos[0]][pos[1]] = 0;
            removed++;
        }

        // Return both so the caller never has to re-solve from a partial board
        return new int[][][]{board, solution};
    }

    private static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        return copy;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;
        for (int i = boxRowStart; i < boxRowStart + 3; i++)
            for (int j = boxColStart; j < boxColStart + 3; j++)
                if (board[i][j] == num) return false;
        return true;
    }
}
