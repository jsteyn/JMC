package com.jannetta.mqtt.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("MQTTSubscriptions")
    @Expose
    private List<MQTTSubscription> mQTTSubscriptions = null;

    public List<MQTTSubscription> getMQTTSubscriptions() {
        return mQTTSubscriptions;
    }

    public void setMQTTSubscriptions(List<MQTTSubscription> mQTTSubscriptions) {
        this.mQTTSubscriptions = mQTTSubscriptions;
    }

}