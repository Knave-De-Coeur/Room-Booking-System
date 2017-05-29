/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objects;

/**
 *
 * @author alexa
 */
public class AdminBookingInput {
    
    private String roomName;
    private String timesoltName;
    private String moduleName;
    private int adminId;

    public AdminBookingInput(String roomName, String timesoltName, String moduleName, int adminId) {
        this.roomName = roomName;
        this.timesoltName = timesoltName;
        this.moduleName = moduleName;
        this.adminId = adminId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTimesoltName() {
        return timesoltName;
    }

    public void setTimesoltName(String timesoltName) {
        this.timesoltName = timesoltName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "AdminBookingInput{" + "roomName=" + roomName + ", timesoltName=" + timesoltName + ", moduleName=" + moduleName + '}';
    }
}
