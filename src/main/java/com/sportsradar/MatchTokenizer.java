package com.sportsradar;

import java.util.*;
import java.util.stream.Collectors;

public class MatchTokenizer {

    private final Map<String, String> isoCountries = new HashMap<>();

    public MatchTokenizer() {

        for (String iso : Locale.getISOCountries()) {
            var disp = Locale.of("", iso)
                    .getDisplayCountry(Locale.of("en"));
            isoCountries.put(disp, iso);
        }
    }

    public String tokenForMatch(Match match) {
        String home = findAbbreviation(match.getHomeTeam().getNationality());
        String away = findAbbreviation(match.getAwayTeam().getNationality());
        long start = match.getNanoStartTimestamp();

        return String.format("%s/%s/%d", home, away, start);
    }


    private String findAbbreviation(String country) {
        if (isoCountries.containsKey(country)) {
            return isoCountries.get(country);
        } else {
            throw new IllegalArgumentException("ISO abbrev. for country does not exist!");
        }
    }
}
