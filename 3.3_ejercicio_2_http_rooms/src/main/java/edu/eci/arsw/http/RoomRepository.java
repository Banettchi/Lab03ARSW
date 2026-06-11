package edu.eci.arsw.http;

import java.util.HashMap;
import java.util.Map;

public class RoomRepository {
    private Map<String, Room> rooms = new HashMap<>();

    public RoomRepository() {
        rooms.put("E301", new Room("E301"));
        rooms.put("E302", new Room("E302"));
        rooms.put("E303", new Room("E303"));
        rooms.put("E304", new Room("E304"));
    }

    public Room findById(String id) {
        return rooms.get(id);
    }
    
    public Map<String, Room> findAll() {
        return rooms;
    }
}

