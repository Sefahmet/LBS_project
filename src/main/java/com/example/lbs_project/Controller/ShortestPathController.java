package com.example.lbs_project.Controller;

import com.example.lbs_project.DataHolder.MyDataSingleton;
import com.example.lbs_project.DataHolder.RouteDataStorage;
import com.example.lbs_project.Entity.Path;
import com.example.lbs_project.Service.ShortestPathService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/shortestPath")
public class ShortestPathController {

    private final ShortestPathService shortestPathService;
    private final ResourceLoader resourceLoader;

    @GetMapping
    public String welcome() {
        return "Welcome to Shortest Path Service";
    }

    @GetMapping(value = "/shortest-path-params")
    public ResponseEntity<Path> getShortestPathWLatLon(@Valid @RequestParam double lat1,
                                                       @Valid @RequestParam double lon1,
                                                       @Valid @RequestParam double lat2,
                                                       @Valid @RequestParam double lon2) throws IOException {

        String directoryLocation = "";
        try {
            String resourcePath = "classpath:Data";
            Resource resource = resourceLoader.getResource(resourcePath);
            File dataFile = resource.getFile();
            java.nio.file.Path directoryPath = Paths.get(dataFile.getParent());
            directoryLocation = directoryPath.toAbsolutePath().toString();

            Coordinate p1 = shortestPathService.LatLon2EN(lat1, lon1);
            Coordinate p2 = shortestPathService.LatLon2EN(lat2, lon2);

            double x1 = p1.getX();
            double y1 = p1.getY();
            double x2 = p2.getX();
            double y2 = p2.getY();
            MyDataSingleton data = shortestPathService.getShortestPathCoordinates(x1, y1, x2, y2, directoryLocation);

            List<Coordinate> coords = ShortestPathService.shortestPath2VisualCoords(data, x1, y1, x2, y2);
            List<Coordinate> rescoords = new ArrayList<>();
            for (Coordinate coord : coords) {
                rescoords.add(shortestPathService.EN2LatLon(coord.getX(), coord.getY()));
            }
            UUID pathId = UUID.randomUUID();
            RouteDataStorage.getInstance().put(pathId, coords);
            Path responsePath = new Path(pathId, rescoords);

            return new ResponseEntity<>(responsePath, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(directoryLocation + e, HttpStatus.BAD_REQUEST);
        }
    }
}