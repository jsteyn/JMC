package com.jannetta.mqtt.controller;

import com.jannetta.mqtt.view.Widget;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.LoggerFactory;

public class SimpleMqttCallBack implements MqttCallback {
    org.slf4j.Logger logger = LoggerFactory.getLogger(SimpleMqttCallBack.class);
    private Widget widget;
    private String topic;
    private String broker;

    public SimpleMqttCallBack(Widget basicWidget, String topic, String broker) {
        this.widget = basicWidget;
        this.topic = topic;
        this.broker = broker;
    }
    public void connectionLost(Throwable throwable) {
        logger.error("Connection to MQTT broker lost!");
        widget.subscribe(topic, broker);
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        //logger.info("message received");
        widget.setRetained(mqttMessage.isRetained());
        widget.update(topic, mqttMessage);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}