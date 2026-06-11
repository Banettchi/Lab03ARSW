package edu.eci.arsw.tcp;

public class Room {
    private String id;
    private boolean isAvailable;

    public Room(String id) {
        this.id = id;
        this.isAvailable = true;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

