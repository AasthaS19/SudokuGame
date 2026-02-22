import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

public class SudokuGUI {

    private JFrame frame;
    private JTextField[][] cells = new JTextField[9][9];
    private int[][] solution;

    private int hints;
    private JLabel hintLabel;

    public SudokuGUI(int[][] puzzle, int[][] solution, int hints) {

        this.solution = solution;
        this.hints    = hints;

        frame = new JFrame("Sudoku Game");
        frame.setSize(600, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // ── Top Panel ────────────────────────────────────────────────────────
        JPanel topPanel = new JPanel(new BorderLayout());

        hintLabel = new JLabel("Hints Left: " + hints);
        hintLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JButton hintButton = new JButton("Use Hint");
        hintButton.addActionListener(e -> useHint());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveGame());

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            frame.dispose();
            new HomeScreen();
        });

        JPanel leftPanel = new JPanel();
        leftPanel.add(hintLabel);
        leftPanel.add(hintButton);
        leftPanel.add(saveButton);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(homeButton, BorderLayout.EAST);
        frame.add(topPanel, BorderLayout.NORTH);

        // ── Grid ─────────────────────────────────────────────────────────────
        JPanel mainGrid = new JPanel(new GridLayout(3, 3));
        mainGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font font = new Font("Arial", Font.BOLD, 20);

        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {

                JPanel subGrid = new JPanel(new GridLayout(3, 3));
                subGrid.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {

                        int r = boxRow * 3 + row;
                        int c = boxCol * 3 + col;

                        JTextField cell = new JTextField();
                        cell.setHorizontalAlignment(JTextField.CENTER);
                        cell.setFont(font);
                        cell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                        ((AbstractDocument) cell.getDocument()).setDocumentFilter(new DigitFilter());

                        if (puzzle[r][c] != 0) {
                            // Pre-filled (locked) cell
                            cell.setText(String.valueOf(puzzle[r][c]));
                            cell.setEditable(false);
                            cell.setFocusable(false);
                            cell.setBackground(new Color(220, 220, 220));
                        } else {
                            // User-input cell — attach live-check listener
                            final int rf = r, cf = c;
                            cell.getDocument().addDocumentListener(new DocumentListener() {
                                @Override public void insertUpdate(DocumentEvent e)  { checkCell(rf, cf); }
                                @Override public void removeUpdate(DocumentEvent e)  { cells[rf][cf].setBackground(Color.WHITE); }
                                @Override public void changedUpdate(DocumentEvent e) {}
                            });
                        }

                        cells[r][c] = cell;
                        subGrid.add(cell);
                    }
                }

                mainGrid.add(subGrid);
            }
        }

        frame.add(mainGrid, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void checkCell(int row, int col) {

        String text = cells[row][col].getText();
        if (text.isEmpty()) return;

        int value = Integer.parseInt(text);

        if (value == solution[row][col]) {
            // Lock the cell immediately (we're already on the EDT from DocumentListener)
            cells[row][col].setEditable(false);
            cells[row][col].setFocusable(false);
            cells[row][col].setBackground(Color.WHITE);

            // Defer the win dialog so the document update fully completes first
            if (isBoardComplete()) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "Congratulations! You solved it!");
                    GameState.clear();
                    frame.dispose();
                    new HomeScreen();
                });
            }
        } else {
            cells[row][col].setBackground(new Color(255, 200, 200));
        }
    }

    private boolean isBoardComplete() {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (cells[r][c].isEditable()) return false;
        return true;
    }

    private void useHint() {

        if (hints == 0) {
            JOptionPane.showMessageDialog(frame, "No hints available!");
            return;
        }

        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                if (cells[r][c].isEditable() && cells[r][c].getText().isEmpty())
                    emptyCells.add(new int[]{r, c});

        if (emptyCells.isEmpty()) return;

        int[] chosen = emptyCells.get(new Random().nextInt(emptyCells.size()));
        int row = chosen[0], col = chosen[1];

        cells[row][col].setText(String.valueOf(solution[row][col]));
        cells[row][col].setEditable(false);
        cells[row][col].setFocusable(false);
        cells[row][col].setBackground(Color.WHITE);

        hints--;
        hintLabel.setText("Hints Left: " + hints);
    }

    private void saveGame() {

        int[][] currentBoard = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                // Only save non-editable cells (original + correctly answered).
                // Editable cells may hold a wrong number (shown red) — saving
                // those would lock the wrong digit on resume.
                if (!cells[r][c].isEditable()) {
                    String text = cells[r][c].getText();
                    currentBoard[r][c] = text.isEmpty() ? 0 : Integer.parseInt(text);
                }
                // else leave as 0 (default)
            }
        }

        GameState.savedBoard    = currentBoard;
        GameState.savedSolution = solution;
        GameState.savedHints    = hints;
        GameState.saveToDisk();

        JOptionPane.showMessageDialog(frame, "Game saved!");
        frame.dispose();
        new HomeScreen();
    }

    // ── Static inner class: single-digit input filter ────────────────────────
    // Static because it doesn't need access to any SudokuGUI instance state.

    private static class DigitFilter extends DocumentFilter {

        @Override
        public void replace(FilterBypass fb, int offset, int length,
                            String text, AttributeSet attrs) throws BadLocationException {
            // Allow a single digit 1-9, or an empty string (deletion/backspace)
            if (text.matches("[1-9]?")) {
                if (fb.getDocument().getLength() == 0 || length == 1) {
                    super.replace(fb, 0, fb.getDocument().getLength(), text, attrs);
                }
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset,
                                 String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[1-9]") && fb.getDocument().getLength() < 1) {
                super.insertString(fb, offset, string, attr);
            }
        }
    }
}
