package com.example.lbs_project.Service;

import com.example.lbs_project.Entity.GraphFeatures;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.lbs_project.Format.CreateGraphFeature.createGraphNodeAndEdgeFile;
import static com.example.lbs_project.Format.ReadSHPFiles.shapeFileReader;
@Service
public class GraphFeaturesGetter {


    public static GraphFeatures getGraphFeatrues() throws IOException {
        String edgeFilePath = "src/main/resources/Data/edges_ne.shp";
        String nodeFilePath = "src/main/resources/Data/nodes_ne.shp";
        GraphFeatures graphFeatures= createGraphNodeAndEdgeFile(edgeFilePath,nodeFilePath);
        SimpleFeatureCollection building = shapeFileReader("src/main/resources/Data/Buildings.shp");
        graphFeatures.setBuilding(building);
        System.out.println("I am loading Data");
        return graphFeatures;
    }
}
