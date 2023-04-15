package tennis;

import javax.swing.*;

public class TennisScorer implements Updateable {

  private JTextField scoreDisplay;
  private JButton playerOneScores;
  private JButton playerTwoScores;

  public static void main(String[] args) {
    new TennisScorer().display();
  }

  private void display() {
    TennisMatch match = new TennisMatch();
    JFrame window = new JFrame("Tennis");
    window.setSize(400, 150);

    playerOneScores = new JButton("Player One Scores");
    playerTwoScores = new JButton("Player Two Scores");

    scoreDisplay = new JTextField(20);
    scoreDisplay.setHorizontalAlignment(JTextField.CENTER);
    scoreDisplay.setEditable(false);

    playerOneScores.addActionListener(e -> {
      match.playerOneWinsPoint();
    });

    playerTwoScores.addActionListener(e -> {
      match.playerTwoWinsPoint();
    });

    match.addObserver(this);

    JPanel panel = new JPanel();
    panel.add(playerOneScores);
    panel.add(playerTwoScores);
    panel.add(scoreDisplay);

    window.add(panel);

    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    window.setVisible(true);

  }

  @Override
  public void updateScores(TennisMatch match) {
    scoreDisplay.setText(match.score());
  }

  @Override
  public void matchEnded() {
    playerOneScores.setEnabled(false);
    playerTwoScores.setEnabled(false);
  }
}