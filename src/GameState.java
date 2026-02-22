import java.io.*;
import java.nio.file.*;

/**
 * Holds the state of a saved game and persists it to disk so progress
 * survives application restarts.
 *
 * Save file location: <user home>/.sudokuapp/save.dat
 */
public class GameState {

    private static final String SAVE_DIR  =
            System.getProperty("user.home") + File.separator + ".sudokuapp";
    private static final String SAVE_FILE =
            SAVE_DIR + File.separator + "save.dat";

    // In-memory state
    public static int[][] savedBoard    = null;
    public static int[][] savedSolution = null;
    public static int     savedHints    = 0;

    public static boolean hasSavedGame() {
        return savedBoard != null;
    }

    /** Clears in-memory state AND deletes the save file from disk. */
    public static void clear() {
        savedBoard    = null;
        savedSolution = null;
        savedHints    = 0;
        deleteSaveFile();
    }

    /** Writes current state to disk so it survives application restarts. */
    public static void saveToDisk() {
        try {
            Files.createDirectories(Paths.get(SAVE_DIR));

            SaveData data = new SaveData();
            data.board    = savedBoard;
            data.solution = savedSolution;
            data.hints    = savedHints;

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(SAVE_FILE))) {
                oos.writeObject(data);
            }
        } catch (IOException e) {
            System.err.println("Warning: could not save game to disk: " + e.getMessage());
        }
    }

    /** Loads a previously saved game from disk. Called once at startup from Main. */
    public static void loadFromDisk() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            SaveData data = (SaveData) ois.readObject();
            savedBoard    = data.board;
            savedSolution = data.solution;
            savedHints    = data.hints;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Warning: could not load save file: " + e.getMessage());
            deleteSaveFile(); // file is corrupted — remove it
        }
    }

    private static void deleteSaveFile() {
        try {
            Files.deleteIfExists(Paths.get(SAVE_FILE));
        } catch (IOException e) {
            System.err.println("Warning: could not delete save file: " + e.getMessage());
        }
    }

    private static class SaveData implements Serializable {
        private static final long serialVersionUID = 1L;
        int[][] board;
        int[][] solution;
        int     hints;
    }
}
