package com.sportsradar;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TeamTest {

    @Test
    @DisplayName("Team should be identified by nationality only")
    public void team_id_test(){
        var team1 = new Team("poland");
        var team2 = new Team("poland");

        Assertions.assertThat(team1).isEqualTo(team2);

        var team3 = new Team("switzerland");

        Assertions.assertThat(team1).isNotEqualTo(team3);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Poland", "pOLAND", "POLAND", "pOlAnD"})
    @DisplayName("Team class should standarize country names")
    public void team_country_name_standard_test(String countryName){
        Assertions.assertThat(new Team(countryName).getNationality())
                .isEqualTo("Poland");
    }
}
