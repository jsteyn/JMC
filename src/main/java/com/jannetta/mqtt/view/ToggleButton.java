package com.jannetta.mqtt.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * https://github.com/kdeloach/labs/blob/master/java/yahtzee/src/Dice.java
 * https://kodejava.org/how-do-i-draw-a-round-rectangle-in-java-2d/
 */
public class ToggleButton extends JComponent implements MouseListener {
    private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
    private boolean isOn = false;
    private Dimension size = new Dimension(100,100);

    public ToggleButton() {
        super();
        setLayout(new BorderLayout());
        enableInputMethods(true);
        addMouseListener(this);
        System.out.println(getPreferredSize().getWidth() + " x " + getPreferredSize().getHeight());
        System.out.println(getMinimumSize().getWidth() + " x " + getMinimumSize().getHeight());
        System.out.println(getMaximumSize().getWidth() + " x " + getMaximumSize().getHeight());
        /*
        Every possible way of setting the size - nothing works
         */
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size.width, size.height);
        /*

         */
        System.out.println(getWidth() + " x " + getHeight());
        setFocusable(true);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2.setStroke(new BasicStroke(2.0f));
        if (!isOn()) {
            g2.setColor(new Color(191,201,202));
            g.fillRoundRect(10, 10, 60, 30, 30, 30);
            g2.setColor(new Color(128,139,150));
            g.drawRoundRect(10, 10, 60, 30, 30, 30);
            g2.setColor(new Color(213, 216, 220));
            g.fillOval(13, 13, 24, 24);
            g2.setColor(new Color(213, 216, 220));
            g.drawOval(13, 13, 24, 24);
        } else {
            g2.setColor(new Color(213,245,227));
            g.fillRoundRect(10, 10, 60, 30, 30, 30);
            g2.setColor(new Color(24,106,59));
            g.drawRoundRect(10, 10, 60, 30, 30, 30);
            g2.setColor(new Color(237, 187, 153));
            g.fillOval(42, 13, 24, 24);
            g2.setColor(new Color(220, 118, 51));
            g.drawOval(42, 13, 24, 24);
        }
    }

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(MouseEvent e) {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, new String(), e.getWhen(), e.getModifiers());
        synchronized(listeners) {
            for (int i = 0; i < listeners.size(); i++) {
                ActionListener tmp = listeners.get(i);
                tmp.actionPerformed(evt);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(size);
    }
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public boolean isOn() {
        return isOn;
    }

    public void isOn(boolean onState) {
        isOn = onState;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        isOn = !isOn;
        repaint();
        notifyListeners(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
