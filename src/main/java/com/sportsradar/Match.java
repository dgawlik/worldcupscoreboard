package com.sportsradar;

import java.util.Objects;

public class Match {
    private final Team homeTeam;
    private final Team awayTeam;

    private final long nanoStartTimestamp;

    private int homeTeamScore;

    private int awayTeamScore;

    public Match(Team homeTeam, Team awayTeam,
                 long startTime, int homeScore, int awayScore) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.nanoStartTimestamp = startTime;
        this.homeTeamScore = homeScore;
        this.awayTeamScore = awayScore;
    }

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.nanoStartTimestamp = System.nanoTime();
        this.homeTeamScore = 0;
        this.awayTeamScore = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return nanoStartTimestamp == match.nanoStartTimestamp && Objects.equals(homeTeam, match.homeTeam) && Objects.equals(awayTeam, match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, awayTeam, nanoStartTimestamp);
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public long getNanoStartTimestamp() {
        return nanoStartTimestamp;
    }

    public int getHomeTeamScore() {
        return homeTeamScore;
    }

    public int getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public void setAwayTeamScore(int awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public String matchToken() {
        return String.format("%s/%s/%d", this.homeTeam.getNationality(),
                this.awayTeam.getNationality(), this.nanoStartTimestamp);
    }
}
