package com.jannetta.mqtt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MQTTSubscription {

    @SerializedName("widget")
    @Expose
    private String widget;
    @SerializedName("protocol")
    @Expose
    private String protocol;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("port")
    @Expose
    private String port;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("colour")
    @Expose
    private String colour;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("timestamp")
    @Expose
    private boolean timestap;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("filename")
    @Expose
    private String filename;

    public String getWidget() {
        return widget;
    }

    public void setWidget(String widget) {
        this.widget = widget;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isTimestamp() {
        return timestap;
    }

    public void setTimestap(boolean timestap) {
        this.timestap = timestap;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}