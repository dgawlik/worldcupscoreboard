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

        Assertions.assertThatThrownBy(() -> scoreboard.updateMatch("fake", 1, 0));
    }

    @Test
    @DisplayName("Error on removing nonexistent match")
    public void removing_nonexistent_match_test(){
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var scoreboard = new ScoreBoard();
        scoreboard.startNewMatch(t1, t2);

        Assertions.assertThatThrownBy(() -> scoreboard.removeMatch("Poland/England"));
    }

    @Test
    @DisplayName("Cannot start match second time")
    public void same_match_second_time_test(){
        var t1 = new Team("Poland");
        var t2 = new Team("Germany");

        var scoreboard = new ScoreBoard();
        scoreboard.startNewMatch(t1, t2);

        Assertions.assertThatThrownBy(() -> scoreboard.startNewMatch(t1, t2));
    }

}