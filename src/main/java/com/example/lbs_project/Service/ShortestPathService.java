package com.example.lbs_project.Service;

import com.example.lbs_project.DataHolder.MyDataSingleton;
import com.example.lbs_project.Entity.Edge;
import com.example.lbs_project.Entity.GraphFeatures;
import com.example.lbs_project.Entity.Node;
import com.example.lbs_project.Format.shortestPath2Coordinate;
import geotrellis.proj4.CRS;
import geotrellis.proj4.Transform;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.*;

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
            Node[] nodes = getClosestNode(graphFeatures.getEdgeHashMap(),start);
            Node startNode1 = nodes[0];
            Node startNode2 = nodes[1];
            Node[]endNodes = getClosestNode(graphFeatures.getEdgeHashMap(),end);
            Node endNode1 = endNodes[0];
            Node endNode2 = endNodes[1];

            GraphPath<Node, DefaultWeightedEdge> shortestPath11 = shortestPathAlg.getPath(startNode1,endNode1);
            GraphPath<Node, DefaultWeightedEdge> shortestPath21 = shortestPathAlg.getPath(startNode2,endNode1);
            GraphPath<Node, DefaultWeightedEdge> shortestPath12 = shortestPathAlg.getPath(startNode1,endNode2);
            GraphPath<Node, DefaultWeightedEdge> shortestPath22 = shortestPathAlg.getPath(startNode2,endNode2);
            double w1 = shortestPath11.getWeight();
            double w2 = shortestPath12.getWeight();
            double w3 = shortestPath21.getWeight();
            double w4 = shortestPath22.getWeight();
            if (Math.min(w1,w2)<Math.min(w3,w4)){
                data.setShortestPath(shortestPath11);
            }else if(Math.min(w2,w1)<Math.min(w3,w4)){
                data.setShortestPath(shortestPath12);
            }else if(Math.min(w3,w1)<Math.min(w2,w4)){
                data.setShortestPath(shortestPath21);
            }else{
                data.setShortestPath(shortestPath22);
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
        }


        return null;
    }
    public static List<String> getEdgeIds (MyDataSingleton data){
        List<Node> nodes = data.getShortestPath().getVertexList();
        List<String> fids = new ArrayList<>();
        for (int i =0;i< nodes.size()-1;i++
        ) {
            List<String> ids = new ArrayList<>();

            String u_id=  nodes.get(i).getOsmid();
            String v_id=  nodes.get(i+1).getOsmid();
            ids.add(u_id);
            ids.add(v_id);

            fids.add(data.getGraphFeatures().getEdgeHashMap().get(ids).getFid());
        }
        return fids;
    }
    public static Node[] getClosestNode(HashMap<List<String>, Edge> edgeHashMap, Coordinate point){
        Double distance= Double.POSITIVE_INFINITY;
        Node[] res = new Node[2];
        for (List<String> key:edgeHashMap.keySet()
             ) {
            Edge testEdge = edgeHashMap.get(key);
            Node p1 = testEdge.getU();
            Node p2 = testEdge.getV();
            double d = BuildingInformationService.distanceEdge2Point(p2.getEast(),p2.getNorth(),
                                                                     p1.getEast(),p1.getNorth(),
                                                                     point.getX(),point.getY());
            if (d<distance){
                distance = d;
                double d1 = Math.hypot(point.getX()- p1.getEast(),point.getY()- p1.getNorth());
                double d2 = Math.hypot(point.getX()- p2.getEast(),point.getY()- p2.getNorth());

                res[0] = p1;
                res[1] = p2;

            }
        }
        return res;
    }

    public static List<Coordinate> shortestPath2VisualCoords(MyDataSingleton data,double x1,double y1,double x2,double y2){
        List<Coordinate> coords = shortestPath2Coordinate.converter(data);
        Node[] entryEdge = getClosestNode(data.getGraphFeatures().getEdgeHashMap(), new Coordinate(x1,y1));
        Node[] exitEdge =  getClosestNode(data.getGraphFeatures().getEdgeHashMap(), new Coordinate(x2,y2));
        Node last =  exitEdge[0];
        Node last1 = exitEdge[1];
        Node first = entryEdge[0];
        Node second = entryEdge[1];

        Coordinate entryPoint = shortestPath2Coordinate.calculateProjection(first.getEast(), first.getNorth(),
                second.getEast(), second.getNorth(),
                x1,y1);
        Coordinate exitPoint = shortestPath2Coordinate.calculateProjection(last.getEast(), last.getNorth(),
                last1.getEast(), last1.getNorth(),
                x2,y2);
        coords.add(0,new Coordinate(entryPoint.getX(),entryPoint.getY()));
        coords.add(new Coordinate(exitPoint.getX(),exitPoint.getY()));
        coords.add(0,new Coordinate(x1,y1));
        coords.add(new Coordinate(x2,y2));


        return coords;
    }
    public static Coordinate EN2LatLon (double x,double y){


        CRS epsg3044 = CRS.fromEpsgCode(3044);
        CRS wgs84 = CRS.fromEpsgCode(4326);

        var toWgs84 = Transform.apply(epsg3044, wgs84);


        Tuple2<Object, Object> EN2LatLon = toWgs84.apply(x,y);

        return new Coordinate((double)EN2LatLon._1(),(double)EN2LatLon._2());


    }
    public static Coordinate LatLon2EN (double lat,double lon){

        CRS epsg3044 = CRS.fromEpsgCode(3044);
        CRS wgs84 = CRS.fromEpsgCode(4326);
        var fromWgs84 = Transform.apply(wgs84, epsg3044);
        Tuple2<Object, Object> latlon2EN = fromWgs84.apply(lat  , lon);
        return new Coordinate((double) latlon2EN._1(),(double) latlon2EN._2());


    }
}
