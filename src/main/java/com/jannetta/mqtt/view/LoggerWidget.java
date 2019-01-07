package com.jannetta.mqtt.view;

import com.jannetta.mqtt.model.MQTTSubscription;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Class to create a widget that subscribes to a topic on a broker and displays the payload
 */
public class LoggerWidget extends Widget {
    Logger logger = LoggerFactory.getLogger(LoggerWidget.class);
    private JLabel lbl_label = new JLabel();
    private JLabel lbl_message = new JLabel("wait ...");
    private String unit;
    private BufferedImage image;
    private String time = "";
    private boolean timestamp;
    private String filename;
    private TextArea textArea = new TextArea(5, 20);

    /**
     * A basic widget that displays an icon, location information payload and time of the latest
     * update
     *
     * @param mqttSubscription The widget configuration read from the JSON configuration file
     */
    public LoggerWidget(MQTTSubscription mqttSubscription) {
        super();
        filename = mqttSubscription.getFilename();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(mqttSubscription.getWidth(), mqttSubscription.getHeight()));
        setMaximumSize(new Dimension(mqttSubscription.getWidth(), mqttSubscription.getHeight()));
        setBorder(new BevelBorder(BevelBorder.RAISED));
        lbl_label.setText(mqttSubscription.getLabel());
        timestamp = mqttSubscription.isTimestamp();
        unit = mqttSubscription.getUnit();
        Color bg;
        try {
            bg = makeColor(mqttSubscription.getColour());
        } catch (Exception e) {
            logger.error("Background colour not defined.");
            bg = null;
        }
        Color fg;
        try {
            //Field field = Color.class.getField(mqttSubscription.getText());
            fg = makeColor(mqttSubscription.getText());
        } catch (Exception e) {
            fg = null;
            logger.error("Foreground colour not defined.");
        }
        setBackground(bg);
        lbl_label.setForeground(fg);
        lbl_message.setForeground(fg);
        add(lbl_label, BorderLayout.NORTH);
        String imageFilename = mqttSubscription.getImage();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image mqtt = toolkit.getImage(ClassLoader.getSystemResource(imageFilename));
        try {
            JLabel label = new JLabel(new ImageIcon(mqtt));
            add(label, BorderLayout.CENTER);
        } catch (Exception e) {
            logger.error("Can't read input file: " + imageFilename);
            image = null;
        }
        add(textArea, BorderLayout.CENTER);
        add(lbl_message, BorderLayout.SOUTH);
        subscribe(mqttSubscription.getTopic(), mqttSubscription.getProtocol() + "://" + mqttSubscription.getAddress() + ":" + mqttSubscription.getPort());
    }

    public void setLbl_label(String label) {
        lbl_label.setText(label);
    }

    public void setLbl_message(String message) {
        lbl_message.setText(message);
    }

    /**
     * Update the message label with the latest date and payload
     *
     * @param topic
     * @param mqttMessage
     */
    @Override
    public void update(String topic, MqttMessage mqttMessage) {
        if (timestamp) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            time = dtf.format(now);
        }
        String msg = time + "," + new String(mqttMessage.getPayload());
        logger.debug("Updating 1");
        lbl_message.setText(time);
        logger.debug("Updating 2");
        try {

            logger.debug("Updating 3");
            textArea.append(msg + "\n");
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println(msg);
            pw.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Helper function to turn a comma delimited string containing rgb values (eg 255,200,100) into
     * an instance of Color
     *
     * @param rgb
     * @return
     */
    private Color makeColor(String rgb) {
        int r = Integer.valueOf(rgb.split(",")[0]);
        int g = Integer.valueOf(rgb.split(",")[1]);
        int b = Integer.valueOf(rgb.split(",")[2]);
        return new Color(r, g, b);
    }

    /**
     * Override the getWidget method - to return the widget class
     *
     * @return
     */
    @Override
    String getWidget() {
        return "com.jannetta.mqtt.view.BasicWidget";
    }

}
