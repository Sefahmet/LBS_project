package com.example.lbs_project.Format;

import com.example.lbs_project.Entity.Edge;
import com.example.lbs_project.Entity.GraphFeatures;
import com.example.lbs_project.Entity.Node;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiLineString;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.lbs_project.Format.ReadSHPFiles.shapeFileReader;

public class CreateGraphFeature {
    private static List<Coordinate> reverseList(List<Coordinate> coords){
        Iterator iterator = coords.iterator();
        List<Coordinate>  reverseCoords = new ArrayList<>();
        for(int i = coords.toArray().length-1;i>=0;i--){
            reverseCoords.add(coords.get(i));
        }
        return reverseCoords;
    }
    public static GraphFeatures createGraphNodeAndEdgeFile(String edgeFilePath, String nodeFilePath){
        try {
            // Read the Shapefile


            Graph<Node, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

            SimpleFeatureCollection nodefeatures = shapeFileReader(nodeFilePath);
            SimpleFeatureIterator nodeiterator =  nodefeatures.features();
            HashMap<String,Node> nodeHashMap = new HashMap<>();


            while(nodeiterator.hasNext()){
                SimpleFeature nodeFeature = nodeiterator.next();
                String osmid = (String) nodeFeature.getAttribute("osmid");
                Double east = (Double) nodeFeature.getAttribute("east");
                Double north =(Double) nodeFeature.getAttribute("north");
                Double lat =  (Double) nodeFeature.getAttribute("y");
                Double lon =  (Double) nodeFeature.getAttribute("x");
                Node node = new Node(osmid,east,north,lat,lon);
                graph.addVertex(node);

                nodeHashMap.put(osmid,node);

            }
            nodeiterator.close();


            SimpleFeatureCollection features = shapeFileReader(edgeFilePath);

            // Extract road geometries
            SimpleFeatureIterator iterator = features.features();
            HashMap<List<String>, Edge> edgeHashMap = new HashMap<>();

            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                MultiLineString lineString = (MultiLineString) feature.getAttribute("the_geom");
                List<Coordinate> coords = List.of(lineString.getCoordinates());

                String fid = String.valueOf(feature.getAttribute("fid"));

                String name = String.valueOf(feature.getAttribute("name"));
                String u_id = String.valueOf(feature.getAttribute("u"));
                String v_id = String.valueOf(feature.getAttribute("v"));
                List<String> uv = new ArrayList<>();
                uv.add(u_id);
                uv.add(v_id);
                List<String> vu = new ArrayList<>();
                vu.add(v_id);
                vu.add(u_id);
                Node u =nodeHashMap.get(u_id);
                Node v =nodeHashMap.get(v_id);
               if(Math.abs(u.getEast()-coords.get(0).getX())>0.01){
                    if(Math.abs(u.getEast()-coords.get(coords.size()-1).getX())<0.01){
                        coords = reverseList(coords);
                    }else{
                        System.out.println(fid+" "+ "false check others");
                    }

                }
                name = ((name.isEmpty()) ? null : name);

                Double distance = 0.0;

                for (int i = 0; i<lineString.getNumPoints()-1;i++) {
                    Coordinate p1 = coords.get(i);
                    Coordinate p2 = coords.get(i+1);
                    distance += Math.hypot(p1.getX()-p2.getX(),p1.getY()-p2.getY());

                }
                Edge edge = new Edge(fid,u_id,v_id,u,v,distance,coords,name);
                DefaultWeightedEdge graphEdge = graph.addEdge(u, v);
                if (graphEdge!=null){
                    graph.setEdgeWeight(graphEdge,edge.getDistance());
                    edgeHashMap.put(uv,edge);
                }else{
                    Edge oldEdge = edgeHashMap.get(uv);
                    if (oldEdge.getDistance()>edge.getDistance()){
                        edgeHashMap.get(uv).setDistance(edge.getDistance());
                        edgeHashMap.get(uv).setEdgeCoordinates(edge.getEdgeCoordinates());
                        edgeHashMap.get(uv).setFid(edge.getFid());
                        graph.setEdgeWeight(graph.getEdge(u,v),edge.getDistance());
                    }

                }
                String reverseFid = String.valueOf(10000.0 - Double.valueOf(fid));
                Edge reverseEdge = new Edge(reverseFid,v_id,u_id,v,u,distance, reverseList(coords),name);
                DefaultWeightedEdge graphReverseEdge = graph.addEdge(v, u);
                if (graphReverseEdge!=null){
                    graph.setEdgeWeight(graphReverseEdge,edge.getDistance());
                    edgeHashMap.put(vu,reverseEdge);
                }else{
                    Edge oldEdge = edgeHashMap.get(vu);
                    if (oldEdge.getDistance()>reverseEdge.getDistance()){
                        edgeHashMap.get(vu).setDistance(reverseEdge.getDistance());
                        edgeHashMap.get(vu).setEdgeCoordinates(reverseEdge.getEdgeCoordinates());
                        graph.setEdgeWeight(graph.getEdge(v,u),reverseEdge.getDistance());
                    }

                }

            }
            iterator.close();
            GraphFeatures graphFeatures = new GraphFeatures(graph,nodeHashMap,edgeHashMap);

            return graphFeatures;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
