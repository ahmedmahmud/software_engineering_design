package tennis;

public class TennisMatch {
    private final String[] scoreNames = {"Love", "15", "30", "40"};
    private int playerOneScore = 0;
    private int playerTwoScore = 0;
    private Updateable observer;

    public String score() {
        if (playerOneScore > 2 && playerTwoScore > 2) {
            int difference = playerOneScore - playerTwoScore;
            switch (difference) {
                case 0:
                    return "Deuce";
                case 1:
                    return "Advantage Player 1";
                case -1:
                    return "Advantage Player 2";
                case 2:
                    return "Game Player 1";
                case -2:
                    return "Game Player 2";
            }
        }

        if (playerOneScore > 3) {
            return "Game Player 1";
        }
        if (playerTwoScore > 3) {
            return "Game Player 2";
        }
        if (playerOneScore == playerTwoScore) {
            return scoreNames[playerOneScore] + " all";
        }
        return scoreNames[playerOneScore] + " - " + scoreNames[playerTwoScore];
    }
    public void playerOneWinsPoint() {
        playerOneScore++;
        updateObserver();
    }
    public void playerTwoWinsPoint() {
        playerTwoScore++;
        updateObserver();
    }

    private void updateObserver() {
        if (observer != null) {
            observer.updateScores(this);
            if (gameHasEnded()) {
                observer.matchEnded();
            }
        }
    }
    private boolean gameHasEnded() {
        return score().contains("Game");
    }

    public void addObserver(Updateable observer) {
        this.observer = observer;
    }
}
