package com.codecool.citystatistics.instance_creator;

import com.codecool.citystatistics.model.RemoteURLReader;
import org.springframework.stereotype.Component;

@Component
public class RemoteURLReaderCreator {
    public RemoteURLReader createRemoteURLReader(){
        return new RemoteURLReader();
    }
}
