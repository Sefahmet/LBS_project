package com.example.lbs_project.DataHolder;

import com.example.lbs_project.Entity.GraphFeatures;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.GraphPath;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class MyDataSingleton {
    @Getter @Setter private GraphFeatures graphFeatures;
    @Getter @Setter private GraphPath shortestPath;

    @Autowired
    public MyDataSingleton(String path) throws IOException {
        this.graphFeatures = GraphFeatures.getInstance(path);
    }

    @Autowired
    public MyDataSingleton() throws IOException {
        this.graphFeatures = GraphFeatures.getInstance("none");
    }
}
