package com.jannetta.mqtt.view;

import com.jannetta.mqtt.controller.HS100;
import com.jannetta.mqtt.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HS100PlugWidget extends Widget implements ActionListener {
    Logger logger = LoggerFactory.getLogger(HS100PlugWidget.class);
    private JLabel lbl_label = new JLabel("-");
    private JLabel lbl_message = new JLabel("wait ...");
    private String unit;
    private BufferedImage image;
    private boolean timestamp;
    static String IP;
    HS100 plug;
    ToggleButton toggleButton = new ToggleButton();
    Color bg = new Color(255, 0, 0);
    Color fg = new Color(255, 255, 255);

    public HS100PlugWidget(Subscription subscription) {
        super();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(subscription.getWidth(), subscription.getHeight()));
        setMaximumSize(new Dimension(subscription.getWidth(), subscription.getHeight()));
        setBorder(new BevelBorder(BevelBorder.RAISED));
        timestamp = subscription.isTimestamp();
        unit = subscription.getUnit();
        IP = subscription.getAddress();
        plug = new HS100(IP);
        try {
            toggleButton.isOn(plug.isOn());
        } catch (IOException e) {
            e.printStackTrace();
        }
        toggleButton.addActionListener(this);
        toggleButton.setBorder(BorderFactory.createLineBorder(Color.black));
        lbl_message.setBorder(BorderFactory.createLineBorder(Color.black));
        lbl_label.setForeground(fg);
        lbl_label.setText(subscription.getLabel());
        lbl_message.setText(subscription.getLabel());
        add(lbl_label, BorderLayout.PAGE_START);
        add(toggleButton, BorderLayout.CENTER);
        add(lbl_message, BorderLayout.SOUTH);
        isPresent();

    }

    public void notPresent() {
    }

    public boolean isPresent() {
        if (plug.isPresent()) {
            setBackground(Color.white);
            return true;
        } else {
            setBackground(Color.orange);
        }

        return false;
    }

    public void plugOn() {
        try {
            plug.switchOn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plugOff() {
        try {
            plug.switchOff();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plugToggle() {
        if (plug.isPresent()) {
            try {
                if (plug.isOn()) {
                    plugOff();
                } else {
                    plugOn();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            notPresent();
            ;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action: " + e.getActionCommand());
        if (e.getActionCommand().equals("Refresh")) {
            isPresent();
        } else {
            plugToggle();
        }
    }
}
