package com.example.lbs_project.Service;

import com.example.lbs_project.DataHolder.MyDataSingleton;
import com.example.lbs_project.Entity.GraphFeatures;
import com.example.lbs_project.Entity.Node;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

@Service
public class ShortestPathService {

    public static MyDataSingleton getShortestPathCoordinates( Double x1, Double y1, Double x2, Double y2){
        try{
            MyDataSingleton data = new MyDataSingleton();

            GraphFeatures graphFeatures = data.getGraphFeatures();
            Graph<Node, DefaultWeightedEdge> graph = graphFeatures.getGraph();
            // Start ve end noktalarını temsil eden Coordinate objeleri oluşturuldu
            Coordinate start = new Coordinate(x1, y1);
            Coordinate end = new Coordinate(x2, y2);
            Set<DefaultWeightedEdge> edgeset = graph.edgeSet();
            Iterator iterator = edgeset.iterator();
            while(iterator.hasNext()){
                DefaultWeightedEdge edgee = (DefaultWeightedEdge) iterator.next();
                Node source = graph.getEdgeSource(edgee);
                Node target = graph.getEdgeTarget(edgee);
            }

            // En kısa yolu hesapla
            DijkstraShortestPath<Node, DefaultWeightedEdge> shortestPathAlg = new DijkstraShortestPath<>(graph);
            // Get closest node on graph
            Node startNode = getClosestNode(graphFeatures.getNodeHashMap(),start);
            Node endNode = getClosestNode(graphFeatures.getNodeHashMap(),end);
            GraphPath<Node, DefaultWeightedEdge> shortestPath = shortestPathAlg.getPath(startNode,endNode);
            data.setShortestPath(shortestPath);
            return data;
        }catch (Exception e){
            System.out.println(e);
        }


        return null;
    }
    private static Node getClosestNode( HashMap<String, Node> nodeHashMap,Coordinate point){
        Double distance= Double.POSITIVE_INFINITY;
        Node resNode=null;
        for (String key:nodeHashMap.keySet()
             ) {
            Node testNode = nodeHashMap.get(key);
            Double d =Math.hypot(testNode.getEast()-point.getX(),
                    testNode.getNorth()-point.getY());
            if(d<distance){
                distance = d;
                resNode = testNode;
                //System.out.println(resNode.getEast() +" " + resNode.getNorth()+ " " + d);
            }

        }
        return resNode;
    }
}
