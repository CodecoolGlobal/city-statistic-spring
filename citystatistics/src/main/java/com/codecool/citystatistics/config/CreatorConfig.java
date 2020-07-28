package com.codecool.citystatistics.config;

import com.codecool.citystatistics.instance_creator.ApiCallCreator;
import com.codecool.citystatistics.instance_creator.RemoteURLReaderCreator;
import com.codecool.citystatistics.model.ApiCall;
import com.codecool.citystatistics.model.RemoteURLReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreatorConfig {

    @Autowired
    private RemoteURLReaderCreator remoteURLReaderCreator;

    @Autowired
    private ApiCallCreator apiCallCreator;

    @Bean
    public RemoteURLReader remoteURLReader(){return remoteURLReaderCreator.createRemoteURLReader();}

    @Bean
    public ApiCall apiCall(){return apiCallCreator.createApiCall();}
}
