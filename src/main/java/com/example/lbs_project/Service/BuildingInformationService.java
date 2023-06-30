package com.example.lbs_project.Service;

import com.example.lbs_project.DataHolder.MyDataSingleton;
import com.example.lbs_project.Entity.Edge;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.example.lbs_project.Format.BuildingInformation.getClosestBuildingsInformations;

@Service
public class BuildingInformationService {
    public static Double distanceEdge2Point(double x1, double y1, double x2, double y2, double x,double y) {


        // Calculate the length between the start and end points of the line segment
        double length = Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));

        if (length == 0) {
            // If the line segment has zero length, return the distance from the point to the start point
            return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
        }

        // Calculate the slope and y-intercept of the line segment
        double m = (y2 - y1) / (x2 - x1);
        double b = y1 - m * x1;

        // Calculate the projection of the point onto the line segment
        double proj_x = (m * (y - y1) + x + Math.pow(m, 2) * x1) / (1 + Math.pow(m, 2));
        double proj_y = m * (proj_x - x1) + y1;

        if ((x1 <= proj_x && proj_x <= x2) || (x2 <= proj_x && proj_x <= x1)) {
            // If the projection point is on the line segment, return the distance from the point to the projection point
            return Math.sqrt(Math.pow((x - proj_x), 2) + Math.pow((y - proj_y), 2));
        }

        // If the projection point is outside the line segment, return the distance from the point to the start or end point
        double distance1 = Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
        double distance2 = Math.sqrt(Math.pow((x - x2), 2) + Math.pow((y - y2), 2));
        return Math.min(distance1, distance2);
    }
    public String getNearestBuildingInformation(Double x,Double y, List<Coordinate> shortestPath) throws IOException {
        int size = shortestPath.size();
        Double distance = Double.POSITIVE_INFINITY;
        Coordinate direction = new Coordinate(0,0);
        Coordinate source = new Coordinate(0,0);
        for (int i = 0 ; i<size-1;i++){
            Coordinate p1 = shortestPath.get(i);
            Coordinate p2 = shortestPath.get(i+1);
            double d = distanceEdge2Point(p1.getX(),p1.getY(), p2.getX(), p2.getY(), x,y);
            if (d<distance){
                distance = d;
                source = p1;
                direction = p2;

            }
        }
        MyDataSingleton data = new MyDataSingleton();
        String streenName = getStreetName(source,direction,data.getGraphFeatures().getEdgeHashMap());
        String Message = getClosestBuildingsInformations(data.getGraphFeatures().getBuilding(),new Coordinate(x,y),direction);
        System.out.println(streenName);
        if (streenName != null){
            Message = "You are on the "+ streenName+ " Street." + Message;
        }
        return Message;


    }
    public String getStreetName(Coordinate p1, Coordinate p2, HashMap<List<String>, Edge> edgeHashMap){
        boolean flag1 = false;
        String streetName = "";
        for (List<String> edgeKey: edgeHashMap.keySet()) {
            Edge edge = edgeHashMap.get(edgeKey);
            List<Coordinate> coordinates = edge.getEdgeCoordinates();
            for (int i =0; i<coordinates.size()-1;i++){
                Coordinate ep1 = coordinates.get(i);
                Coordinate ep2 = coordinates.get(i+1);

                if ((ep1==p1 & ep2 == p2)| (ep1==p2 & ep2 == p1)){
                    flag1 = true;
                    streetName = edge.getStreetName();
                }
            }
            if (flag1){
                break;
            }

        }
        return streetName;
    }
}
