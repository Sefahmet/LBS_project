package com.example.lbs_project.DataHolder;


import com.example.lbs_project.Entity.Node;
import lombok.Data;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class MyDataSingleton {
    private Data data;

    public MyDataSingleton() {
        try {
            // Read the Shapefile
            String shapefilePath = "/Users/sefahmet/Desktop/2. Semester/Project/Data/Roads.shp";
            File shapefile = new File(shapefilePath);
            FileDataStore dataStore = FileDataStoreFinder.getDataStore(shapefile);
            SimpleFeatureCollection features = dataStore.getFeatureSource().getFeatures();
            Graph<Node, DefaultWeightedEdge> newGraph = new Graph<Node, DefaultWeightedEdge>;
            // Extract road geometries
            List<Geometry> geometries = new ArrayList<>();
            SimpleFeatureIterator iterator = features.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                System.out.println(feature.getProperties());
                //Long fid = (Long) feature.getAttribute("fid");
                //String u_id = feature.getAttribute("u").toString();
                //String v_id = feature.getAttribute("v").toString();
                MultiLineString lineString = (MultiLineString) feature.getAttribute("the_geom");
                System.out.println(lineString.getNumPoints());
                geometries.add(geometry);
            }
            iterator.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}