package com.example.lbs_project.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class PointEnt {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull(message = "{validation.messages.startpoint.xCoordinate}")
    private double x1;

    @NotNull(message = "{validation.messages.startpoint.yCoordinate}")
    private double y1;
    @NotNull(message = "{validation.messages.endpoint.xCoordinate}")
    private double x2;
    @NotNull(message = "{validation.messages.endpoint.yCoordinate}")
    private double y2;}

