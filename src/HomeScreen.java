import javax.swing.*;
import java.awt.*;

public class HomeScreen {

    private JFrame frame;

    public HomeScreen() {

        frame = new JFrame("Sudoku - Home");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel("Sudoku Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        frame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String[] difficulties = {"Easy", "Medium", "Hard"};
        JComboBox<String> difficultyBox = new JComboBox<>(difficulties);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {

            String difficulty = (String) difficultyBox.getSelectedItem();

            // Puzzles now returns [puzzle, solution] together — no re-solving needed
            int[][][] result;
            if ("Easy".equals(difficulty)) {
                result = Puzzles.getEasyPuzzle();
            } else if ("Medium".equals(difficulty)) {
                result = Puzzles.getMediumPuzzle();
            } else {
                result = Puzzles.getHardPuzzle();
            }

            int[][] puzzle   = result[0];
            int[][] solution = result[1];

            GameState.clear(); // wipe old save from memory and disk

            frame.dispose();
            new SudokuGUI(puzzle, solution, 3);
        });

        // Enabled only when a saved game exists on disk
        JButton resumeButton = new JButton("Resume Game");
        resumeButton.setEnabled(GameState.hasSavedGame());
        resumeButton.addActionListener(e -> {
            frame.dispose();
            new SudokuGUI(GameState.savedBoard, GameState.savedSolution, GameState.savedHints);
        });

        centerPanel.add(difficultyBox);
        centerPanel.add(startButton);
        centerPanel.add(resumeButton);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
