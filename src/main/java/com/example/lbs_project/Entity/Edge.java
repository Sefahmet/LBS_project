package com.example.lbs_project.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jgrapht.graph.DefaultWeightedEdge;
@NoArgsConstructor
public class Edge extends DefaultWeightedEdge {
        @Getter @Setter int fid;
        @Getter @Setter  private Long u_id;
        @Getter @Setter  private Long v_id;
        @Getter @Setter  private Node u;
        @Getter @Setter  private Node v;
        @Getter @Setter  private double distance;


    public Edge(int fid, Long u_id, Long v_id) {
        this.fid = fid;
        this.u_id = u_id;
        this.v_id = v_id;;
    }
    public Edge(int fid, Long u_id, Long v_id, double weight) {
        this.fid = fid;
        this.u_id = u_id;
        this.v_id = v_id;;
    }
    public Edge(int fid, Long u_id, Long v_id, double distance, double slope) {
        this.fid = fid;
        this.u_id = u_id;
        this.v_id = v_id;;
        this.distance = distance;
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

