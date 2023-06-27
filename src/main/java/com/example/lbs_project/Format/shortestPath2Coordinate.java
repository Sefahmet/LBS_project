package com.example.lbs_project.Format;

import com.example.lbs_project.DataHolder.MyDataSingleton;
import com.example.lbs_project.Entity.Edge;
import com.example.lbs_project.Entity.Node;
import org.jgrapht.GraphPath;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class shortestPath2Coordinate {
    public static List<Coordinate> converter(MyDataSingleton data){
        GraphPath shortestPath = data.getShortestPath();
        List<Node> vertexList = shortestPath.getVertexList();



        Iterator iterator = shortestPath.getEdgeList().iterator();
        HashMap<List<String>, Edge> edgeHashMap = data.getGraphFeatures().getEdgeHashMap();
        System.out.println(edgeHashMap.keySet());
        List<Coordinate> newList = new ArrayList<>();
        int t = vertexList.toArray().length;
        for (int i = 0;i<t-1;i++){
            System.out.println(iterator.next());


            Node p1 = vertexList.get(i);
            Node p2 = vertexList.get(i+1);
            String u_id = p1.getOsmid();
            String v_id = p2.getOsmid();
            List<String> uv = new ArrayList<>();
            uv.add(u_id);
            uv.add(v_id);
            Edge edge = edgeHashMap.get(uv);
            List<Coordinate> coords = edge.getEdgeCoordinates();
            newList.addAll(coords);

        }
        return newList;
    }
}
