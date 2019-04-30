package com.example.familylocation;
import java.util.Calendar;
import java.util.Date;


public class eventRecord {
    String currentTime;
    String nickName;
    String event;
    String uid;
    String lat,longt;

    public eventRecord() {

    }

    public eventRecord(String currentTime, String nickName, String event, String uid, String lat, String longt){
        this.currentTime = currentTime;
        this.nickName = nickName;
        this.event = event;
        this.uid =  uid;
        this.lat = lat;
        this.longt = longt;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
