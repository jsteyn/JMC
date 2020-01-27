package com.jannetta.mqtt.controller;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.LocalDateTime;
import java.util.TimerTask;


public class TimerJob extends TimerTask {
    String topic = "sensor/falcon/temperature";
    String protocol = "tcp";
    String content = "";
    int qos = 2;
    String broker = "192.168.1.6";
    String port = "1883";
    String clientId = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();
    String username = "";
    char[] password = {};

    public TimerJob(String broker, String clientId, MemoryPersistence persistence, int qos, String topic, String content, String port, String username, char[] password) {
        super();
        this.topic = topic;
        this.content = content;
        this.qos = qos;
        this.broker = broker;
        this.clientId = clientId;
        this.persistence = persistence;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        LocalDateTime localTime = LocalDateTime.now();
        System.out.println("Run TimerJob at " + localTime.toString());

        MQTTPublish publish = new MQTTPublish();
        publish.perform(broker, clientId, persistence, qos, topic, content, username, password);

    }
}