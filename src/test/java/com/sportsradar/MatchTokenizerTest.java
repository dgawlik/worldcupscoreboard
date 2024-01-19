package com.sportsradar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MatchTokenizerTest {

    @Test
    @DisplayName("MatchTokenizer should generate valid token for valid match")
    public void valid_test(){
        var team1 = new Team("Poland");
        var team2 = new Team("Germany");

        var match = new Match(team1, team2, 12L, 0, 0);

        var tokenizer = new MatchTokenizer();

        Assertions.assertThat(tokenizer.tokenForMatch(match)).isEqualTo("PL/DE/12");
    }

    @Test
    @DisplayName("MatchTokenizer will panic when nonexistent country given")
    public void wrong_country_invalid_test() {
        var team1 = new Team("Third Reich");
        var team2 = new Team("Atlantis");

        var match = new Match(team1, team2, 12L, 0, 0);

        var tokenizer = new MatchTokenizer();

        Assertions.assertThatThrownBy(() -> tokenizer.tokenForMatch(match));
    }
}
