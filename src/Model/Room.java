package Model;

import java.io.Serializable;
import java.sql.Date;

public class Room implements Serializable {
    int id;
    String roomname;
    Date createdate;
    String createdBy;
    public Room(){}

    public Room(int id, String roomname, Date createdate, String createdBy) {
        this.id = id;
        this.roomname = roomname;
        this.createdate = createdate;
        this.createdBy = createdBy;
    }

    public Room(String roomname, Date createdate, String createdBy) {
        this.roomname = roomname;
        this.createdate = createdate;
        this.createdBy = createdBy;
    }

    public Room(String roomname, String createdBy) {
        this.roomname = roomname;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
