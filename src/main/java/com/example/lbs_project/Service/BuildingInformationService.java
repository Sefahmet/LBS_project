package com.example.lbs_project.Service;

import com.example.lbs_project.DataHolder.MyDataSingleton;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.example.lbs_project.Format.BuildingInformation.getClosestBuildingsInformations;

@Service
public class BuildingInformationService {

    public static Double distanceEdge2Point(double x1, double y1, double x2, double y2, double x,double y) {


        // slope
        double m = (y2 - y1) / (x2 - x1);
        double b = y1 - m * x1;

        // distance
        double distance = Math.abs((m * x - y + b)) / Math.sqrt(m * m + 1);

        return distance;
    }
    public String getNearestBuildingInformation(Double x,Double y, List<Coordinate> shortestPath) throws IOException {
        int size = shortestPath.size();
        Double distance = Double.POSITIVE_INFINITY;
        Coordinate direction = new Coordinate(0,0);
        for (int i = 0 ; i<size-1;i++){
            Coordinate p1 = shortestPath.get(i);
            Coordinate p2 = shortestPath.get(i+1);
            double d = distanceEdge2Point(p1.getX(),p1.getY(), p2.getX(), p2.getY(), x,y);
            if (d<distance){
                distance = d;
                direction = p2;
            }
        }

        MyDataSingleton data = new MyDataSingleton();
        String Message = getClosestBuildingsInformations(data.getGraphFeatures().getBuilding(),new Coordinate(x,y),direction);
        return Message;
    }
}
