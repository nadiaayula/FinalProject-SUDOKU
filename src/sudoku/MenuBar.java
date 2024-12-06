package sudoku;

import javax.swing.*;

public class MenuBar extends JMenuBar {
  JMenu file = new JMenu("File");
  JMenuItem newGame = new JMenuItem("New Game");
  JMenuItem resetGame = new JMenuItem("Reset Game");
  JMenuItem exitGame = new JMenuItem("Exit Game");
  ButtonGroup difficulties = new ButtonGroup();
  JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Easy");
  JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Medium");
  JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Hard");
  ButtonGroup puzzleSource = new ButtonGroup();
  JRadioButtonMenuItem generator = new JRadioButtonMenuItem("Puzzle Generator");
  JRadioButtonMenuItem template = new JRadioButtonMenuItem("Puzzle Template");
  JMenu help = new JMenu("Help");
  JMenuItem about = new JMenuItem("About");

  public MenuBar() {
    exitGame.addActionListener(e -> {
      System.exit(0);
    });

    file.add(newGame);
    file.add(resetGame);
    file.addSeparator();
    file.add(exitGame);

    easy.setSelected(true);

    difficulties.add(easy);
    difficulties.add(medium);
    difficulties.add(hard);

    puzzleSource.add(generator);
    puzzleSource.add(template);
    generator.setSelected(true);

    about.addActionListener(e -> {
      SwingUtilities.invokeLater(AboutFrame::new);
    });

    help.add(about);

    add(file);
    add(help);
  }
}
