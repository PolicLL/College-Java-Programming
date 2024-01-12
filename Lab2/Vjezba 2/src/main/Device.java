package main;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class Device {
    private MqttClient client;
    private List<Sensor> sensors;
    private String topic, address;
    public Device(String topic, String address) throws MqttException {
        this.topic = topic;
        this.address = address;
        this.sensors = new ArrayList<>();
        this.client = new MqttClient(address, topic);
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
                client.publish(topic, messageBytes);
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

}