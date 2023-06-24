package com.example.lbs_project.Entity;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;

public class GraphFeatures {
    private Graph<Node, DefaultWeightedEdge> graph;
    private HashMap<String,Node> nodeHashMap;
    private HashMap<String[],Edge> edgeHashMap;


    public HashMap<String, Node> getNodeHashMap() {
        return nodeHashMap;
    }

    public HashMap<String[], Edge> getEdgeHashMap() {
        return edgeHashMap;
    }

    public Graph<Node, DefaultWeightedEdge> getGraph() {return graph;}


    public void setEdgeHashMap(HashMap<String[], Edge> edgeHashMap) {
        this.edgeHashMap = edgeHashMap;
    }

    public void setNodeHashMap(HashMap<String, Node> nodeHashMap) {
        this.nodeHashMap = nodeHashMap;
    }

    public void setGraph(Graph<Node, DefaultWeightedEdge> graph) {this.graph = graph;}
}
