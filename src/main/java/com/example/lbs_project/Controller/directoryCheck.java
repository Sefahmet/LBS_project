package com.example.lbs_project.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller("/directory")
public class directoryCheck {

    private final ResourceLoader resourceLoader;

    @Autowired
    public directoryCheck(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping
    public ResponseEntity getDirectoryLocation() throws IOException {
        try {
            String resourcePath = "classpath:Data";
            Resource resource = resourceLoader.getResource(resourcePath);
            File dataFile = resource.getFile();
            Path directoryPath = Paths.get(dataFile.getParent());
            String directoryLocation = directoryPath.toAbsolutePath().toString();
            return new ResponseEntity(directoryLocation,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("There is a problem", HttpStatus.BAD_GATEWAY);
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> getFileNames() {
        try {
            String currentPath = System.getProperty("user.dir");
            File currentDirectory = new File(currentPath);
            File[] files = currentDirectory.listFiles();

            List<String> fileNames = new ArrayList<>();
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }

            return ResponseEntity.ok(fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
