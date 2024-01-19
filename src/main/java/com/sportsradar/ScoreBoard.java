package com.sportsradar;

import java.util.*;

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


    public synchronized String startNewMatch(Team homeTeam, Team awayTeam) {

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

    public synchronized void updateMatch(String handle, int homeScore, int awayScore) {
        if (!this.ongoingMatches.containsKey(handle)) {
            throw new IllegalArgumentException("The match does not exist!");
        }


        var match = this.ongoingMatches.get(handle);
        this.matchOrder.remove(match);

        match.setHomeTeamScore(homeScore);
        match.setAwayTeamScore(awayScore);

        this.matchOrder.add(match);
    }

    public synchronized void removeMatch(String handle) {
        if (!this.ongoingMatches.containsKey(handle)) {
            throw new IllegalArgumentException("The match does not exist");
        }

        var match = this.ongoingMatches.get(handle);
        this.matchOrder.remove(match);
        this.ongoingMatches.remove(handle);
    }

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


