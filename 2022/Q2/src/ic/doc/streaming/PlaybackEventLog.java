package ic.doc.streaming;

import ic.doc.User;
import ic.doc.movies.Movie;

public interface PlaybackEventLog {
    void logWatched(User user, Movie movie);
    void logRejection(User user, Movie movie);
}



