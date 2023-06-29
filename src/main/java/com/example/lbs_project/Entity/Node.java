package com.example.lbs_project.Entity;

import lombok.Getter;
import lombok.Setter;

public class Node {
        @Getter @Setter private String osmid;
        @Getter @Setter private Double east;
        @Getter @Setter private Double north;
        @Getter @Setter private Double lat;
        @Getter @Setter private Double lon;
        public Node(){

        }
        public Node(String osmid, Double east, Double north,Double lat, Double lon) {
            this.osmid = osmid;
            this.east = east;
            this.north = north;
            this.lat = lat;
            this.lon = lon;
        }

}
