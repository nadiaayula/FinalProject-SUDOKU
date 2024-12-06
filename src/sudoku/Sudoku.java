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
  private final MenuBar menu = new MenuBar();
  private final Timer timer;
  private final JLabel timerLabel;
  private int level = 0;

  // Constructor
  public Sudoku() {
    // Initialize the timer with a countdown logic
    remindingSeconds = 60000;
    timer = new Timer(1000, e -> {
      if (remindingSeconds > 0) {
        remindingSeconds -= 1000;
        updateTimerLabel(remindingSeconds);
      } else {
        JOptionPane.showMessageDialog(this, "Time's up! Game over.");
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

    // Initialize the game board
    board = new GameBoardPanel(this, timer);

    // Configure JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());

    // Add the game board to the center
    cp.add(board, BorderLayout.CENTER);

    // Create a label to display the timer
    timerLabel = new JLabel("Timer: " + (remindingSeconds / 1000) + " seconds");
    timerLabel.setHorizontalAlignment(JLabel.CENTER);
    timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    cp.add(timerLabel, BorderLayout.NORTH);

    // Set up the menu and its actions
    setMenu(menu);

    // Start a new game
    board.newGame();

    // Start the timer
    timer.start();

    // Pack and finalize UI setup
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Sudoku");
    setVisible(true);
  }

  // Set menu bar and configure its actions
  public void setMenu(MenuBar menu) {
    menu.newGame.addActionListener(e -> {
      System.out.println("masuk 1");
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
    });

    menu.medium.addActionListener(e -> {
      level = 1;
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
    });

    menu.hard.addActionListener(e -> {
      level = 2;
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
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
        return 600000;
      case 1:
        return 480000;
      case 2:
        return 300000;
      default:
        return 600000;
    }
  }

  public void resetTimer() {
    remindingSeconds = getTimeForLevel(level);
    updateTimerLabel(remindingSeconds);
    timer.restart();
  }
}
