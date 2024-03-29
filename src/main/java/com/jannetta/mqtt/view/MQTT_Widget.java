package com.jannetta.mqtt.view;

import com.jannetta.mqtt.controller.SimpleMqttCallBack;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class to be used for creating new Widgets that will be displayed on the dashboard
 */
public abstract class MQTT_Widget extends Widget {
    Logger logger = LoggerFactory.getLogger(MQTT_Widget.class);
    private boolean retained;
    private String jsonconfig = null;

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
    public void subscribe(String topic, String broker, String username, String password) {
        MqttClient client;
        logger.info("subscribe to " + broker + " for " + topic);
        try {
            client = new MqttClient(broker, MqttClient.generateClientId());
            System.out.println("ClientID: " + client.getClientId());
            client.setCallback(new SimpleMqttCallBack(this, topic, broker, username, password));
            MqttConnectOptions options = new MqttConnectOptions();
            options.setPassword(password.toCharArray());
            options.setUserName(username);
            System.out.println("Connection: " + new String(options.getPassword()) + " " + options.getUserName());
            client.connect(options);
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

    public String getJsonconfig() {
        return jsonconfig;
    }

    public void setJsonconfig(String jsonconfig) {
        this.jsonconfig = jsonconfig;
    }
}
