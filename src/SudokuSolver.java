import java.util.*;

public class SudokuSolver {

    /**
     * Fills the board in-place using backtracking.
     *
     * @param shuffle  true  → try digits in random order (used during puzzle
     *                         generation to produce varied boards)
     *                 false → try digits 1-9 in order (used when solving a
     *                         known puzzle deterministically)
     */
    public boolean solve(int[][] board, boolean shuffle) {

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (board[row][col] == 0) {

                    int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9};
                    if (shuffle) shuffleArray(digits);

                    for (int num : digits) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board, shuffle)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /** Convenience overload — shuffles by default (backwards-compatible). */
    public boolean solve(int[][] board) {
        return solve(board, true);
    }

    private void shuffleArray(int[] arr) {
        Random rand = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
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
