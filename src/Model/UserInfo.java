package Model;

import java.io.Serializable;

public class UserInfo implements Serializable {
    int id;
    String username, name;
    int phonenumber;
    String address;
    public UserInfo(){}

    public UserInfo(int id, String username, String name, int phonenumber, String address) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public UserInfo(String username, String name, int phonenumber, String address) {
        this.username = username;
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
