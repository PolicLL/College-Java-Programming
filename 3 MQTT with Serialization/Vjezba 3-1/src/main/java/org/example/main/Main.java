package org.example.main;

import org.example.utils.DeserializationUtil;

public class Main {
    public static void main(String[] args) {

        String CONFIGURATION_FILE_PATH = "src/main/resources/configuration.json";

        // Deserialize the JSON configuration file into a Device object
        Reader tempReader = DeserializationUtil.getFromConfigurationFile(CONFIGURATION_FILE_PATH, Reader.class);

        tempReader.start();
    }
}