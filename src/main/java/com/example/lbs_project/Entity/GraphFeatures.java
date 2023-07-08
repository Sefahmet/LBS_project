package com.example.lbs_project.Entity;

import com.example.lbs_project.exception.NotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.example.lbs_project.Format.CreateGraphFeature.createGraphNodeAndEdgeFile;
import static com.example.lbs_project.Format.ReadSHPFiles.shapeFileReader;

@NoArgsConstructor
public class GraphFeatures {
    @Getter @Setter private Graph<Node, DefaultWeightedEdge> graph;
    @Getter @Setter private HashMap<String,Node> nodeHashMap;
    @Getter @Setter private HashMap<List<String>,Edge> edgeHashMap;
    @Getter @Setter private SimpleFeatureCollection building;
    private static GraphFeatures instance;
    public static GraphFeatures getInstance(String path) throws IOException {
        if (instance == null){
            instance = getGraphFeatures(path);
        }
        return instance;
    }
    public GraphFeatures(Graph<Node, DefaultWeightedEdge> graph, HashMap<String, Node> nodeHashMap, HashMap<List<String>, Edge> edgeHashMap){
        this.graph = graph;
        this.edgeHashMap = edgeHashMap;
        this.nodeHashMap = nodeHashMap;
    }
    public GraphFeatures(Graph<Node, DefaultWeightedEdge> graph,HashMap<String,Node> nodeHashMap,HashMap<List<String>,Edge> edgeHashMap,SimpleFeatureCollection building){
        this.graph = graph;
        this.edgeHashMap = edgeHashMap;
        this.nodeHashMap = nodeHashMap;
        this.building = building;

    }
    public static GraphFeatures getGraphFeatures(String directoryLocation) throws IOException {
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
