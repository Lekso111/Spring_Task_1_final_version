package org.gym.workload.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "trainer_workloads")
@CompoundIndex(name = "idx_trainer_first_last_name", def = "{'firstName': 1, 'lastName': 1}")
public class TrainerWorkload {

    @Id
    private String username;
    private String firstName;
    private String lastName;
    private boolean active;
    private List<Year> years = new ArrayList<>();

    public TrainerWorkload() {
    }

    public TrainerWorkload(String username, String firstName, String lastName, boolean active) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Year> getYears() {
        return years;
    }

    public void setYears(List<Year> years) {
        this.years = years;
    }

    public void addDuration(int year, int month, double duration) {
        Year targetYear = findOrCreateYear(year);
        Month targetMonth = targetYear.findOrCreateMonth(month);
        targetMonth.setSummaryDuration(targetMonth.getSummaryDuration() + duration);
    }

    public void subtractDuration(int year, int month, double duration) {
        Year target = findYear(year);
        if (target == null) {
            return;
        }
        Month found = target.findMonth(month);
        if (found == null) {
            return;
        }
        double remaining = found.getSummaryDuration() - duration;
        found.setSummaryDuration(Math.max(remaining, 0));
    }

    private Year findYear(int year) {
        for (Year candidate : years) {
            if (candidate.getYear() == year) {
                return candidate;
            }
        }
        return null;
    }

    private Year findOrCreateYear(int year) {
        Year existing = findYear(year);
        if (existing != null) {
            return existing;
        }
        Year created = new Year(year);
        years.add(created);
        return created;
    }
}
