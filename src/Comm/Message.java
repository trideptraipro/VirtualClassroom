package Comm;

import java.io.Serializable;

public class Message implements Serializable {
String mess;//tin nhan tu nguoi gui
Object object;//object can be string, list, ... and class implement Serializable
private int cID;//...
    public Message(){}

    public int getcID() {
        return cID;
    }

    public void setcID(int cID) {
        this.cID = cID;
    }

    public Message(String mess, Object object, int cID) {
        this.mess = mess;
        this.object = object;
        this.cID = cID;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Message(String mess, Object object) {
        this.mess = mess;
        this.object = object;
    }
}
