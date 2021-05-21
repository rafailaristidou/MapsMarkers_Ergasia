package com.example.mapsmarkers_ergasia;

import com.google.firebase.firestore.GeoPoint;


public class Marker {
    private GeoPoint Location;
    private float Color;
    private String TypeMetrisis;
    private float ValueMetrisis;
    private String desc;

    public Marker() {
    }

    public GeoPoint getLocation() {
        return Location;
    }

    public void setLocation(GeoPoint location) {
        Location = location;
    }

    public float getColor() {
        return Color;
    }

    public void setColor(float color) {
        Color = color;
    }

    public String getTypeMetrisis() {
        return TypeMetrisis;
    }

    public void setTypeMetrisis(String typeMetrisis) {
        TypeMetrisis = typeMetrisis;
    }

    public float getValueMetrisis() {
        return ValueMetrisis;
    }

    public void setValueMetrisis(float valueMetrisis) {
        ValueMetrisis = valueMetrisis;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
