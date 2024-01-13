package org.example.main;

import org.eclipse.paho.client.mqttv3.MqttException;
import utils.DeserializationUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws MqttException, IOException {

        String CONFIGURATION_FILE_PATH = "src/main/resources/configuration.json";

        // Deserialize the JSON configuration file into a Device object
        Device device = DeserializationUtil.getFromConfigurationFile(CONFIGURATION_FILE_PATH, Device.class, Device::initialize);

        // Run the Device
        device.runDevice();

    }
}