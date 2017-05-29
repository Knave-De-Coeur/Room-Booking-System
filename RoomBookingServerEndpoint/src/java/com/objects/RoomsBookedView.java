/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objects;

import java.sql.Time;

/**
 *
 * @author alexanderjames
 */
public class RoomsBookedView {
    private String roomName;
    private String moduleName;
    private String lecturerName;
    private String lecturerSurname;
    private Time start_at;
    private Time end_at;

    public RoomsBookedView(String roomName, String moduleName, String lecturerName, String lecturerSurname, Time start_at, Time end_at) {
        this.roomName = roomName;
        this.moduleName = moduleName;
        this.lecturerName = lecturerName;
        this.lecturerSurname = lecturerSurname;
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

    public String getLectureName() {
        return lecturerName;
    }

    public void setLectureName(String lectureName) {
        this.lecturerName = lectureName;
    }

    public String getLecturerSurname() {
        return lecturerSurname;
    }

    public void setLecturerSurname(String lecturerSurname) {
        this.lecturerSurname = lecturerSurname;
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
        return "RoomsBookedView{" + "roomName=" + roomName + ", moduleName=" + moduleName + ", lecturerName=" + lecturerName + ", lecturerSurname=" + lecturerSurname + ", start_at=" + start_at + ", end_at=" + end_at + '}';
    }
}
