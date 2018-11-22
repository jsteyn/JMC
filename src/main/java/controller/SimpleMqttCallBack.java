package controller;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import view.Widget;

public class SimpleMqttCallBack implements MqttCallback {
    Widget widget;

    public SimpleMqttCallBack(Widget widget) {
        this.widget = widget;
    }
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("message received");
        widget.setRetained(mqttMessage.isRetained());
        widget.update(topic, mqttMessage);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}