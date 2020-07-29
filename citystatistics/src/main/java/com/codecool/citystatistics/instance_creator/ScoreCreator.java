package com.codecool.citystatistics.instance_creator;

import com.codecool.citystatistics.model.Score;
import org.springframework.stereotype.Component;

@Component
public class ScoreCreator {
    public Score createScore(){
        return new Score();
    }
}
