package main;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MqttException {


        List<Sensor> sensorList = new ArrayList<>(Arrays.asList(
                new Sensor(-3266.8,3266.8,10,"C","Water temperature sensor."),
                new Sensor(0,65.36,1000,"Bar","Water pressure sensor."),
                new Sensor(0,65336,0,"L","Water consumption sensor."),
                new Sensor(0,6533.6,10,"M3","Water consumption sensor.")
        ));

        String brokerAddress = "tcp://localhost:1883";
        String topicName = "topic";

        Device device = new Device(topicName, brokerAddress);

        device.addListOfSensors(sensorList);

        device.runDevice();

    }
}