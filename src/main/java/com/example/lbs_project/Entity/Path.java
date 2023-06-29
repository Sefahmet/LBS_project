package com.example.lbs_project.Entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

public class Path {
    @Getter @Setter UUID pathId ;
    @Getter @Setter List<?> coordinates;

    public Path(UUID pathId, List<?> coordinates){
        this.pathId = pathId;
        this.coordinates = coordinates;

    }
}
