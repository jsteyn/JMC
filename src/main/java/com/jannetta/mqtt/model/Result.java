package com.jannetta.mqtt.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("Subscriptions")
    @Expose
    private List<Subscription> subscriptions = null;

    public List<Subscription> getsubscriptions() {
        return subscriptions;
    }

    public void setMQTTSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

}