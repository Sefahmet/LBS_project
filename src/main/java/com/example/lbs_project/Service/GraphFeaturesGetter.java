package com.example.lbs_project.Service;

import com.example.lbs_project.Entity.GraphFeatures;
import com.example.lbs_project.exception.NotFoundException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.lbs_project.Format.CreateGraphFeature.createGraphNodeAndEdgeFile;
import static com.example.lbs_project.Format.ReadSHPFiles.shapeFileReader;

@Service
public class GraphFeaturesGetter {


    public GraphFeatures getGraphFeatures(String directoryLocation) throws IOException {
        String edgeFilePath = directoryLocation + "/Data/edges_ne.shp";
        String nodeFilePath = directoryLocation +"/Data/nodes_ne.shp";
        try {

            SimpleFeatureCollection building = shapeFileReader(directoryLocation + "/Data/Buildings.shp");

            GraphFeatures graphFeatures = createGraphNodeAndEdgeFile(edgeFilePath, nodeFilePath);
            graphFeatures.setBuilding(building);

            return graphFeatures;
        } catch (IOException e) {
            throw new NotFoundException("Data cannot be found at " + edgeFilePath);
        }
    }
}
