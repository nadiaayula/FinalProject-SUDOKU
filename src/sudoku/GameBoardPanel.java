package sudoku;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
  private Sudoku sudoku;

  private static final long serialVersionUID = 1L; // to prevent serial warning

  // Define named constants for UI sizes
  public static final int CELL_SIZE = 60; // Cell width/height in pixels
  public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
  public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
  // Board width/height in pixels

  // Define properties
  /** The game board composes of 9x9 Cells (customized JTextFields) */
  private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
  /** It also contains a Puzzle with array numbers and isGiven */
  private Puzzle puzzle = new Puzzle();
  private int difficulties = 0;
  private int numbersSource = 0;

  private Mistake mistake;
  private Timer timer;
  private JLabel timerLabel;
  private JButton btnStart;

  /** Constructor */
  public GameBoardPanel(Sudoku sudoku, Timer timer, Mistake mistake) {
    this.mistake = mistake;
    super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE)); // JPanel
    // If timer is null, create a default one
    if (timer == null) {
      this.timer = new Timer(1000, e -> {
        // Handle timer events here if necessary
      });
    } else {
      this.timer = timer;
    }

    // Set up the Start button to start the game
    btnStart = new JButton("Start Game");
    btnStart.addActionListener(e -> startGame());

    // Create a panel for the top area containing inputBar and timerLabel
    JPanel topPanel = new JPanel(new BorderLayout());

    // Create a label to display the timer
    timerLabel = new JLabel("Timer: 0 seconds");

    // Allocate the 2D array of Cell, and added into JPanel.
    for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
      for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
        cells[row][col] = new Cell(row, col, mistake);
        super.add(cells[row][col]); // JPanel
      }
    }

    // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
    // Cells (JTextFields)
    // .........\
    CellInputListener listener = new CellInputListener();

    this.sudoku = sudoku; // inisialisasi reference

    // [TODO 4] Adds this common listener to all editable cells
    // .........
    // for (int row ) {
    // for (int col ) {
    // if (cells[row][col].isEditable()) {
    // cells[row][col].addActionListener(listener); // For all editable rows and
    // cols
    // }
    // }
    // }
    for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
      for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
        if (cells[row][col].isEditable()) {
          cells[row][col].addActionListener(listener);
        }
      }
    }

    super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    this.timer = timer;
  }

  // Start a new game
  public void startGame() {
    mistake.reset();
    timer.start();
    newGame();
  }

  /**
   * Generate a new puzzle; and reset the game board of cells based on the puzzle.
   * You can call this method to start a new game.
   */
  public void newGame() {
    // Generate a new puzzle

    puzzle.newPuzzle(difficulties, numbersSource);

    // Initialize all the 9x9 cells, based on the puzzle.
    for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
      for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
        cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
      }
    }

    mistake.reset();
    timerLabel.setText("Timer: 0 seconds");
  }

  /**
   * Return true if the puzzle is solved
   * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
   */
  public boolean isSolved() {
    for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
      for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
        if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
          return false;
        }
      }
    }
    return true;
  }

  // [TODO 2] Define a Listener Inner Class for all the editable Cells
  // .........
  class CellInputListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      // Get a reference of the JTextField that triggers this action event
      Cell sourceCell = (Cell) e.getSource();

      try {
        String input = sourceCell.getText().trim(); // Mengambil input pengguna dan menghapus spasi
        if (input.length() != 1 || !Character.isDigit(input.charAt(0))) {
          throw new NumberFormatException("Input must be a single digit between 1 and 9.");
        }
        // Retrieve the int entered
        int numberIn = Integer.parseInt(input); // Konversi ke integer

        // Validasi angka harus antara 1 dan 9
        if (numberIn < 1 || numberIn > 9) {
          throw new NumberFormatException("Input must be a digit between 1 and 9.");
        }

        // Debugging: Cetak angka yang dimasukkan
        System.out.println("You entered " + numberIn);

        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
          for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
            Cell cell = cells[row][col];
            if (cell.status == CellStatus.CORRECT_GUESS && cell != sourceCell) {
              cell.status = CellStatus.GIVEN;
              cell.paint();
            }
            cell.resetHighlight();
          }
        }

        if (numberIn == sourceCell.number) {
          sourceCell.status = CellStatus.CORRECT_GUESS;
          for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
              Cell cell = cells[row][col];
              if (cell.status == CellStatus.GIVEN && cell.number == numberIn) {
                cell.highlight();
              }
            }
          }
        } else {
          sourceCell.status = CellStatus.WRONG_GUESS;
        }
        sourceCell.paint(); // re-paint this cell based on its status
        System.out.println("Mistake: " + mistake.getMistakes() + "/" + mistake.getMaxMistakes());
        if (mistake.getMistakes() >= mistake.getMaxMistakes()) {
          SoundEffect.WRONG.play();
          JOptionPane.showMessageDialog(
              null,
              "You have made too many mistakes! Game over.",
              "Game Over",
              JOptionPane.WARNING_MESSAGE);
          System.exit(0);
        }

        /*
         * [TODO 6] (later)
         * Check if the player has solved the puzzle after this move,
         * by calling isSolved(). Put up a congratulation JOptionPane, if so.
         */
        if (isSolved()) {
          SoundEffect.WIN.play();
          showCompletionDialog();
        }
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            null,
            "Invalid input! Please enter a single digit (0-9).",
            "Invalid Input",
            JOptionPane.WARNING_MESSAGE);
        sourceCell.setText("");
      }

    }

    private void showCompletionDialog() {
      // Opsi untuk dialog
      Object[] options = { "Restart", "Quit" };

      int choice = JOptionPane.showOptionDialog(
          null,
          "Congratulations! You've solved the puzzle.\nWhat would you like to do next?",
          "Puzzle Solved",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.INFORMATION_MESSAGE,
          null,
          options,
          options[0] // Default pilihan
      );

      if (choice == JOptionPane.YES_OPTION) {
        // Restart permainan baru
        sudoku.resetTimer();
        newGame();
      } else if (choice == JOptionPane.NO_OPTION) {
        // Keluar dari aplikasi
        System.exit(0);
      }
    }
  }

  public boolean giveHint() {
    int[] nums = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    ArrayList<Integer> rows = new ArrayList<>(9);
    ArrayList<Integer> cols = new ArrayList<>(9);
    for (int i : nums) {
      rows.add(i);
      cols.add(i);
    }
    Collections.shuffle(rows);
    for (int i : rows) {
      Collections.shuffle(cols);
      for (int j : cols) {
        if (cells[i][j].status == CellStatus.TO_GUESS || cells[i][j].status == CellStatus.WRONG_GUESS) {
          cells[i][j].setText("" + puzzle.numbers[i][j]);
          cells[i][j].status = CellStatus.CORRECT_GUESS;
          cells[i][j].paint();
          return true;
        }
      }
    }
    return false;
  }

  public void setDifficulties(int difficulties) {
    this.difficulties = difficulties;
  }

  public void setNumbersSource(int numbersSource) {
    this.numbersSource = numbersSource;
  }

  public void getMistakes() {
    mistake.getMistakes();
  }
}