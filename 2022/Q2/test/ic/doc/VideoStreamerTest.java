package ic.doc;

import ic.doc.awards.Oscar;
import ic.doc.movies.Actor;
import ic.doc.movies.Genre;
import ic.doc.movies.Movie;
import ic.doc.streaming.PlaybackEventLog;
import ic.doc.streaming.VideoStream;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static ic.doc.movies.Certification.PARENTAL_GUIDANCE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class VideoStreamerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    Library library = context.mock(Library.class);
    PlaybackEventLog playbackEventLog = context.mock(PlaybackEventLog.class);

    List<Movie> moviesLib = List.of(
            new Movie("Jurassic Park",
                    "A pragmatic paleontologist touring an almost complete theme park on an " +
                            "island in Central America is tasked with protecting a couple of kids after " +
                            "a power failure causes the park's cloned dinosaurs to run loose.",
                    2342384,
                    List.of(new Actor("Richard Attenborough"),
                            new Actor("Laura Dern"),
                            new Actor("Sam Neill"),
                            new Actor("Jeff Goldblum")),
                    Set.of(Genre.ADVENTURE),
                    List.of(Oscar.forBest("Visual Effects")),
                    PARENTAL_GUIDANCE
            ));

    @Test
    public void allowsUserToStreamSuggestedMovies() {

        context.checking(new Expectations() {{
            exactly(1).of(library).getLibrary(); with(moviesLib);
        }});

        VideoStreamer streamer = new VideoStreamer(library, );
        User user = new User("Adam", 9);

        List<Movie> movies = streamer.getSuggestedMovies(user);

        // assertThat(movies, containsInAnyOrder(moviesLib.toArray()));

        VideoStream stream = streamer.startStreaming(movies.get(0), user);

        // adam watches the movie

        streamer.stopStreaming(stream);
    }

    @Test
    public void logsStreamsWatchedForMoreThanFifteenMinutes() {

    }
}
