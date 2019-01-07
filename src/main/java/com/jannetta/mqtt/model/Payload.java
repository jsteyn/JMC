
package com.jannetta.mqtt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("Units")
    @Expose
    private String units;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Architecture")
    @Expose
    private String architecture;
    @SerializedName("IPAddress")
    @Expose
    private String iPAddress;
    @SerializedName("MACAddress")
    @Expose
    private String mACAddress;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public String getMACAddress() {
        return mACAddress;
    }

    public void setMACAddress(String mACAddress) {
        this.mACAddress = mACAddress;
    }

}