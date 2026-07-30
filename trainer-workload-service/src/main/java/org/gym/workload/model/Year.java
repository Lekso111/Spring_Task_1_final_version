package org.gym.workload.model;

import java.util.ArrayList;
import java.util.List;

public class Year {

    private int year;
    private List<Month> months = new ArrayList<>();

    public Year() {
    }

    public Year(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Month> getMonths() {
        return months;
    }

    public void setMonths(List<Month> months) {
        this.months = months;
    }

    public Month findMonth(int month) {
        for (Month candidate : months) {
            if (candidate.getMonth() == month) {
                return candidate;
            }
        }
        return null;
    }

    public Month findOrCreateMonth(int month) {
        Month existing = findMonth(month);
        if (existing != null) {
            return existing;
        }
        Month created = new Month(month);
        months.add(created);
        return created;
    }
}
