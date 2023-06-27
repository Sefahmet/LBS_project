package com.example.lbs_project.Entity;

import com.example.lbs_project.Service.GraphFeaturesGetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class GraphFeatures {
    @Getter @Setter private Graph<Node, DefaultWeightedEdge> graph;
    @Getter @Setter private HashMap<String,Node> nodeHashMap;
    @Getter @Setter private HashMap<List<String>,Edge> edgeHashMap;
    @Getter @Setter private SimpleFeatureCollection building;
    private static GraphFeatures instance;
    public static GraphFeatures getInstance() throws IOException {
        if (instance == null){
            instance = GraphFeaturesGetter.getGraphFeatrues();
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

}
