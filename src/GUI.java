import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    public GUI() {
        // Title
        super("Morse Code Translator");

        // Sets the size of the frame
        setSize(new Dimension(540, 660));

        // Prevents the user to resize the GUI
        setResizable(false);

        // Setting null allows to manually position and set the size of the components of the GUI
        setLayout(null);

        // Exits the application when closing the GUI
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // GUI background color
        getContentPane().setBackground(Color.DARK_GRAY);

        // Place the GUI in the middle of the screen
        setLocationRelativeTo(null);
    }
}
