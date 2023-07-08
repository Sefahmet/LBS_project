package com.example.lbs_project.Controller;

import com.example.lbs_project.DataHolder.RouteDataStorage;
import com.example.lbs_project.Service.BuildingInformationService;
import com.example.lbs_project.Service.ShortestPathService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/building")
public class BuildingInformationController {
    private  final BuildingInformationService infoService;

    @GetMapping()
    public ResponseEntity welcome() throws IOException {
        String res = "directoryCheck.getDirectoryLocation()";
        return new ResponseEntity(res+" welcome", HttpStatus.OK);}

    @GetMapping("/info")
    public ResponseEntity getInfo(@Valid @RequestParam Double x,@Valid @RequestParam Double y,@Valid @RequestParam UUID pathId ) throws IOException {

      List<Coordinate> coords = RouteDataStorage.getCoordinates(pathId);

      try{
          return new ResponseEntity(infoService.getNearestBuildingInformation(x,y,coords),HttpStatus.OK);

      }catch (Exception e){
          e.printStackTrace();
          return new ResponseEntity("Something Wrong", HttpStatus.BAD_REQUEST);
      }
   }
    @GetMapping("/infoLatLon")
    public ResponseEntity getInfoWithLatLon(@Valid @RequestParam Double lat,@Valid @RequestParam Double lon,@Valid @RequestParam UUID pathId ) throws IOException {
        Coordinate p = ShortestPathService.LatLon2EN(lat,lon);

        double x = p.getX();
        double y = p.getY();

        List<Coordinate> coords = RouteDataStorage.getCoordinates(pathId);


        try{
            return new ResponseEntity(infoService.getNearestBuildingInformation(x,y,coords),HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity("Something Wrong", HttpStatus.BAD_REQUEST);
        }
    }
}
