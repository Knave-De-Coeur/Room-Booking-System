package com.objects;

import java.sql.Time;

public class TimeSlot {
    
    private int id;
    private Time start_at;
    private Time end_at;
    private String name;

    public TimeSlot(int id, Time start_at, Time end_at, String name) {
        this.id = id;
        this.start_at = start_at;
        this.end_at = end_at;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
