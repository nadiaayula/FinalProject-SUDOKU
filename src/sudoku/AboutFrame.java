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

import java.awt.Image;
import javax.swing.*;

public class AboutFrame extends JFrame {
  public AboutFrame() {
    setSize(400, 600);
    setLocationRelativeTo(null);

    ImageIcon originalImage = new ImageIcon("src/sudoku/images/grouppict.jpg");
    Image scaledImage = originalImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
    ImageIcon resizedImage = new ImageIcon(scaledImage);

    JLabel label = new JLabel(
        "<html><br><center><b>Sudoku Game</b></center><center>Version 1.0</center><br><center>Developed by Group 3:</center><br>"
            + "<center>5026221006 - Noval Akbar Ramadhany</center>"
            + "<center>5026221007 - Nadia Lovely</center>"
            + "<center>5026221090 - Nadia Ayula Assyaputri</center><br>"
            + "<center>© Final Project ASD A 2024/2025</center></html>");
    label.setIcon(resizedImage);
    label.setHorizontalTextPosition(JLabel.CENTER);
    label.setVerticalTextPosition(JLabel.BOTTOM);
    label.setIconTextGap(10);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setHorizontalAlignment(JLabel.CENTER);

    add(label);
    setTitle("About");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setVisible(true);
  }
}
