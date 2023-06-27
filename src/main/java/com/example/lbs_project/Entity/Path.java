package com.example.lbs_project.Entity;

import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;
import java.util.UUID;

public class Path {
    @Getter @Setter UUID pathId ;
    @Getter @Setter List<Coordinate> coordinates;

    public Path(UUID pathId, List<Coordinate> coordinates){
        this.pathId = pathId;
        this.coordinates = coordinates;

    }
}
