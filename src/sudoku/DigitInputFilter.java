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
import javax.swing.text.*;

public class DigitInputFilter extends DocumentFilter {
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

        if (newText.matches("[1-9]?")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
        replace(fb, offset, 0, text, attrs);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }
}
