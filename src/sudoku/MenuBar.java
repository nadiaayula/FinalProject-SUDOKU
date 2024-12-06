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

import javax.swing.*;

public class MenuBar extends JMenuBar {
  ButtonGroup difficulties = new ButtonGroup();
  JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy");
  JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium");
  JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard");
  ButtonGroup puzzleSource = new ButtonGroup();
  JMenuItem about = new JMenuItem("About");

  public MenuBar() {

    easy.setSelected(true);

    difficulties.add(easy);
    difficulties.add(medium);
    difficulties.add(hard);

    about.addActionListener(e -> {
      SwingUtilities.invokeLater(AboutFrame::new);
    });

    add(about);
  }
}
