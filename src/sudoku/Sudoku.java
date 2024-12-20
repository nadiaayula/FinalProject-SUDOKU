/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #3
 * 1 - 5026231006 - Noval Akbar Ramadhany
 * 2 - 5026231007 - Nadia Lovely
 * 3 - 5026231090 - Nadia Ayula Assyaputri
 */
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
  private JButton btnHint;
  private JButton btnNewGame;
  private JPanel btnPanel;
  private JPanel btnSubPanel1;
  private JPanel btnSubPanel2;
  private JComboBox<String> difficulties;
  private final MenuBar menu = new MenuBar();
  private Timer timer;
  private Mistake mistake;
  private final JLabel timerLabel;
  private int level = 0;
  private String[] choices = { "Easy", "Medium", "Hard" };
  private int hintCount;

  // Constructor
  public Sudoku() {
    ImageIcon icon = new ImageIcon("src/sudoku/images/sudokuicon.png");
    Image sclImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
    ImageIcon welcomeImg = new ImageIcon(sclImage);
    JOptionPane.showMessageDialog(
            null,
            "<html>" +
                    "<body style='font-family: Rubik; color: #333;'>" +
                    "<h1 style='text-align: center; color: #006666;'>Welcome to Sudoku!</h1>" +
                    "<p style='font-size: 14px; line-height: 1.5; color: #555;'>Solve the puzzle by filling the grid with numbers from 1 to 9.<br>" +
                    "Remember, each number must appear only once per row, column, and 3x3 box.</p>" +
                    "<h2 style='margin-bottom: 5px; color: #006666;'>Game Levels:</h2>" +
                    "<ul style='margin-top: 0px; padding-left: 20px; color: #000;'>" +
                    "<li><b>Easy</b>: <span style='color: #008000;'>600 seconds</span> to complete</li>" +
                    "<li><b>Medium</b>: <span style='color: #FF8C00;'>480 seconds</span> to complete</li>" +
                    "<li><b>Hard</b>: <span style='color: #FF0000;'>300 seconds</span> to complete</li>" +
                    "</ul>" +
                    "<p style='font-size: 14px; line-height: 1.5; color: #555;'>The board will be randomly generated based on the level you choose.<br>" +
                    "<p style='font-size: 14px; line-height: 1.5; color: #555;'><b>Don't forget to click the <span style='color: #006666;'>Start Game</span> button to begin!</b></p>" +
                    "Good luck and have fun!</p>" +
                    "</body>" +
                    "</html>",
            "Welcome!",
            JOptionPane.INFORMATION_MESSAGE,
            welcomeImg
    );

    // Configure JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());

    // Initialize the game board and input bar
    board = new GameBoardPanel(this, timer, mistake);
    btnPanel = new JPanel(new GridLayout(2, 1));
    btnSubPanel1 = new JPanel(new GridLayout());
    btnSubPanel2 = new JPanel(new GridLayout());

    // Create button to start game
    btnStart = new JButton("Start Game");
    btnNewGame = new JButton("New Game");
    btnNewGame.setEnabled(false);
    btnHint = new JButton("Hint");
    btnHint.setEnabled(false);
    difficulties = new JComboBox<>(choices);

    mistake = new Mistake(level);
    remindingSeconds = getTimeForLevel(level);
    timer = new Timer(1000, f -> {
      if (remindingSeconds > 0) {
        remindingSeconds -= 1000;
        updateTimerLabel(remindingSeconds);
      } else {
        SoundEffect.WRONG.play();
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

    board = new GameBoardPanel(this, timer, mistake);

    // Start a new game
    board.newGame();

    btnStart.addActionListener(e -> {
      btnHint.setEnabled(true);
      btnNewGame.setEnabled(true);
      if (!timer.isRunning()) {
        timer.start();
      }
    });

    btnNewGame.addActionListener(e -> {
      board.newGame();
      hintCount = 0;
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      timer.restart();
      btnHint.setEnabled(true);
      btnNewGame.setEnabled(true);
    });

    btnHint.addActionListener(e -> {
      if (hintCount < 3 && board.giveHint()) {
        hintCount++;
        if (board.isSolved()) {
          timer.stop();
          JOptionPane.showMessageDialog(null, "Congratulations! You've solved the puzzle!");
        }
      } else if (hintCount == 3) {
        btnHint.setEnabled(false);
        JOptionPane.showMessageDialog(null, "You have used all of your hints");
      }
    });

    difficulties.addActionListener(e -> {
      JComboBox<String> choice = (JComboBox<String>) e.getSource();
      level = choice.getSelectedIndex();

      // Update the timer for the new level
      remindingSeconds = getTimeForLevel(level);
      updateTimerLabel(remindingSeconds);
      timer.restart();

      // Set the board difficulty
      board.setDifficulties(level);
      mistake.resetMaxMistakes(level);

      board.setNumbersSource(level);
    });

    btnSubPanel1.add(btnHint, BorderLayout.NORTH);
    btnSubPanel1.add(btnStart, BorderLayout.NORTH);
    btnSubPanel1.add(btnNewGame, BorderLayout.NORTH);
    btnSubPanel2.add(difficulties, BorderLayout.NORTH);
    btnSubPanel2.add(mistake, BorderLayout.NORTH);
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
