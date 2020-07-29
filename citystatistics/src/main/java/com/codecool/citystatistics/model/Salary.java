package com.codecool.citystatistics.model;

public class Salary {
    public String title;
    public int percentile_25;
    public int percentile_50;
    public int percentile_75;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPercentile_25(int percentile_25) {
        this.percentile_25 = percentile_25;
    }

    public void setPercentile_50(int percentile_50) {
        this.percentile_50 = percentile_50;
    }

    public void setPercentile_75(int percentile_75) {
        this.percentile_75 = percentile_75;
    }
}
