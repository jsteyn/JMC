package com.jannetta.mqtt.view;

import com.jannetta.mqtt.controller.SimpleMqttCallBack;
import com.jannetta.mqtt.model.MQTTSubscription;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class to create a widget that subscribes to a topic on a broker and displays the payload
 */
public class Widget extends JPanel {
    private JLabel lbl_label = new JLabel();
    private JLabel lbl_message = new JLabel("wait ...");
    private String unit;
    private BufferedImage image;
    private boolean retained;
    private boolean timestamp;

    public Widget(MQTTSubscription mqttSubscription) {
        super();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(mqttSubscription.getWidth(), mqttSubscription.getHeight()));
        setBorder(new BevelBorder(BevelBorder.RAISED));
        lbl_label.setText(mqttSubscription.getLabel());
        timestamp = mqttSubscription.isTimestap();
        unit = mqttSubscription.getUnit();
        Color bg;
        try {
            Field field = Color.class.getField(mqttSubscription.getColour());
            bg = (Color) field.get(null);
        } catch (Exception e) {
            bg = null;
        }
        Color fg;
        try {
            Field field = Color.class.getField(mqttSubscription.getText());
            fg = (Color) field.get(null);
        } catch (Exception e) {
            fg = null;
        }
        setBackground(bg);
        lbl_label.setForeground(fg);
        lbl_message.setForeground(fg);
        add(lbl_label, BorderLayout.NORTH);
        String imageFilename = mqttSubscription.getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image mqtt = toolkit.getImage(ClassLoader.getSystemResource(imageFilename));
        System.out.println("FILE IMAGE: " + imageFilename);
        try {
            JLabel label = new JLabel(new ImageIcon(mqtt));
            add(label, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println("Can't read input file: " + imageFilename);
            image = null;
        }
        add(lbl_message, BorderLayout.SOUTH);
        subscribe(mqttSubscription.getTopic(), mqttSubscription.getProtocol() + "://" + mqttSubscription.getAddress() + ":" + mqttSubscription.getPort());
    }

    public void setLbl_label(String label) {
        lbl_label.setText(label);
    }

    public void setLbl_message(String message) {
        lbl_message.setText(message);
    }

    private void subscribe(String topic, String broker) {
        System.out.println("subscribe to " + broker + " for " + topic);


        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.setCallback(new SimpleMqttCallBack(this));
            client.connect();
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void update(String topic, MqttMessage mqttMessage) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        String msg = new String(mqttMessage.getPayload()) + unit + " " + (retained ? "Retained" : timestamp ? "     " +time : "");
        lbl_message.setText(msg);
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }
}
