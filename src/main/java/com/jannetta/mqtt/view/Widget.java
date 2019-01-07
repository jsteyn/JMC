package com.jannetta.mqtt.view;

import com.jannetta.mqtt.controller.SimpleMqttCallBack;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Abstract class to be used for creating new Widgets that will be displayed on the dashboard
 */
public abstract class Widget extends JPanel {
    Logger logger = LoggerFactory.getLogger(Widget.class);
    private boolean retained;

    public abstract void update(String topic, MqttMessage mqttMessage);

    /**
     * Get the name of the widget
     * @return widget name as a string
     */
    abstract String getWidget();
    /**
     * Subscribe to a topic on the specified broker
     * @param topic The topic to subscribe to as a String
     * @param broker The ip or domain name of the broker - format protocol://address:port
     */
    public void subscribe(String topic, String broker) {
        MqttClient client;
        logger.info("subscribe to " + broker + " for " + topic);
        try {
            client = new MqttClient(broker, MqttClient.generateClientId());
            client.setCallback(new SimpleMqttCallBack(this, topic, broker));
            client.connect();
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }


}
