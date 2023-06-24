package com.example.lbs_project.Service;

import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShortestPathService {
    public static List<Point2D.Double> getShortestPathCoordinates(Double x1,Double y1, Double x2,Double y2){
        ArrayList<Point2D.Double> doubles = new ArrayList<>();
        doubles.add(new Point2D.Double(1.1,2.2));
         return doubles;
    }
}
