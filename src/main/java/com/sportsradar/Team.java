package com.sportsradar;


import java.util.Objects;

// Whole new class is not required for this task
// but let's implement it anyway in case of further
// library extensions
public class Team {
    private final String nationality;

    public Team(String nationality) {
        String n = nationality.toLowerCase();
        this.nationality = Character.toUpperCase(n.charAt(0))
                + n.substring(1);
    }

    public String getNationality() {
        return nationality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(nationality, team.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nationality);
    }
}
