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
  public static final Color BG_GIVEN = new Color(29, 53, 87); // RGB
  public static final Color FG_GIVEN = new Color(241, 250, 238);
  public static final Color FG_NOT_GIVEN = new Color(45, 30, 47);
  public static final Color BG_TO_GUESS = new Color(69, 123, 157);
  public static final Color BG_CORRECT_GUESS = new Color(168, 218, 220);
  public static final Color BG_WRONG_GUESS = new Color(230, 57, 70);
  public static final Font FONT_NUMBERS = new Font("Rubik", Font.PLAIN, 28);

  // Define properties (package-visible)
  /** The row and column number [0-8] of this cell */
  int row, col;
  /** The puzzle number [1-9] for this cell */
  int number;
  /** The status of this cell defined in enum CellStatus */
  CellStatus status;

  /** Constructor */
  public Cell(int row, int col) {
    super(); // JTextField
    this.row = row;
    this.col = col;
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
      super.setBackground(BG_CORRECT_GUESS);
      super.setForeground(BG_GIVEN);
      super.setEditable(false);
      SoundEffect.CORRECT.play();
    } else if (status == CellStatus.WRONG_GUESS) { // from TO_GUESS
      super.setBackground(BG_WRONG_GUESS);
      SoundEffect.WRONG.play();
    }
  }
}