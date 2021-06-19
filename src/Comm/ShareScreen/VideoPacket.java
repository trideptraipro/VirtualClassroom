package Comm.ShareScreen;

import java.io.Serializable;

public class VideoPacket implements Serializable {
    byte[] data;
    int length;

    public VideoPacket(byte[] data, int length) {
        this.data = data;
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public int getLength() {
        return length;
    }
}
