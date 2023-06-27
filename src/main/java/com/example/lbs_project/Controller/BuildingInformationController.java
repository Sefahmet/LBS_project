package com.example.lbs_project.Controller;

import com.example.lbs_project.DataHolder.RouteDataStorage;
import com.example.lbs_project.Service.BuildingInformationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/building")
public class BuildingInformationController {
    private  final BuildingInformationService infoService;
    @GetMapping()
    public ResponseEntity welcome(){return new ResponseEntity("welcome", HttpStatus.OK);}
    @GetMapping("/info")
    public ResponseEntity getInfo(@Valid @RequestParam Double x,@Valid @RequestParam Double y,@Valid @RequestParam UUID pathId ) throws IOException {

      System.out.println(pathId);
      List<Coordinate> coords = RouteDataStorage.getCoordinates(pathId);
      System.out.println(x);
      System.out.println(y);
      System.out.println(pathId);
      System.out.println(coords);
      try{
          return new ResponseEntity(infoService.getNearestBuildingInformation(x,y,coords),HttpStatus.OK);

      }catch (Exception e){
          System.out.println(e);
          e.printStackTrace();
          return new ResponseEntity("Something Wrong", HttpStatus.BAD_REQUEST);
      }
   }
}
