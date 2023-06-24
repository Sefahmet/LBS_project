package com.example.lbs_project.Entity;

public class Node {
        private Double id;
        private Long osmid;
        private double east;
        private double north;
        private Integer streetCount;

        public Node(Double id, Long osmid, double east, double north) {
            this.id = id;
            this.osmid = osmid;
            this.east = east;
            this.north = north;
        }

        public Long getOsmid() {
            return osmid;
        }

        public double geteast() {
            return east;
        }

        public double getnorth() {
            return north;
        }

        @Override
        public String toString() {
            return osmid.toString();
        }

}
