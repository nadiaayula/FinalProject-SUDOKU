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
