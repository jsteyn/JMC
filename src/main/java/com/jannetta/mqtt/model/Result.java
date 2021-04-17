package com.jannetta.mqtt.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("MQTTSubscriptions")
    @Expose
    private List<Subscription> subscriptions = null;

    public void setMQTTSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    /**
     * @return the subscriptions
     */
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    /**
     * @param subscriptions the subscriptions to set
     */
    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

}