package com.sportsradar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class ScoreBoardTest {

    @Test
    @DisplayName("Scoreboard should create new matches")
    public void create_matches() {
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var scoreboard = new ScoreBoard();

        var handle = scoreboard.startNewMatch(t1, t2);

        Assertions.assertThat(handle).startsWith("Poland/Germany/");
        System.out.println(handle);
    }

    @Test
    @DisplayName("Scoreboard should update old matches")
    public void update_matches() {
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var t3 = new Team("England");
        var t4 = new Team("India");

        var scoreboard = new ScoreBoard();

        var h1 = scoreboard.startNewMatch(t1, t2);
        var h2 = scoreboard.startNewMatch(t3, t4);

        scoreboard.updateMatch(h1, 1, 1);
        scoreboard.updateMatch(h2, 2, 1);
    }

    @Test
    @DisplayName("Scoreboard should remove old matches")
    public void remove_matches(){
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var t3 = new Team("England");
        var t4 = new Team("India");

        var scoreboard = new ScoreBoard();

        var h1 = scoreboard.startNewMatch(t1, t2);
        var h2 = scoreboard.startNewMatch(t3, t4);

        scoreboard.removeMatch(h1);
        scoreboard.removeMatch(h2);
    }

    @Test
    @DisplayName("Scoreboard should have positions in correct order")
    public void correct_order() {
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var t3 = new Team("England");
        var t4 = new Team("India");

        var t5 = new Team("Australia");
        var t6 = new Team("New Zealand");

        var scoreboard = new ScoreBoard();

        var h1 = scoreboard.startNewMatch(t1, t2);
        var h2 = scoreboard.startNewMatch(t3, t4);
        var h3 = scoreboard.startNewMatch(t5, t6);

        List<BoardPosition> poss = scoreboard.showBoard();

        var reduced = poss.stream().map(BoardPosition::home).toList();
        Assertions.assertThat(reduced).isEqualTo(List.of("Australia", "England", "Poland"));

        scoreboard.updateMatch(h2, 1, 1);

        poss = scoreboard.showBoard();

        reduced = poss.stream().map(BoardPosition::home).toList();
        Assertions.assertThat(reduced).isEqualTo(List.of("England", "Australia", "Poland"));
    }

    @Test
    @DisplayName("Error on updating nonexistent match")
    public void nonexistent_match_test(){
        var scoreboard = new ScoreBoard();

        Assertions.assertThatCode(() -> scoreboard.updateMatch("fake", 1, 0))
                        .hasMessage("The match does not exist!");
    }

    @Test
    @DisplayName("Error on removing nonexistent match")
    public void removing_nonexistent_match_test(){
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var scoreboard = new ScoreBoard();
        scoreboard.startNewMatch(t1, t2);

        Assertions.assertThatCode(() -> scoreboard.removeMatch("Poland/England"))
                        .hasMessage("The match does not exist");
    }

    @Test
    @DisplayName("Cannot start match second time")
    public void same_match_second_time_test(){
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var scoreboard = new ScoreBoard();
        scoreboard.startNewMatch(t1, t2);

        Assertions.assertThatCode(() -> scoreboard.startNewMatch(t1, t2))
                        .hasMessage("No team can participate in 2 concurrent matches");
    }

    @Test
    @DisplayName("Scoreboard throws on starting new match - null")
    public void start_match_null(){
        var scoreboard = new ScoreBoard();

        Assertions.assertThatCode(() -> scoreboard.startNewMatch(null, null))
                .hasMessage("Home team is null");
    }

    @Test
    @DisplayName("Scoreboard throws on starting match with same team")
    public void start_match_same_team() {
        var t = new Team("Poland");
        var scoreboard = new ScoreBoard();

        Assertions.assertThatCode(() -> scoreboard.startNewMatch(t, t))
                .hasMessage("Home and away teams must differ");
    }

    @Test
    @DisplayName("Scoreboard update handle is null")
    public void update_match_handle_is_null(){
        var scoreboard = new ScoreBoard();

        Assertions.assertThatCode(() -> scoreboard.updateMatch(null, 1, 1))
                .hasMessage("Handle must be not blank");
    }

    @Test
    @DisplayName("Scoreboard update score is negative")
    public void update_match_handle_is_negative(){
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var scoreboard = new ScoreBoard();

        var m = scoreboard.startNewMatch(t1, t2);

        Assertions.assertThatCode(() -> scoreboard.updateMatch(m, -1, 1))
                .hasMessage("Home score must be equal or greater than zero!");
    }

    @Test
    @DisplayName("Scoreboard remove handle is null")
    public void remove_handle_is_null() {
        var scoreboard = new ScoreBoard();

        Assertions.assertThatCode(() -> scoreboard.removeMatch(null))
                .hasMessage("Handle is null");
    }

}