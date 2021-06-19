package Comm;

import Model.Account;
import Model.UserInfo;

import java.io.Serializable;

public class RegisterPacket implements Serializable {
    Account account;
    UserInfo userInfo;

    public RegisterPacket(Account account, UserInfo userInfo) {
        this.account = account;
        this.userInfo = userInfo;
    }

    public Account getAccount() {
        return account;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
