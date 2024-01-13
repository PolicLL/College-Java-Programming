package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class DeserializationUtil {

    public static <T> T getFromConfigurationFile(String CONFIGURATION_FILE_PATH, Class<T> valueType, Consumer<T> initializer) {
        T device = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            device = objectMapper.readValue(new File(CONFIGURATION_FILE_PATH), valueType);

            if (initializer != null) {
                initializer.accept(device);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return device;
    }

    public static <T> T getFromConfigurationFile(String CONFIGURATION_FILE_PATH, Class<T> valueType) {
        T device = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            device = objectMapper.readValue(new File(CONFIGURATION_FILE_PATH), valueType);

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return device;
    }
}
