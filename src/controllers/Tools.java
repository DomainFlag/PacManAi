package controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tools {

    private static final String TAG = "Tools";

    public Tools() {
        generateMaps();
    }

    public static List<File> generateMaps() {
        List<File> maps = new ArrayList<>();

        File directory = new File("res/maps/");

        if(directory.isDirectory()) {
            File[] files = directory.listFiles();

            if(files != null)
                Collections.addAll(maps, files);
        }

        return maps;
    }

    public static List<String> generateMapsDecoded(List<File> files) {
        List<String> filesDecoded = new ArrayList<>();

        for(File file : files)
            filesDecoded.add(file.getName());

        return filesDecoded;
    }

    public static List<String> generateTextures() {
        List<String> textures = new ArrayList<>();

        textures.add("Arcade");
        textures.add("Classic");
        textures.add("Minimal");

        return textures;
    }
}
