package com.example.lbs_project.Controller;

import com.example.lbs_project.Entity.PointEnt;
import com.example.lbs_project.Service.ShortestPathService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.awt.geom.Point2D;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortestPath")
public class ShortestPathController {
    @GetMapping
    public String welcome(){return "Welcome to Shortest Path Service";}

    @GetMapping(value = "/get")
    public ResponseEntity<List<Point2D.Double>> getShortestPathCoordinates(@Valid @RequestBody PointEnt shortestPathParameters) {


        double x1 = shortestPathParameters.getX1();
        double y1 = shortestPathParameters.getY1();
        double x2 = shortestPathParameters.getX2();
        double y2 = shortestPathParameters.getY2();

        return new ResponseEntity(ShortestPathService.getShortestPathCoordinates(x1, y1, x2, y2), HttpStatus.OK);
    }
    @GetMapping(value = "/gettest")
    public ResponseEntity<List<Coordinate>> getTest() {
        double x1 = 368565.50596542074;
        double y1 = 5616230.13241898;
        double x2 = 370248.0009995926;
        double y2 = 5617561.043232782;
        double w1 = 1.1;
        double w2 = 1.9;

        return new ResponseEntity(ShortestPathService.getShortestPathCoordinates(x1, y1, x2, y2), HttpStatus.OK);
    }
}
