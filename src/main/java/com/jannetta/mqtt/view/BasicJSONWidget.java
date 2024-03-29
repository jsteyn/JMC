package com.jannetta.mqtt.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jannetta.mqtt.model.JSONPayload;
import com.jannetta.mqtt.model.Subscription;
import com.jannetta.mqtt.model.Payload;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class to create a widget that subscribes to a topic on a broker and displays the payload
 */
public class BasicJSONWidget extends MQTT_Widget implements ActionListener {
    Logger logger = LoggerFactory.getLogger(BasicJSONWidget.class);
    private JLabel lbl_label = new JLabel();
    private JLabel lbl_message = new JLabel("wait ...");
    private String unit;
    private BufferedImage image;
    private boolean retained;
    private boolean timestamp;
    MqttClient client;

    /**
     * A basic widget that displays an icon, location information payload and time of the latest
     * update
     * @param subscription The widget configuration read from the JSON configuration file
     */
    public BasicJSONWidget(Subscription subscription) {
        super();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(subscription.getWidth(), subscription.getHeight()));
        setMaximumSize(new Dimension(subscription.getWidth(), subscription.getHeight()));
        setBorder(new BevelBorder(BevelBorder.RAISED));
        lbl_label.setText(subscription.getLabel());
        timestamp = subscription.isTimestamp();
        unit = subscription.getUnit();
        Color bg;
        try {
            bg = makeColor(subscription.getColour());
        } catch (Exception e) {
            logger.error("Background colour not defined.");
            bg = null;
        }
        Color fg;
        try {
            //Field field = Color.class.getField(subscription.getText());
            fg = makeColor(subscription.getText());
        } catch (Exception e) {
            fg = null;
            logger.error("Foreground colour not defined.");
        }
        setBackground(bg);
        lbl_label.setForeground(fg);
        lbl_message.setForeground(fg);
        lbl_message.setToolTipText("payload");
        add(lbl_label, BorderLayout.NORTH);
        String imageFilename = subscription.getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image mqtt = null;
        try {
            mqtt = ImageIO.read(new URL(imageFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JLabel label = new JLabel(new ImageIcon(mqtt));
            add(label, BorderLayout.CENTER);
        } catch (Exception e) {
            logger.error("Can't read input file: " + imageFilename);
            image = null;
        }
        add(lbl_message, BorderLayout.SOUTH);
        subscribe(subscription.getTopic(), subscription.getProtocol() +
                "://" + subscription.getAddress() + ":" + subscription.getPort(),
                subscription.getUsername(), subscription.getPassword());
    }

    public void setLbl_label(String label) {
        lbl_label.setText(label);
    }

    public void setLbl_message(String message) {
        lbl_message.setText(message);
    }

    /**
     * Update the message label with the latest date and payload
     * @param topic
     * @param mqttMessage
     */
    public void update(String topic, MqttMessage mqttMessage) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String msg = new String(mqttMessage.getPayload());
        JSONPayload jsonPayload = gson.fromJson(msg,JSONPayload.class);
        Payload payload = jsonPayload.getPayload();
        String time = dtf.format(now);
        System.out.println(payload.getValue() + " " + payload.getUnits() + " " + (isRetained() ? "Retained" : timestamp ? "     " +time : ""));
        lbl_message.setText(payload.getValue() + " " + payload.getUnits() + " " + (isRetained() ? "Retained" : timestamp ? "     " +time : ""));
        lbl_message.setToolTipText(gson.toJson(jsonPayload).replace("\n","<br>"));
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }

    /**
     * Helper function to turn a comma delimited string containing rgb values (eg 255,200,100) into
     * an instance of Color
     * @param rgb
     * @return
     */
    private Color makeColor(String rgb) {
        int r = Integer.valueOf(rgb.split(",")[0]);
        int g = Integer.valueOf(rgb.split(",")[1]);
        int b = Integer.valueOf(rgb.split(",")[2]);
        return new Color(r,g,b);
    }

    /**
     * Override the getWidget method - to return the widget class
     * @return
     */
    @Override
    String getWidget() {
        return "com.jannetta.mqtt.view.BasicWidget";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
