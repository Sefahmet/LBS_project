package com.example.lbs_project.Format;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;

import java.io.File;
import java.io.IOException;

public class ReadSHPFiles {

    public static SimpleFeatureCollection shapeFileReader(String filename) throws IOException {

        File file = new File(filename);
        FileDataStore fileDataStore = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureCollection  features = fileDataStore.getFeatureSource().getFeatures();
        return features;
    }


}
