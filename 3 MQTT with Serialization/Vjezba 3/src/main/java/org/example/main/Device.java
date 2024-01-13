package org.example.main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Setter
@Getter
public class Device {

    @JsonIgnore
    private MqttClient client;
    private List<Sensor> sensors;
    private String topicName;
    private String brokerAddress;

    private Device(String topicName, String brokerAddress) throws MqttException {
        this.topicName = topicName;
        this.brokerAddress = brokerAddress;
        this.sensors = new ArrayList<>();
        this.client = new MqttClient(brokerAddress, topicName);
    }

    public void initialize()  {
        try {
            this.client = new MqttClient(brokerAddress, topicName);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void addListOfSensors(List<Sensor> sensorList){
        sensors.addAll(sensorList);
    }

    public void runDevice() throws MqttException {
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        client.connect(connectOptions);

        int i = 0;
        while (i < 5) {
            for (Sensor sensor : sensors) {
                String message = sensor.getSensorData();
                MqttMessage messageBytes = new MqttMessage(message.getBytes());
                client.publish(topicName, messageBytes);
                printMessage(message);
            }
            i++;

            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        client.disconnect();
    }

    private void printMessage(String message){
        System.out.println("Published message : " + message);
    }

}