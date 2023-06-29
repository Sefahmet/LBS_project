package com.example.lbs_project.Controller;

import com.example.lbs_project.DataHolder.MyDataSingleton;
import com.example.lbs_project.DataHolder.RouteDataStorage;
import com.example.lbs_project.Entity.Node;
import com.example.lbs_project.Entity.Path;
import com.example.lbs_project.Entity.PointEnt;
import com.example.lbs_project.Format.shortestPath2Coordinate;
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
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortestPath")

public class ShortestPathController {

    private final ShortestPathService shortestPathService;


    @GetMapping
    public String welcome(){return "Welcome to Shortest Path Service";}

    @GetMapping(value = "/get")
    public ResponseEntity<Path> getShortestPathCoordinates(@Valid @RequestBody PointEnt shortestPathParameters) throws IOException{


        double x1 = shortestPathParameters.getX1();
        double y1 = shortestPathParameters.getY1();
        double x2 = shortestPathParameters.getX2();
        double y2 = shortestPathParameters.getY2();
        MyDataSingleton data = shortestPathService.getShortestPathCoordinates(x1, y1, x2, y2);


        List<Coordinate> coords = shortestPath2Coordinate.converter(data);

        Node[] entryEdge =  shortestPathService.getClosestNode(data.getGraphFeatures().getEdgeHashMap(), new Coordinate(x1,y1));
        Node[] exitEdge =  shortestPathService.getClosestNode(data.getGraphFeatures().getEdgeHashMap(), new Coordinate(x2,y2));
        Node last =  exitEdge[0];
        Node last1 = exitEdge[1];
        Node first = entryEdge[0];
        Node second = entryEdge[1];

        Coordinate entryPoint = shortestPath2Coordinate.calculateProjection(first.getEast(), first.getNorth(),
                second.getEast(), second.getNorth(),
                x1,y1);
        Coordinate exitPoint = shortestPath2Coordinate.calculateProjection(last.getEast(), last.getNorth(),
                last1.getEast(), last1.getNorth(),
                x2,y2);
        coords.add(0,new Coordinate(entryPoint.getX(),entryPoint.getY()));
        coords.add(new Coordinate(exitPoint.getX(),exitPoint.getY()));
        coords.add(0,new Coordinate(x1,y1));
        coords.add(new Coordinate(x2,y2));


        UUID pathId = UUID.randomUUID();
        RouteDataStorage.getInstance().put(pathId,coords);
        Path responsePath = new Path(pathId,coords);
        return new ResponseEntity(responsePath, HttpStatus.OK);
    }
    @GetMapping(value = "/gettest")
    public ResponseEntity<List<Coordinate>> getTest() throws IOException {
        double x1 = 365288.50596542074;
        double y1 = 5621068.13241898;
        double x2 = 364640.0;
        double y2 = 5621281.0;

        MyDataSingleton data = ShortestPathService.getShortestPathCoordinates(x1, y1, x2, y2);

        List<Coordinate> coords = shortestPath2Coordinate.converter(data);
        UUID pathId = UUID.randomUUID();
        RouteDataStorage.getInstance().put(pathId,coords);
        Path responsePath = new Path(pathId,coords);
        return new ResponseEntity(responsePath, HttpStatus.OK);
    }
    @GetMapping(value = "/gettestfid")
    public ResponseEntity<List<String>> getTestFid() throws IOException {
        double x1 = 365288.50596542074;
        double y1 = 5621068.13241898;
        double x2 = 364640.0;
        double y2 = 5621281.0;

        MyDataSingleton data = ShortestPathService.getShortestPathCoordinates(x1, y1, x2, y2);
        List<String> fid = shortestPathService.getEdgeIds(data);
        List<Coordinate> coords = shortestPath2Coordinate.converter(data);
        UUID pathId = UUID.randomUUID();
        RouteDataStorage.getInstance().put(pathId,coords);
        Path responsePath = new Path(pathId,fid);
        return new ResponseEntity(responsePath, HttpStatus.OK);
    }
    @GetMapping(value = "/getFid")
    public ResponseEntity<Path> getShortestPathFids(@Valid @RequestBody PointEnt shortestPathParameters) throws IOException{


        double x1 = shortestPathParameters.getX1();
        double y1 = shortestPathParameters.getY1();
        double x2 = shortestPathParameters.getX2();
        double y2 = shortestPathParameters.getY2();
        MyDataSingleton data = shortestPathService.getShortestPathCoordinates(x1, y1, x2, y2);
        List<String> fids = shortestPathService.getEdgeIds(data);
        List<Coordinate> coords = shortestPath2Coordinate.converter(data);
        UUID pathId = UUID.randomUUID();
        RouteDataStorage.getInstance().put(pathId,coords);
        Path responsePath = new Path(pathId,fids);
        return new ResponseEntity(responsePath, HttpStatus.OK);
    }
}
