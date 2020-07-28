package com.codecool.citystatistics.instance_creator;

import com.codecool.citystatistics.model.ApiCall;
import com.codecool.citystatistics.model.RemoteURLReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiCallCreator {

    @Autowired
    private RemoteURLReader remoteURLReader;

    public ApiCall createApiCall(){
        return new ApiCall(remoteURLReader);
    }
}
