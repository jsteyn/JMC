package controller;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.LocalDateTime;
import java.util.TimerTask;


public class TimerJob extends TimerTask {
    String topic = "sensor/falcon/temperature";
    String content = "";
    int qos = 2;
    String broker = "tcp://192.168.1.6:1883";
    String clientId = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();

    public TimerJob(String broker, String clientId, MemoryPersistence persistence, int qos, String topic, String content) {
        super();
        this.topic = topic;
        this.content = content;
        this.qos = qos;
        this.broker = broker;
        this.clientId = clientId;
        this.persistence = persistence;
    }

    @Override
    public void run() {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run TimerJob at " + localTime.toString());

        MQTTPublish publish = new MQTTPublish();
        publish.perform(broker, clientId, persistence, qos, topic, content);

    }
}