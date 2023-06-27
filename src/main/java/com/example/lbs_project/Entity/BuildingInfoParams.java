package com.example.lbs_project.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BuildingInfoParams {
    @Id
    private UUID PathId;

    @NotNull(message = "{validation.messages.startpoint.xCoordinate}")
    private double x;

    @NotNull(message = "{validation.messages.startpoint.yCoordinate}")
    private double y;

}
