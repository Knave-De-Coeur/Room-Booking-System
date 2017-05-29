package com.objects;

import java.sql.Date;

public class RoomBooked {
    
    private int id;
    private int Room_id;
    private int Module_id;
    private int TimeSlot_id;
    private Date DayBooked;
    private int User_id;

    public RoomBooked(int id, int Room_id, int Module_id, int TimeSlot_id, Date DayBooked, int User_id) {
        this.id = id;
        this.Room_id = Room_id;
        this.Module_id = Module_id;
        this.TimeSlot_id = TimeSlot_id;
        this.DayBooked = DayBooked;
        this.User_id = User_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return Room_id;
    }

    public void setRoom_id(int Room_id) {
        this.Room_id = Room_id;
    }

    public int getModule_id() {
        return Module_id;
    }

    public void setModule_id(int Module_id) {
        this.Module_id = Module_id;
    }

    public int getTimeSlot_id() {
        return TimeSlot_id;
    }

    public void setTimeSlot_id(int TimeSlot_id) {
        this.TimeSlot_id = TimeSlot_id;
    }

    public Date getDayBooked() {
        return DayBooked;
    }

    public void setDayBooked(Date DayBooked) {
        this.DayBooked = DayBooked;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int User_id) {
        this.User_id = User_id;
    }

    @Override
    public String toString() {
        return "RoomBooked{" + "id=" + id + ", Room_id=" + Room_id + ", Module_id=" + Module_id + ", TimeSlot_id=" + TimeSlot_id + ", DayBooked=" + DayBooked + ", User_id=" + User_id + '}';
    }
    
}
