package org.gym.workload.model;

import java.util.ArrayList;
import java.util.List;

public class Year {

    private final int year;
    private final List<Month> months = new ArrayList<>();

    public Year(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public List<Month> getMonths() {
        return months;
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
