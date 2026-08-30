package org.gym.workload.model;

public class Month {

    private int month;
    private double summaryDuration;

    public Month() {
    }

    public Month(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getSummaryDuration() {
        return summaryDuration;
    }

    public void setSummaryDuration(double summaryDuration) {
        this.summaryDuration = summaryDuration;
    }
}
