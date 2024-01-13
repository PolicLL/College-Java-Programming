public class Main {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientId = "MqttSubscriber";
        String topic = "topic";
        Reader tempReader = new Reader(broker, clientId, topic);
        tempReader.start();
    }
}