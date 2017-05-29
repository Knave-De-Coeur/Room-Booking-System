package com.objects;

public class Room {
    
    private int id;
    private String roomName;
    private String type;
    private int size;

    public Room(int id, String roomName, String type, int size) {
        this.id = id;
        this.roomName = roomName;
        this.type = type;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", roomName=" + roomName + ", type=" + type + ", size=" + size + '}';
    }
    
}
