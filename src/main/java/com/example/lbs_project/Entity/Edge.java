package com.example.lbs_project.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;

@NoArgsConstructor
public class Edge extends DefaultWeightedEdge {
        @Getter @Setter String fid;
        @Getter @Setter  private String u_id;
        @Getter @Setter  private String v_id;
        @Getter @Setter  private Node u;
        @Getter @Setter  private Node v;
        @Getter @Setter  private double distance;
        @Getter @Setter private List<Coordinate> edgeCoordinates;
        @Getter @Setter private String streetName;

    public Edge(String fid, String  u_id, String v_id,Node u,Node v){
        this.fid = fid;
        this.u_id = u_id;
        this.v_id = v_id;;
        this.u  = u ;
        this.v  = v;
    }
    public Edge(String fid, String u_id, String v_id,Node u,Node v,double distance,List<Coordinate> edgeCoordinates,String streetName) {
        this.fid = fid;
        this.u_id = u_id;
        this.v_id = v_id;;
        this.u  = u ;
        this.v  = v;
        this.distance = distance;
        this.edgeCoordinates = edgeCoordinates;
        this.streetName = streetName;
    }






/*

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setU(Node u) {

        this.u = u;
    }

    public Long getU_id() {
        return u_id;
    }

    public Node getU() {
        return u;
    }



    public Node getV() {
        return v;
    }

    public Long getV_id() {
        return v_id;
    }

    public void setV(Node v) {
        this.v = v;
    }

    public int getFid() {
        return fid;
    }
*/

}

