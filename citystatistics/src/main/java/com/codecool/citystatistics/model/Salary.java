package com.codecool.citystatistics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salary {
    public String title;
    public int percentile_25;
    public int percentile_50;
    public int percentile_75;

}
