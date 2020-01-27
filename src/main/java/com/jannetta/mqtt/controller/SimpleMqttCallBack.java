package com.jannetta.mqtt.controller;

import com.jannetta.mqtt.view.MQTT_Widget;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;

public class SimpleMqttCallBack implements MqttCallback {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(SimpleMqttCallBack.class);
    private MQTT_Widget MQTTWidget;
    private String topic;
    private String broker;
    private String username;
    private String password;

    public SimpleMqttCallBack(MQTT_Widget basicMQTTWidget, String topic, String broker, String username, String password) {
        this.MQTTWidget = basicMQTTWidget;
        this.topic = topic;
        this.broker = broker;
        this.username = username;
        this.password = password;
    }
    public void connectionLost(Throwable throwable) {
        logger.error("Connection to MQTT broker lost!");
        MQTTWidget.subscribe(topic, broker, username, password);
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        logger.info("message received for topic: " + topic + ", " + mqttMessage.toString());
        MQTTWidget.setRetained(mqttMessage.isRetained());
        MQTTWidget.update(topic, mqttMessage);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}