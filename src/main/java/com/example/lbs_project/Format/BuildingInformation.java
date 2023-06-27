package com.example.lbs_project.Format;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.feature.Feature;

import java.io.File;
import java.util.*;

public class BuildingInformation {
    public static String angle2Info(Double maxAngle, Double minAngle){
        String max  ="";
        if (maxAngle>=110){
            max = "Behind";
        }else if(70>=maxAngle){
            max ="Infont";
        }

        String min  ="";
        if (minAngle>=110){
            min = "Behind";
        }else if(70>=minAngle){
            min ="Infont";
        }
        String res = "";
        if (max == "Behind" & min == "Infont"){
            res = "";
        }else if (max == "Infont" & min == "Infont"){
            res = "Infront";
        }else if (max == "Behind" & min == "Behind"){
            res = "Behind";
        }
        return res;
    }


    public static String getClosestBuildingsInformations(SimpleFeatureCollection featureCollection, Coordinate current, Coordinate direction){
        Integer alfa = 20;

        Double maxAnlge  = 0.0;
        Double minAnlge  = 0.0;

        String left_street = "";
        String left_house  = "";
        String right_street = "";
        String right_house  = "";
        String behind_left_street = "";
        String behind_left_house = "";
        String front_left_street = "";
        String front_left_house = "";
        String behind_right_street = "";
        String behind_right_house = "";
        String front_right_street = "";
        String front_right_house = "";



        String Message_left = "";
        String Message_right = "";
        String Message = "";
        String Message_behind_right = "";
        String Message_behind_left = "";
        String Message_front_right = "";
        String Message_front_left = "";
        FeatureIterator iterator = featureCollection.features();
        try {
            Double distance_left = Double.POSITIVE_INFINITY;
            Double distance_behind_left = Double.POSITIVE_INFINITY;
            Double distance_front_left = Double.POSITIVE_INFINITY;
            Double distance_right = Double.POSITIVE_INFINITY;
            Double distance_behind_right = Double.POSITIVE_INFINITY;
            Double distance_front_right= Double.POSITIVE_INFINITY;



            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                String multyPoly = feature.getProperty("the_geom").getValue().toString();

                HashMap<String, Object> sub = buildingInformationForPoint(current, direction, multyPoly);
                String pos = (String) sub.get("position");
                Double distance = (double) sub.get("distance");
                double maxAngle = (double) sub.get("maxAngle");
                double minAngle = (double) sub.get("minAngle");
                if (pos == "left") {

                    if (minAngle > 90+alfa) {
                        // Behind Left
                        if (distance_behind_left > distance) {
                            behind_left_street = feature.getProperty("addr_stree").getValue().toString();
                            behind_left_house = feature.getProperty("addr_house").getValue().toString();
                            distance_behind_left = distance;


                        }
                    } else if (maxAngle < 90-alfa) {
                        if (distance_front_left > distance) {
                            front_left_street = feature.getProperty("addr_stree").getValue().toString();
                            front_left_house = feature.getProperty("addr_house").getValue().toString();
                            distance_front_left = distance;
                        }
                    } else {
                        if (distance_left > distance) {

                            left_street = feature.getProperty("addr_stree").getValue().toString();
                            left_house = feature.getProperty("addr_house").getValue().toString();
                            distance_left = distance;


                        }

                    }


                } else if (pos == "right") {
                    if (minAngle > 90+alfa) {
                        // Behind Right
                        if (distance_behind_right > distance) {
                            behind_right_street = feature.getProperty("addr_stree").getValue().toString();
                            behind_right_house = feature.getProperty("addr_house").getValue().toString();
                            distance_behind_right = distance;


                        }
                    } else if (maxAngle < 90-alfa) {
                        // Front Right
                        if (distance_front_right > distance) {
                            front_right_street = feature.getProperty("addr_stree").getValue().toString();
                            front_right_house = feature.getProperty("addr_house").getValue().toString();
                            distance_front_right = distance;
                        }

                    } else {
                        if (distance_right > distance) {
                            distance_right = distance;
                            right_street = feature.getProperty("addr_stree").getValue().toString();
                            right_house = feature.getProperty("addr_house").getValue().toString();


                        }
                    }


                }


            }


            if (distance_left<50){
                if(left_street.isEmpty()) {
                    if (!right_street.isEmpty()) {
                        if (left_house.isEmpty()){
                            left_street = right_street;
                            left_house = " Unknown Number";
                        }else{
                            left_street = right_street;
                        }
                    } else {
                        // When the street Information Adds, there will be fixed with street name
                        right_street = "Unknown Building";

                    }
                }
                Message_left = left_street + " " + left_house + " building is " + Math.round(distance_left) + " meters from you. And it is on your "
                        +  " left.";
            }
            if (distance_right<50){
                if(right_street.isEmpty()) {
                    if (!left_street.isEmpty()) {

                        if (right_house.isEmpty()) {
                            right_street = left_street;
                            right_house = " Unknown Number";
                        } else {
                            right_street = left_street;
                        }

                    } else {
                        // When the street Information Adds, there will be fixed with street name
                        right_street = "Unknown Building";
                    }
                    Message_right = right_street + " " + right_house + " building is " + Math.round(distance_right) + " meters from you. And it is on your "
                            + " right.";
                }

            }
            if (distance_behind_left<50){
                if (behind_left_street.isEmpty()){
                    behind_left_street = "Unkown";
                }
                Message_behind_right = behind_left_street +" "+behind_left_house+" building is " + Math.round(distance_behind_left) + " meters from you. And it is on your "
                        +  " behind left.";;
            }
            if (distance_behind_right<50){
                if (behind_right_street.isEmpty()){
                    behind_right_street = "Unkown";
                }
                Message_behind_right = behind_right_street +" "+behind_right_house+" building is " + Math.round(distance_behind_right) + " meters from you. And it is on your "
                        +  " behind right.";;
            }
            if (distance_front_left<50){
                if (front_left_street.isEmpty()){
                    front_left_street = "Unkown";
                }
                Message_front_right = front_left_street +" "+front_left_house+" building is " + Math.round(distance_front_left) + " meters from you. And it is on your "
                        +  " front left.";;
            }
            if (distance_front_right<50){
                if (front_right_street.isEmpty()){
                    front_right_street = "Unkown";
                }
                Message_front_right = front_right_street +" "+front_right_house+" building is " + Math.round(distance_front_right) + " meters from you. And it is on your "
                        +  " front right.";;
            }
            if(Message_left.isEmpty() & !Message_right.isEmpty()){
                Message = Message_right + " " + Message_behind_left +" "+ Message_front_left;

            }else if (!Message_left.isEmpty() & Message_right.isEmpty()){
                Message = Message_left + " " + Message_behind_right +" "+ Message_front_right;

            }else if (Message_left.isEmpty() & Message_right.isEmpty()){
                Message = Message_behind_left + " " + Message_front_left + " " + Message_behind_right +" "+ Message_front_right;
            }else{
                Message = Message_left + " " + Message_right;
            }
            return Message;

        }catch (Exception e ){
            System.out.println(e);
        } finally {
            iterator.close();
        }
        return null;

    }

    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }
    public static FeatureCollection readShapeFiles(String filename) {
        File file = new File(filename);

        try {
            Map<String, String> connect = new HashMap();
            connect.put("url", file.toURI().toString());

            DataStore dataStore = DataStoreFinder.getDataStore(connect);
            String[] typeNames = dataStore.getTypeNames();
            String typeName = typeNames[0];

            System.out.println("Reading content " + typeName);

            FeatureSource featureSource = dataStore.getFeatureSource(typeName);
            FeatureCollection collection = featureSource.getFeatures();




            return collection;

        } catch (Throwable e) { return null;}

    }

    public static double getAngle(Coordinate p1, Coordinate p2, Coordinate p3){
        double a = p2.distance(p3);
        double b = p2.distance(p1);
        double c = p1.distance(p3);
        double angle = Math.toDegrees(Math.acos((Math.pow(b,2)+Math.pow(c,2)-Math.pow(a,2))/(2*b*c)));

        return Math.round(angle);
    }
    public static String buildingPosition(Coordinate p1,Coordinate p2,Coordinate p3){
        double ax = p2.getX() - p1.getX();
        double ay = p2.getY() - p1.getY();
        double bx = p3.getX() - p1.getX();
        double by = p3.getY() - p1.getY();

        if (ax * by - ay * bx > 0.0){
            return "left";
        }else{
            return "right";
        }

    }
    public static HashMap<String,Object> buildingInformationForPoint(Coordinate current,Coordinate direction, String multiPoly){

        String[] points = multiPoly.split("[MULTYPOLIGON(),]");
        double last_distance = Double.POSITIVE_INFINITY;
        List<Double> angleList = new ArrayList<>();
        List<String> positions = new ArrayList<>();
        for(String point: points){
            String[] point_split = point.split(" ");
            try {
                if (point_split.length >= 2) {
                    double x = Double.valueOf(point_split[0]);
                    double y = Double.valueOf(point_split[1]);
                    Coordinate point1 = new Coordinate(x, y);
                    double distance = point1.distance(current);
                    if (distance < last_distance) {
                        last_distance = distance;
                    }
                    double angle = getAngle(current, direction, point1);
                    angleList.add(angle);

                    String pos = buildingPosition(current, direction, point1);
                    positions.add(pos);
                }
            }catch (Exception e){

            }
        }


        HashMap<String,Object> result = new HashMap<>();
        result.put("maxAngle", Collections.max(angleList));
        result.put("minAngle", Collections.min(angleList));
        result.put("position",mostCommon(positions));
        result.put("distance",last_distance);





        return result;
    }

}
