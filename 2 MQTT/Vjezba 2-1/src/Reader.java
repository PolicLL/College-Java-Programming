import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Reader {

    private String broker, clientID, topic;

    public Reader(String broker, String clientID, String topic) {
        this.broker = broker;
        this.clientID = clientID;
        this.topic = topic;
    }

    public void start(){

        try {
            MqttClient client = new MqttClient(broker, clientID, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            client.connect(connectOptions);
            client.subscribe(topic);

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
