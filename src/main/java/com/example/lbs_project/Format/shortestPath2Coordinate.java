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
        List<Coordinate> newList = new ArrayList<>();
        int t = vertexList.toArray().length;
        for (int i = 0;i<t-1;i++){


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
    public static Coordinate calculateProjection(double x1, double y1, double x2, double y2, double x, double y) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        double t = ((x - x1) * dx + (y - y1) * dy) / (dx * dx + dy * dy);

        double projX, projY;
        if (t < 0) {
            projX = x1;
            projY = y1;
        } else if (t > 1) {
            projX = x2;
            projY = y2;
        } else {
            projX = x1 + t * dx;
            projY = y1 + t * dy;
        }

        return new Coordinate(projX, projY);
    }
}
