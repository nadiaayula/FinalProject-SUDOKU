package sudoku;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * The main Sudoku program
 */
public class Sudoku extends JFrame {
  private static final long serialVersionUID = 1L; // to prevent serial warning

  private int secondsPassed;
  private GameBoardPanel board;
  private final MenuBar menu = new MenuBar();
  private final Timer timer;
  private final JLabel timerLabel;

  // Constructor
  public Sudoku() {
    // Create a timer
    timer = new Timer(1000, (ActionEvent e) -> {
      secondsPassed++;
      updateTimerLabel();
    });

    // Initialize the game board
    board = new GameBoardPanel(timer);

    // Configure JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());

    // Add the game board to the center
    cp.add(board, BorderLayout.CENTER);

    // Create a label to display the timer
    timerLabel = new JLabel("Timer: 0 seconds");
    timerLabel.setHorizontalAlignment(JLabel.CENTER);
    timerLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Optional: Better styling
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
  private void setMenu(MenuBar menu) {
    menu.newGame.addActionListener(e -> {
      board.newGame();
      secondsPassed = 0;
    });

    menu.resetGame.addActionListener(e -> {
      timerLabel.setText("Timer: 0 seconds");
      secondsPassed = 0;
      timer.start();
    });

    menu.easy.addActionListener(e -> {
      int level = 0;
      // board.setDifficulties(level);
    });
    menu.medium.addActionListener(e -> {
      int level = 1;
      // board.setDifficulties(level);
    });
    menu.hard.addActionListener(e -> {
      int level = 2;
      // board.setDifficulties(level);
    });
    menu.generator.addActionListener(e -> {
      // board.setPuzzleSource(0);
    });
    menu.template.addActionListener(e -> {
      // board.setPuzzleSource(1);
    });

    setJMenuBar(menu); // Set the menu bar to the frame
  }

  // Update the timer label text
  private void updateTimerLabel() {
    timerLabel.setText("Timer: " + secondsPassed + " seconds");
  }
}
