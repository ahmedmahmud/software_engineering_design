package tennis;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TennisMatchTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Updateable observer = context.mock(Updateable.class);

    @Test
    public void givesCorrectScoreWhenPlayerWinsPoint() {
        TennisMatch match = new TennisMatch();
        match.playerOneWinsPoint();
        assertThat(match.score(), equalTo("15 - Love"));
        match.playerOneWinsPoint();
        assertThat(match.score(), equalTo("30 - Love"));
    }

    @Test
    public void showsDeuceOnGamePointForBothPlayers() {
        TennisMatch match = new TennisMatch();
        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
        match.playerTwoWinsPoint();
        match.playerTwoWinsPoint();
        match.playerTwoWinsPoint();
        assertThat(match.score(), equalTo("Deuce"));
    }

    @Test
    public void showsGameWhenPlayerWins() {
        TennisMatch match = new TennisMatch();
        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
        assertThat(match.score(), equalTo("Game Player 1"));
    }

    @Test
    public void updatesViewWhenEitherPlayerScores() {
        TennisMatch match = new TennisMatch();
        match.addObserver(observer);

        context.checking(new Expectations() {{
            exactly(2).of(observer).updateScores(match);
        }});

        match.playerOneWinsPoint();
        match.playerTwoWinsPoint();
    }

    @Test
    public void updatesViewWhenPlayerWinsGame() {
        TennisMatch match = new TennisMatch();
        match.addObserver(observer);

        context.checking(new Expectations() {{
            exactly(4).of(observer).updateScores(match);
            oneOf(observer).matchEnded();
        }});

        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
        match.playerOneWinsPoint();
    }
}
