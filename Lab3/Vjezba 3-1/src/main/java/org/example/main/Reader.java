package org.example.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Reader {

    private String brokerAddress;
    private String clientId;
    private String topicName;

    public void start(){

        try {
            MqttClient client = new MqttClient(brokerAddress, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            client.connect(connectOptions);
            client.subscribe(topicName);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    // Handle connection lost
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    // Handle incoming messages
                    String message = new String(mqttMessage.getPayload());
                    System.out.println("Received message: " + message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    // Handle message delivery completion
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
