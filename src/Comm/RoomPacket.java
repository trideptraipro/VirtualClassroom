package Comm;

import Model.Room;

import java.io.Serializable;

public class RoomPacket implements Serializable {
    Room r;

    public RoomPacket(Room r) {
        this.r = r;
    }

    public Room getR() {
        return r;
    }

    public void setR(Room r) {
        this.r = r;
    }
}
