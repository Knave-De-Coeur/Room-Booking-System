/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objects;

import java.sql.Time;

/**
 *
 * @author alexa
 */
public class UsersBookedRooms {
    private String roomName;
    private String moduleName;
    private Time start_at;
    private Time end_at;

    public UsersBookedRooms(String roomName, String moduleName, Time start_at, Time end_at) {
        this.roomName = roomName;
        this.moduleName = moduleName;
        this.start_at = start_at;
        this.end_at = end_at;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Time getStart_at() {
        return start_at;
    }

    public void setStart_at(Time start_at) {
        this.start_at = start_at;
    }

    public Time getEnd_at() {
        return end_at;
    }

    public void setEnd_at(Time end_at) {
        this.end_at = end_at;
    }

    @Override
    public String toString() {
        return "UsersBookedRooms{" + "roomName=" + roomName + ", moduleName=" + moduleName + ", start_at=" + start_at + ", end_at=" + end_at + '}';
    }
}
