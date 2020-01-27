package com.jannetta.mqtt.controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.LoggerFactory;

public class MQTTPublish {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(MQTTPublish.class);

    public void perform(String broker, String clientId, MemoryPersistence persistence, int qos, String topic, String content, String username, char[] password) {
        //System.out.println("MQTTPublish performed by thread: " + Thread.currentThread().getName());
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(username);
            connOpts.setPassword(password);
            connOpts.setCleanSession(true);
            logger.trace("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            logger.trace("Connected");
            logger.trace("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            logger.trace("Message published");
            sampleClient.disconnect();
            logger.trace("Disconnected");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }
}
