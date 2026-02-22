import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Load any previously saved game from disk before opening the UI
        GameState.loadFromDisk();

        SwingUtilities.invokeLater(HomeScreen::new);
    }
}
