import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GUI extends JFrame implements KeyListener {
    private JTextArea textInputArea, morseCodeArea;
    private Controller morseCodeController;

    public GUI() {
        // Title
        super("Morse Code Translator");

        setSize(new Dimension(540, 675));

        setResizable(false);

        // Setting null allows to manually position and set the size of the components of the GUI
        setLayout(new BorderLayout());

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(Color.DARK_GRAY);

        // Place the GUI in the middle of the screen
        setLocationRelativeTo(null);

        morseCodeController = new Controller();
        addGuiComponents();
    }

    private void addGuiComponents(){
        //Title
        JLabel titleLabel = new JLabel("Morse Code Translator");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Text input
        JLabel textInputLabel = new JLabel("Text:");
        textInputLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        textInputLabel.setForeground(Color.WHITE);

        textInputArea = new JTextArea();
        textInputArea.setFont(new Font("Dialog", Font.PLAIN, 16));
        textInputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textInputArea.setLineWrap(true);
        textInputArea.setWrapStyleWord(true);
        textInputArea.addKeyListener(this);

        // Scroll for the text input area
        JScrollPane textInputScroll = new JScrollPane(textInputArea);

        JPanel textPanel = new JPanel(new BorderLayout(0, 10));
        textPanel.setBackground(Color.DARK_GRAY);
        textPanel.add(textInputLabel, BorderLayout.NORTH);
        textPanel.add(textInputScroll, BorderLayout.CENTER);

        // Morse code input
        JLabel morseCodeInputLabel = new JLabel("Morse Code:");
        morseCodeInputLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        morseCodeInputLabel.setForeground(Color.WHITE);

        morseCodeArea = new JTextArea();
        morseCodeArea.setFont(new Font("Dialog", Font.PLAIN, 16));
        morseCodeArea.setEditable(false);
        morseCodeArea.setLineWrap(true);
        morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Scroll for the morse code text area
        JScrollPane morseCodeScroll = new JScrollPane(morseCodeArea);

        JPanel morsePanel = new JPanel(new BorderLayout(0, 10));
        morsePanel.setBackground(Color.DARK_GRAY);
        morsePanel.add(morseCodeInputLabel, BorderLayout.NORTH);
        morsePanel.add(morseCodeScroll, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.add(textPanel);
        centerPanel.add(morsePanel);

        // Play sound button
        JButton playButton = new JButton("Play Sound");
        playButton.setBounds(210, 680, 100, 30);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playButton.setEnabled(false);

                Thread playMorseCodeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // attempt to play the morse code sound
                        try{
                            String[] morseCodeMessage = morseCodeArea.getText().split(" ");
                            morseCodeController.playSound(morseCodeMessage);
                        }catch(LineUnavailableException lineUnavailableException){
                            lineUnavailableException.printStackTrace();
                        }catch(InterruptedException interruptedException){
                            interruptedException.printStackTrace();
                        }finally{
                            // enable play sound button
                            playButton.setEnabled(true);
                        }
                    }
                });

                playMorseCodeThread.start();
            }
        });

        // Add to the GUI
        add(centerPanel, BorderLayout.CENTER);
        add(playButton, BorderLayout.SOUTH);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() != KeyEvent.VK_ENTER){
            String text = textInputArea.getText();

            morseCodeArea.setText(morseCodeController.translateToMorse(text));
        }
    }
}