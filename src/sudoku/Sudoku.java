package sudoku;

import java.awt.*;
import javax.swing.*;

public class Sudoku extends JFrame {
  private static final long serialVersionUID = 1L;

  private int remindingSeconds;
  private GameBoardPanel board;
  private JButton btnStart;
  private JButton btnHint;
  private JButton btnNewGame;
  private JPanel btnPanel;
  private JPanel btnSubPanel1;
  private JPanel btnSubPanel2;
  private JComboBox<String> difficulties;
  private Mistake mistake;
  private final MenuBar menu = new MenuBar();
  private Timer timer;
  private final JLabel timerLabel;
  private int level = 0;
  private String[] choices = { "Easy", "Medium", "Hard" };
  private int hintCount;

  public Sudoku() {
    // Configure JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());

    // Initialize the game board and input bar
    board = new GameBoardPanel(this, timer, mistake);
    btnPanel = new JPanel(new GridLayout(2, 1));
    btnSubPanel1 = new JPanel(new GridLayout());
    btnSubPanel2 = new JPanel(new GridLayout());

    // Create buttons
    btnStart = new JButton("Start Game");
    btnNewGame = new JButton("New Game");
    btnNewGame.setEnabled(false);
    btnHint = new JButton("Hint");
    btnHint.setEnabled(false);
    difficulties = new JComboBox<>(choices);

    // Initialize Mistake
    mistake = new Mistake(level);

    // Initialize timer
    remindingSeconds = getTimeForLevel(level);
    timer = new Timer(1000, f -> {
      if (remindingSeconds > 0) {
        remindingSeconds -= 1000;
        updateTimerLabel(remindingSeconds);
      } else {
        handleGameOver();
      }
    });
    board = new GameBoardPanel(this, timer, mistake);

    board.newGame();

    // Button Listeners
    btnStart.addActionListener(e -> startGame());
    btnNewGame.addActionListener(e -> restartGame());
    btnHint.addActionListener(e -> giveHint());

    // Difficulty Listener
    difficulties.addActionListener(e -> {
      level = difficulties.getSelectedIndex();
      updateDifficulty();
    });

    // Layout setup
    btnSubPanel1.add(btnHint);
    btnSubPanel1.add(btnNewGame);
    btnSubPanel1.add(btnStart);
    btnSubPanel2.add(difficulties);
    btnSubPanel2.add(mistake);
    btnPanel.add(btnSubPanel1);
    btnPanel.add(btnSubPanel2);
    cp.add(btnPanel, BorderLayout.SOUTH);
    cp.add(board, BorderLayout.CENTER);

    // Timer label
    timerLabel = new JLabel("Timer: " + (remindingSeconds / 1000) + " seconds");
    timerLabel.setHorizontalAlignment(JLabel.CENTER);
    timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
    cp.add(timerLabel, BorderLayout.NORTH);

    // Menu setup
    setMenu(menu);

    // Finalize UI
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Sudoku");
    setVisible(true);
  }

  private void startGame() {
    btnHint.setEnabled(true);
    btnNewGame.setEnabled(true);
    if (!timer.isRunning()) {
      timer.start();
    }
  }

  private void restartGame() {
    board.newGame();
    hintCount = 0;
    remindingSeconds = getTimeForLevel(level);
    updateTimerLabel(remindingSeconds);
    timer.restart();
    btnHint.setEnabled(true);
    btnSubPanel2.revalidate();
    btnSubPanel2.repaint();
    mistake.reset();
  }

  private void giveHint() {
    if (hintCount < 3 && board.giveHint()) {
      hintCount++;
      if (board.isSolved()) {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Congratulations! You've solved the puzzle!");
      }
    } else if (hintCount == 3) {
      btnHint.setEnabled(false);
      JOptionPane.showMessageDialog(this, "You have used all of your hints");
    }
  }

  private void updateDifficulty() {
    remindingSeconds = getTimeForLevel(level);
    updateTimerLabel(remindingSeconds);
    board.setDifficulties(level);
    mistake.resetMaxMistakes(level);
  }

  private void handleGameOver() {
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
      restartGame();
    } else {
      System.exit(0);
    }
  }

  private void updateTimerLabel(int remindingSeconds) {
    timerLabel.setText("Time Remaining: " + (remindingSeconds / 1000) + " seconds");
  }

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

  private void setMenu(MenuBar menu) {
    menu.newGame.addActionListener(e -> restartGame());
    menu.resetGame.addActionListener(e -> restartGame());
    menu.easy.addActionListener(e -> setDifficulty(0));
    menu.medium.addActionListener(e -> setDifficulty(1));
    menu.hard.addActionListener(e -> setDifficulty(2));
    setJMenuBar(menu);
  }

  private void setDifficulty(int level) {
    this.level = level;
    updateDifficulty();
  }
}
