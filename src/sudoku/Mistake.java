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

import javax.swing.JTextField;

public class Mistake extends JTextField {
  private int mistakes = 0;
  private int maxMistakes;

  public Mistake(int difficulty) {
    resetMaxMistakes(difficulty);
    setEditable(false);
    setHorizontalAlignment(JTextField.CENTER);
  }

  public void increment() {
    mistakes++;
    updateText();
  }

  public void reset() {
    mistakes = 0;
    updateText();
  }
  
  @Override
  public String toString() {
    return "Mistake[current=" + mistakes + ", max=" + maxMistakes + "]";
  }

  public void resetMaxMistakes(int difficulty) {
    maxMistakes = switch (difficulty) {
      case 0 -> 8;
      case 1 -> 5;
      default -> 3;
    };
    updateText();
  }

  public void updateText() {
    System.out.println("Mistakes DARI UPDATE: " + mistakes + "/" + maxMistakes);
    setText("Mistakes: " + mistakes + "/" + maxMistakes);
  }

  public int getMistakes() {
    return mistakes;
  }

  public int getMaxMistakes() {
    return maxMistakes;
  }
}