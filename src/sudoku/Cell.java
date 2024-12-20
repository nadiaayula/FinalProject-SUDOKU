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

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

/**
 * The Cell class model the cells of the Sudoku puzzle, by customizing
 * (subclass)
 * the javax.swing.JTextField to include row/column, puzzle number and status.
 */
public class Cell extends JTextField {
  private static final long serialVersionUID = 1L; // to prevent serial warning

  // Define named constants for JTextField's colors and fonts
  // to be chosen based on CellStatus
  public static final Color BG_GIVEN = new Color(0, 102, 102);
  public static final Color FG_GIVEN = new Color(224, 255, 255);
  public static final Color FG_NOT_GIVEN = new Color(0, 51, 51);
  public static final Color BG_TO_GUESS = new Color(0, 153, 153);
  public static final Color BG_CORRECT_GUESS = new Color(102, 255, 204);
  public static final Color BG_WRONG_GUESS = new Color(255, 51, 51);
  public static final Color BG_HIGHLIGHT = new Color(0, 180, 128);
  public static final Font FONT_NUMBERS = new Font("Rubik", Font.PLAIN, 28);

  // Define properties (package-visible)
  /** The row and column number [0-8] of this cell */
  int row, col;
  /** The puzzle number [1-9] for this cell */
  int number;
  /** The status of this cell defined in enum CellStatus */
  CellStatus status;
  private Mistake mistake;

  /** Constructor */
  public Cell(int row, int col, Mistake mistake) {
    super(); // JTextField
    this.row = row;
    this.col = col;
    this.mistake = mistake;

    // Inherited from JTextField: Beautify all the cells once for all
    super.setHorizontalAlignment(JTextField.CENTER);
    super.setFont(FONT_NUMBERS);
    setCellBorder(row, col);

    PlainDocument doc = (PlainDocument) this.getDocument();
    doc.setDocumentFilter(new DigitInputFilter());
  }

  private void setCellBorder(int row, int col) {
    int top = (row % 3 == 0) ? 3 : 1;
    int left = (col % 3 == 0) ? 3 : 1;
    int bottom = (row % 3 == 2) ? 3 : 1;
    int right = (col % 3 == 2) ? 3 : 1;

    setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
  }

  /** Reset this cell for a new game, given the puzzle number and isGiven */
  public void newGame(int number, boolean isGiven) {
    this.number = number;
    status = isGiven ? CellStatus.GIVEN : CellStatus.TO_GUESS;
    paint(); // paint itself
  }

  /** This Cell (JTextField) paints itself based on its status */
  public void paint() {
    if (status == CellStatus.GIVEN) {
      // Inherited from JTextField: Set display properties
      super.setText(number + "");
      super.setEditable(false);
      super.setBackground(BG_GIVEN);
      super.setForeground(FG_GIVEN);
    } else if (status == CellStatus.TO_GUESS) {
      // Inherited from JTextField: Set display properties
      super.setText("");
      super.setEditable(true);
      super.setBackground(BG_TO_GUESS);
      super.setForeground(FG_NOT_GIVEN);
    } else if (status == CellStatus.CORRECT_GUESS) { // from TO_GUESS
      super.setText(number + "");
      super.setBackground(BG_CORRECT_GUESS);
      super.setForeground(BG_GIVEN);
      super.setEditable(false);
      SoundEffect.CORRECT.play();
    } else if (status == CellStatus.WRONG_GUESS) { // from TO_GUESS
      super.setBackground(BG_WRONG_GUESS);
      SoundEffect.WRONG.play();
    }
  }

  /************* ✨ Codeium Command ⭐ *************/
  /**
   * Set the background color to highlight this cell.
   */
  /****** 106a88c9-d4c5-468a-8782-13d5f7905d8c *******/
  public void highlight() {
    super.setBackground(BG_HIGHLIGHT);
  }

  public void resetHighlight() {
    paint();
  }
}