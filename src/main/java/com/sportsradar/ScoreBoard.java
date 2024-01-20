package com.sportsradar;

import java.util.*;

/**
 * If race conditions occurred in between the calls of public methods the results could
 * be undefined. Thats why all of the public methods are synchronized and atomic.
 *
 * The complexity:
 * startMatch - O(logN)
 * updateMatch - O(logN)
 * removeMatch - O(logN)
 * showBoard - O(N)
 *
 * The design decision has been made to sacrifice CRUD operations for better time
 * of retrieval board positions in order. It makes sense as showingBoard should be
 * most frequent operation as it is online board.
 */
public class ScoreBoard {

    private final Comparator<Match> matchComparator;

    private final Map<String, Match> ongoingMatches;

    private final TreeSet<Match> matchOrder;


    public ScoreBoard() {
        this.matchComparator = (l, r) -> {
            var totalL = l.getAwayTeamScore() + l.getHomeTeamScore();
            var totalR = r.getAwayTeamScore() + r.getHomeTeamScore();

            if (totalL != totalR) {
                return Integer.compare(totalR, totalL);
            } else {
                return Long.compare(r.getNanoStartTimestamp(), l.getNanoStartTimestamp());
            }
        };
        this.ongoingMatches = new HashMap<>();
        this.matchOrder = new TreeSet<>(this.matchComparator);
    }

    /**
     * Starts a new match and puts it in the scoreboard. Note that at all times the
     * matches are sorted: (1) more total goals first (2) on ties most recent.
     * The teams must be different and not occurring already on the board.
     * Returns handle for match as string in format: HomeCountry/AwayCountry/StartSystemNanos
     * @param homeTeam
     * @param awayTeam
     * @return handle
     */
    public synchronized String startNewMatch(Team homeTeam, Team awayTeam) {

        Objects.requireNonNull(homeTeam, "Home team is null");
        Objects.requireNonNull(awayTeam, "Away team is null");

        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home and away teams must differ");
        }

        for (Match m : this.matchOrder) {
            if (m.getAwayTeam().equals(homeTeam) || m.getAwayTeam().equals(awayTeam)
                    || m.getHomeTeam().equals(homeTeam) || m.getHomeTeam().equals(awayTeam)) {
                throw new IllegalArgumentException("No team can participate in 2 concurrent matches");
            }
        }

        var match = new Match(homeTeam, awayTeam);
        var token = match.matchToken();
        this.ongoingMatches.put(token, match);
        this.matchOrder.add(match);

        return token;
    }

    /**
     * Updates matches score, works as removing match from scoreboard and adding it with new
     * score. Expects a handle representing a match (must be present in scoreboard). The scores
     * must be equal or more than zero..
     * @param handle
     * @param homeScore
     * @param awayScore
     */
    public synchronized void updateMatch(String handle, int homeScore, int awayScore) {

        Objects.requireNonNull(handle, "Handle must be not blank");

        if (homeScore < 0) {
            throw new IllegalArgumentException("Home score must be equal or greater than zero!");
        }

        if (awayScore < 0) {
            throw new IllegalArgumentException("Away score must be equal or greater than zero!");
        }

        if (!this.ongoingMatches.containsKey(handle)) {
            throw new IllegalArgumentException("The match does not exist!");
        }


        var match = this.ongoingMatches.get(handle);
        this.matchOrder.remove(match);

        match.setHomeTeamScore(homeScore);
        match.setAwayTeamScore(awayScore);

        this.matchOrder.add(match);
    }

    /**
     * Removes a match given handle. Note that the match must be present on board
     * or else exception is thrown.
     * @param handle
     */
    public synchronized void removeMatch(String handle) {
        Objects.requireNonNull(handle, "Handle is null");

        if (!this.ongoingMatches.containsKey(handle)) {
            throw new IllegalArgumentException("The match does not exist");
        }

        var match = this.ongoingMatches.get(handle);
        this.matchOrder.remove(match);
        this.ongoingMatches.remove(handle);
    }

    /**
     * Returns ordered board positions. The positions are defensive copies of matches.
     * @return
     */
    public synchronized List<BoardPosition> showBoard() {
        return this.matchOrder
                .stream()
                .map(m -> new BoardPosition(
                        m.getHomeTeam().getNationality(),
                        m.getAwayTeam().getNationality(),
                        m.getHomeTeamScore(),
                        m.getAwayTeamScore()))
                .toList();
    }
}


