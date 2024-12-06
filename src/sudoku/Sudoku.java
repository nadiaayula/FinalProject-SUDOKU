package sudoku;

import java.awt.*;
import javax.swing.*;

/**
 * The main Sudoku program
 */
public class Sudoku extends JFrame {
  private static final long serialVersionUID = 1L; // To prevent serial warning

  private int remindingSeconds;
  private GameBoardPanel board;
  private JButton btnStart;
  private JButton btnNewGame;
  private JButton btnReset;
  private JPanel btnPanel;
  private JPanel btnSubPanel1;
  private JPanel btnSubPanel2;
  private JComboBox<String> difficulties;
  private final MenuBar menu = new MenuBar();
  private Timer timer;
  private final JLabel timerLabel;
  private int level = 0;
  private String[] choices = {"Easy", "Medium", "Hard"};

  // Constructor
  public Sudoku() {
    // Configure JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());

    // Initialize the game board and input bar
    board = new GameBoardPanel(this,timer);
    btnPanel = new JPanel((new GridLayout(2,1)));
    btnSubPanel1 = new JPanel(new GridLayout());
    btnSubPanel2 = new JPanel(new GridLayout());

    // Create button to start game
    btnStart = new JButton("Start Game");
    btnNewGame = new JButton("New Game");
    btnReset = new JButton("Reset Timer");
    difficulties = new JComboBox<>(choices);


    // Initialize the timer outside the listener
    remindingSeconds = 60000; // Default is easy level: 60 seconds
    timer = new Timer(1000, f -> {
      if (remindingSeconds > 0) {
        remindingSeconds -= 1000;
        updateTimerLabel(remindingSeconds);
      } else {
        int choice = JOptionPane.showOptionDialog(
            this,
            "Time's up! Game over.\nWould you like to restart the game?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[] { "Restart", "Exit" },
            "Restart");
        if (choice == JOptionPane.YES_OPTION) {
          board.newGame();
          remindingSeconds = getTimeForLevel(level);
          updateTimerLabel(remindingSeconds);
        } else {
          System.exit(0);
        }
      }
    });

    board = new GameBoardPanel(this, timer);

    // Start a new game
    board.newGame();

    btnStart.addActionListener(e -> {
        if (!timer.isRunning()) {
          timer.start();
        }
    });

    btnNewGame.addActionListener(e -> {
      board.newGame();
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      timer.restart(); // Restart the timer
      // harusnya ngacak lagi
    });

    btnReset.addActionListener(e -> {
      board.newGame(); // Reset the game board
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      timer.restart(); // Restart the timer
    });

    difficulties.addActionListener(e -> {
      JComboBox<String> choice = (JComboBox<String>) e.getSource();
      int difficulties = choice.getSelectedIndex();

      // Update the timer for the new level
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      timer.restart();  // Restart the timer to reflect the new difficulty time

      // Set the board difficulty
      board.setDifficulties(level);
    });

    btnSubPanel1.add(btnNewGame, BorderLayout.NORTH);
    btnSubPanel1.add(btnStart, BorderLayout.NORTH);
    btnSubPanel1.add(btnReset, BorderLayout.NORTH);
    btnSubPanel2.add(difficulties, BorderLayout.NORTH);
    btnPanel.add(btnSubPanel1);
    btnPanel.add(btnSubPanel2);
    cp.add(btnPanel, BorderLayout.SOUTH);

    // Add the game board to the center
    cp.add(board, BorderLayout.CENTER);

    // Create a label to display the timer
    timerLabel = new JLabel("Timer: " + (remindingSeconds / 1000) + " seconds");
    timerLabel.setHorizontalAlignment(JLabel.CENTER);
    timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    cp.add(timerLabel, BorderLayout.NORTH);

    // Set up the menu and its actions
    setMenu(menu);

    // Pack and finalize UI setup
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Sudoku");
    setVisible(true);
  }

  // Set menu bar and configure its actions
  public void setMenu(MenuBar menu) {
    menu.newGame.addActionListener(e -> {
      board.newGame();
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      timer.restart(); // Restart the timer
    });

    menu.resetGame.addActionListener(e -> {
      board.newGame(); // Reset the game board
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      timer.restart(); // Restart the timer
    });

    menu.easy.addActionListener(e -> {
      level = 0;
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      board.setDifficulties(level);
    });

    menu.medium.addActionListener(e -> {
      level = 1;
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      board.setDifficulties(level);
    });

    menu.hard.addActionListener(e -> {
      level = 2;
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      board.setDifficulties(level);
    });

    menu.generator.addActionListener(e -> {
      // board.setPuzzleSource(0);
    });

    menu.template.addActionListener(e -> {
      // board.setPuzzleSource(1);
    });

    setJMenuBar(menu);
  }

  // Update the timer label text
  private void updateTimerLabel(int remindingSeconds) {
    timerLabel.setText("Time Remaining: " + (remindingSeconds / 1000) + " seconds");
  }

  // Get the time in milliseconds for the given level
  private int getTimeForLevel(int level) {
    switch (level) {
      case 0:
        return 60000;
      case 1:
        return 48000;
      case 2:
        return 30000;
      default:
        return 60000;
    }
  }

  public void resetTimer() {
    remindingSeconds = getTimeForLevel(level);
    updateTimerLabel(remindingSeconds);
    timer.restart();
  }
}
